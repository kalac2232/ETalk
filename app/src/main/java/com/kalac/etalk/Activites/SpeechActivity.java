package com.kalac.etalk.Activites;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.kalac.etalk.Adapter.ChatAdapter;
import com.kalac.etalk.JoinChatRoomStatusMessage;
import com.kalac.etalk.R;
import com.kalac.etalk.Utils.UIUtil;
import com.lqr.audio.AudioPlayManager;
import com.lqr.audio.AudioRecordManager;
import com.lqr.audio.IAudioPlayListener;
import com.lqr.audio.IAudioRecordListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.utilities.RongUtils;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.ChatRoomInfo;
import io.rong.imlib.model.ChatRoomMemberInfo;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.CommandNotificationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import io.rong.push.RongPushClient;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class SpeechActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private ListView lvChatContent;
    private ArrayList<Message> mMessageArray;
    private ArrayList<String> mOnlineMember;
    private FrameLayout ffSendToggle;
    private LinearLayout llInputText;
    private Button btnInputVoice;
    private static final int Input_Voice = 1;
    private static final int Input_Text = 0;
    private static int Input_Statues = Input_Text;
    private ImageView ivTextToggle;
    private ImageView ivVoiceToggle;
    private ImageView iv_seating;
    private int mVirtualKeyHeight;
    private EditText etInputText;
    private FrameLayout flSendToggle;
    private ChatAdapter mChatAdapter;
    private String chatRoomId;
    private static final String TAG = "SpeechActivity";
    private ImageView ivHeadIcon;
    private TextView tvNickname;
    private File mAudioDir;
    private View llSpeechLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        Intent intent = getIntent();
        chatRoomId = intent.getStringExtra("chatRoomId");
        RongIMClient.getInstance().getChatRoomInfo(chatRoomId,6,
                ChatRoomInfo.ChatRoomMemberOrder.RC_CHAT_ROOM_MEMBER_ASC,new myResultCallback_ChatRoomInfo());
        mMessageArray = new ArrayList<>();
        init();
        initView();
        initListener();
    }

    private void init() {
        String[] permissions ={Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, permissions)) {
            //具备权限 直接进行操作
        } else {
            //权限拒绝 申请权限
            EasyPermissions.requestPermissions(this, "", 0, permissions);
        }
        AudioRecordManager.getInstance(this).setMaxVoiceDuration(12);
        mAudioDir = new File(Environment.getExternalStorageDirectory(), "Etalk_voice");
        if (!mAudioDir.exists()) {
            mAudioDir.mkdirs();
        }
        //设置融云消息的监听
        RongIMClient.setOnReceiveMessageListener(new ReceiveMessageListener());
        AudioRecordManager.getInstance(this).setAudioSavePath(mAudioDir.getAbsolutePath());
    }

    private void initView() {
        iv_seating = findViewById(R.id.iv_Seating);
        View statusBar = findViewById(R.id.statusBarView);
        //根据状态栏高度设置占位控件的高度
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
        lvChatContent = findViewById(R.id.lv_speech_chatcontent);
        llInputText = findViewById(R.id.ll_input_text);
        btnInputVoice = findViewById(R.id.btn_input_voice);
        ivTextToggle = findViewById(R.id.iv_text_toggle);
        ivVoiceToggle = findViewById(R.id.iv_voice_toggle);
        etInputText = findViewById(R.id.rc_edit_text);
        flSendToggle = findViewById(R.id.rc_send_toggle);
        //主布局
        llSpeechLayout = findViewById(R.id.rl_speechLayout);
        //解决输入法顶布局的问题
        controlKeyboardLayout(llSpeechLayout);
        //设置数据适配器
        mChatAdapter = new ChatAdapter(mMessageArray);
        lvChatContent.setAdapter(mChatAdapter);
        //设置监听
        ivTextToggle.setOnClickListener(this);
        ivVoiceToggle.setOnClickListener(this);
        flSendToggle.setOnClickListener(this);
        etInputText.addTextChangedListener(this);
        showInputBar();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发到EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    /**
     * 请求权限成功。
     * 可以弹窗显示结果，也可执行具体需要的逻辑操作
     *
     * @param requestCode
     * @param perms
     */
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Toast.makeText(getApplicationContext(), "用户授权成功",Toast.LENGTH_SHORT).show();
    }
    /**
     * 请求权限失败
     *
     * @param requestCode
     * @param perms
     */
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(getApplicationContext(), "用户授权失败",Toast.LENGTH_SHORT).show();
        /**
         * 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
         * 这时候，需要跳转到设置界面去，让用户手动开启。
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
    private void initListener() {
        btnInputVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        AudioRecordManager.getInstance(UIUtil.getContext()).startRecord();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isCancelled(v, event)) {
                            AudioRecordManager.getInstance(UIUtil.getContext()).willCancelRecord();
                        } else {
                            AudioRecordManager.getInstance(UIUtil.getContext()).continueRecord();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        AudioRecordManager.getInstance(UIUtil.getContext()).stopRecord();
                        AudioRecordManager.getInstance(UIUtil.getContext()).destroyRecord();
                        break;
                }
                return false;
            }

        });

        AudioRecordManager.getInstance(this).setAudioRecordListener(new IAudioRecordListener() {

            private TextView mTimerTV;
            private TextView mStateTV;
            private ImageView mStateIV;
            private PopupWindow mRecordWindow;

            @Override
            public void initTipView() {
                View view = View.inflate(UIUtil.getContext(), R.layout.popup_audio_wi_vo, null);
                mStateIV = (ImageView) view.findViewById(R.id.rc_audio_state_image);
                mStateTV = (TextView) view.findViewById(R.id.rc_audio_state_text);
                mTimerTV = (TextView) view.findViewById(R.id.rc_audio_timer);
                mRecordWindow = new PopupWindow(view, -1, -1);
                mRecordWindow.showAtLocation(llSpeechLayout, 17, 0, 0);
                mRecordWindow.setFocusable(true);
                mRecordWindow.setOutsideTouchable(false);
                mRecordWindow.setTouchable(false);
            }

            @Override
            public void setTimeoutTipView(int counter) {
                if (this.mRecordWindow != null) {
                    this.mStateIV.setVisibility(View.GONE);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_rec);
                    this.mStateTV.setBackgroundResource(R.drawable.bg_voice_popup);
                    this.mTimerTV.setText(String.format("%s", new Object[]{Integer.valueOf(counter)}));
                    this.mTimerTV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void setRecordingTipView() {
                if (this.mRecordWindow != null) {
                    this.mStateIV.setVisibility(View.VISIBLE);
                    this.mStateIV.setImageResource(R.mipmap.ic_volume_1);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_rec);
                    this.mStateTV.setBackgroundResource(R.drawable.bg_voice_popup);
                    this.mTimerTV.setVisibility(View.GONE);
                }
            }

            @Override
            public void setAudioShortTipView() {
                if (this.mRecordWindow != null) {
                    mStateIV.setImageResource(R.mipmap.ic_volume_wraning);
                    mStateTV.setText(R.string.voice_short);
                }
            }

            @Override
            public void setCancelTipView() {
                if (this.mRecordWindow != null) {
                    this.mTimerTV.setVisibility(View.GONE);
                    this.mStateIV.setVisibility(View.VISIBLE);
                    this.mStateIV.setImageResource(R.mipmap.ic_volume_cancel);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_cancel);
                    this.mStateTV.setBackgroundResource(R.drawable.corner_voice_style);
                }
            }

            @Override
            public void destroyTipView() {
                if (this.mRecordWindow != null) {
                    this.mRecordWindow.dismiss();
                    this.mRecordWindow = null;
                    this.mStateIV = null;
                    this.mStateTV = null;
                    this.mTimerTV = null;
                }
            }

            @Override
            public void onStartRecord() {
                //开始录制
            }

            @Override
            public void onFinish(Uri audioPath, int duration) {
                recordVoice(audioPath, duration);
            }

            @Override
            public void onAudioDBChanged(int db) {
                switch (db / 5) {
                    case 0:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_1);
                        break;
                    case 1:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_2);
                        break;
                    case 2:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_3);
                        break;
                    case 3:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_4);
                        break;
                    case 4:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_5);
                        break;
                    case 5:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_6);
                        break;
                    case 6:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_7);
                        break;
                    default:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_8);
                }
            }
        });
    }

    private void recordVoice(Uri audioPath, int duration) {
        File file = new File(audioPath.getPath());
        if (file.exists()) {
            Toast.makeText(getApplicationContext(), "录制成功", Toast.LENGTH_SHORT).show();
            VoiceMessage vocMsg = VoiceMessage.obtain(audioPath, duration);

            RongIMClient.getInstance().sendMessage(Conversation.ConversationType.CHATROOM, chatRoomId, vocMsg, null, null, new IRongCallback.ISendMessageCallback() {

                @Override
                public void onAttached(Message message) {

                }

                @Override
                public void onSuccess(Message message) {
                    Log.i(TAG, "onSuccess: 语音消息发送成功");
                    mMessageArray.add(message);
                    mChatAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                    Log.i(TAG, "onError: 语音发送失败");
                }
            } );
//
        }
    }

    private boolean isCancelled(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        if (event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth() || event.getRawY() < location[1] - 40) {
            return true;
        }
        return false;
    }


    private void controlKeyboardLayout(final View rootLayout) {
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                // 获取当前页面窗口的显示范围
                rootLayout.getWindowVisibleDisplayFrame(r);
                //获取屏幕高度
                int screenHeight = rootLayout.getRootView().getHeight();
                //获取输入法的高度 (r.bottom本应该是等于屏幕高度的 但是还包括了虚拟按键的高度 所以获取的输入法的高度还包括了一个虚拟按键的高度)
                int softHeight = screenHeight - (r.bottom);
                //如果测量的输入法高度大于屏幕的1/5，则说明输入法打开
                if (softHeight > screenHeight / 5) {
                    //将聊天输入框（顶）上去
                    riseEditText(softHeight - mVirtualKeyHeight);
                } else {
                    //如果小于1/5，说明输入法未打开，记录此时的测量值（实际为虚拟按键的高度）
                    mVirtualKeyHeight = softHeight;
                    //将聊天输入框（降）下来
                    dropEditText();
                }
            }
        });
    }
    /**
     * 升聊天输入框 （设置聊天框下占位控件的高度）
     * @param keyboardHeight 升起的高度
     */
    private void riseEditText(int keyboardHeight) {
        ViewGroup.LayoutParams params = iv_seating.getLayoutParams();
        params.height = keyboardHeight;

        iv_seating.setLayoutParams(params);
    }

    /**
     * 收起输入法的时候降下输入框
     */
    private void dropEditText() {
        ViewGroup.LayoutParams params = iv_seating.getLayoutParams();
        //如果之前就是为关闭状态，则直接退出
        if (params.height == 0) {
            return;
        }
        params.height = 0;

        iv_seating.setLayoutParams(params);
    }

    /**
     * 更新输入bar
     */
    private void showInputBar() {
        if (Input_Statues == Input_Text) {
            llInputText.setVisibility(View.VISIBLE);
            btnInputVoice.setVisibility(View.GONE);
            ivTextToggle.setVisibility(View.GONE);
            ivVoiceToggle.setVisibility(View.VISIBLE);
        } else {
            llInputText.setVisibility(View.GONE);
            btnInputVoice.setVisibility(View.VISIBLE);
            ivTextToggle.setVisibility(View.VISIBLE);
            ivVoiceToggle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.iv_text_toggle:
                changeInputStatus();
                break;
            case R.id.iv_voice_toggle:
                changeInputStatus();
                break;
            case R.id.rc_send_toggle:
                final String s = etInputText.getText().toString();
                TextMessage textMessage = TextMessage.obtain(s);
                RongIMClient.getInstance().sendMessage(Conversation.ConversationType.CHATROOM, chatRoomId, textMessage, null, null, new IRongCallback.ISendMessageCallback() {
                    @Override
                    public void onAttached(Message message) {
                        // 消息成功存到本地数据库的回调
                    }

                    @Override
                    public void onSuccess(Message message) {
                        // 消息发送成功的回调
                        mMessageArray.add(message);
                        mChatAdapter.notifyDataSetChanged();
                        etInputText.setText("");
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        // 消息发送失败的回调
                    }
                });

                break;
            default:
                break;
        }
    }

    private void changeInputStatus() {
        if (Input_Statues == Input_Text) {
            Input_Statues = Input_Voice;
        } else {
            Input_Statues = Input_Text;
        }
        showInputBar();
    }

    @Override
    protected void onPause() {
        super.onPause();
        quitChatRoom();
    }

    @Override
    public void finish() {
        super.finish();
        quitChatRoom();
    }
    private void joinChatRoom() {

    }
    private void quitChatRoom() {
        RongIMClient.getInstance().quitChatRoom(chatRoomId, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(UIUtil.getContext(),"退出聊天室成功",Toast.LENGTH_SHORT).show();
                JoinChatRoomStatusMessage message = JoinChatRoomStatusMessage.obtain("out");
                RongIMClient.getInstance().sendMessage(Conversation.ConversationType.CHATROOM, chatRoomId, message, null, null, new IRongCallback.ISendMessageCallback() {
                    @Override
                    public void onAttached(Message message) {
                        // 消息成功存到本地数据库的回调
                    }
                    @Override
                    public void onSuccess(Message message) {
                        // 消息发送成功的回调
                        Log.i(TAG, "onSuccess: 发送状态成功");
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        // 消息发送失败的回调
                    }
                });
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length()>0) {
            flSendToggle.setVisibility(View.VISIBLE);
        } else {
            flSendToggle.setVisibility(View.GONE);
        }
    }



    class myResultCallback_ChatRoomInfo extends RongIMClient.ResultCallback<ChatRoomInfo> {
        @Override
        public void onSuccess(ChatRoomInfo chatRoomInfo) {
            mOnlineMember = new ArrayList<String> ();
            List<ChatRoomMemberInfo> memberInfo = chatRoomInfo.getMemberInfo();
            Log.i(TAG, "onSuccess: memberInfo"+memberInfo.size());
            for (ChatRoomMemberInfo memberInfo1 : memberInfo) {
                String userId = memberInfo1.getUserId();
                mOnlineMember.add(userId);
            }
            Toast.makeText(UIUtil.getContext(),"获取成员信息成功",Toast.LENGTH_SHORT).show();
            updataIconInfo();
        }

        @Override
        public void onError(RongIMClient.ErrorCode errorCode) {
            Toast.makeText(UIUtil.getContext(),"获取成员信息失败:"+errorCode.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void updataIconInfo() {

        UIUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0;i<6;i++) {
                    if (i<mOnlineMember.size()) {
                        tvNickname = findViewById(R.id.tv_nickname_no1+i);
                        tvNickname.setText(mOnlineMember.get(i));
                    } else {
                        tvNickname = findViewById(R.id.tv_nickname_no1+i);
                        tvNickname.setText("空");
                    }
                }

            }
        });
    }

    class ReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {

        @Override
        public boolean onReceived(Message message, int i) {
            if (message.getConversationType() == Conversation.ConversationType.CHATROOM) {
                if (message.getContent() instanceof JoinChatRoomStatusMessage) {
                    someBodyJoinoOrExitRoom(message);

                } else {
                    mMessageArray.add(message);
                    UIUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mChatAdapter.notifyDataSetChanged();
                            lvChatContent.setSelection(mMessageArray.size()-1);
                        }
                    });
                }

            }
            return false;
        }

        private void someBodyJoinoOrExitRoom(Message message) {
            final String senderUserId = message.getSenderUserId();
            JoinChatRoomStatusMessage content = (JoinChatRoomStatusMessage)message.getContent();
            final String messageContent = content.getContent();
            UIUtil.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(UIUtil.getContext(),senderUserId+ " " +messageContent,Toast.LENGTH_SHORT).show();
                }
            });
            if (messageContent.equals("in")) {
                mOnlineMember.add(senderUserId);
            } else {
                mOnlineMember.remove(senderUserId);
            }

            updataIconInfo();
        }
    }
}
