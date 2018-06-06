package com.kalac.etalk.Activites;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.TextView;

import com.kalac.etalk.R;
import com.kalac.etalk.Utils.UIUtil;

import java.util.ArrayList;

public class SpeechActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private ListView lvChatContent;
    private ArrayList<String> list;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        list = new ArrayList<>();
        for (int i = 0;i<15;i++) {
            String s = "测试数据";
            list.add(s+i);
        }
        initView();
    }

    private void initView() {
        iv_seating = findViewById(R.id.iv_Seating);
        View statusBar = findViewById(R.id.statusBarView);
        //根据状态栏高度设置占位控件的高度
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
        lvChatContent = findViewById(R.id.lv_speech_chatcontent);
        mChatAdapter = new ChatAdapter();
        lvChatContent.setAdapter(mChatAdapter);
        llInputText = findViewById(R.id.ll_input_text);
        btnInputVoice = findViewById(R.id.btn_input_voice);
        ivTextToggle = findViewById(R.id.iv_text_toggle);
        ivVoiceToggle = findViewById(R.id.iv_voice_toggle);
        etInputText = findViewById(R.id.rc_edit_text);
        flSendToggle = findViewById(R.id.rc_send_toggle);
        View llSpeechLayout = findViewById(R.id.rl_speechLayout);
        controlKeyboardLayout(llSpeechLayout);
        ivTextToggle.setOnClickListener(this);
        ivVoiceToggle.setOnClickListener(this);
        flSendToggle.setOnClickListener(this);
        etInputText.addTextChangedListener(this);
        showInputBar();
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
                String s = etInputText.getText().toString();
                list.add(s);
                mChatAdapter.notifyDataSetChanged();
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

    class ChatAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(UIUtil.getContext());
            textView.setText(list.get(position));
            textView.setTextColor(Color.BLACK);
            return textView;
        }
    }
}
