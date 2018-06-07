package com.kalac.etalk.Fragments.TrainPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kalac.etalk.Activites.SpeechActivity;
import com.kalac.etalk.Fragments.BaseFragment;
import com.kalac.etalk.R;
import com.kalac.etalk.Utils.UIUtil;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class SpeechFragment extends BaseFragment {

    private ListView lvSpeechHomeList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speech, null);
        initView(view);

        return view;
    }

    private static final String TAG = "SpeechFragment";
    private void initView(View view) {
        lvSpeechHomeList = view.findViewById(R.id.lv_speechhomelist);
        SpeechAdapter speechAdapter = new SpeechAdapter();
        lvSpeechHomeList.setAdapter(speechAdapter);
    }
    class SpeechAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Button button = new Button(UIUtil.getContext());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UIUtil.getContext(), SpeechActivity.class));
                    RongIMClient.getInstance().joinChatRoom("123456", 0, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(UIUtil.getContext(),"加入聊天室成功",Toast.LENGTH_SHORT).show();
                            // 构造 TextMessage 实例
                            TextMessage myTextMessage = TextMessage.obtain("我是消息内容");
                            Message myMessage = Message.obtain("123456", Conversation.ConversationType.CHATROOM,myTextMessage);
                            RongIMClient.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {

                                public void onAttached(Message message) {
                                    //消息本地数据库存储成功的回调
                                    Log.i(TAG, "onAttached: 消息本地数据库存储成功的回调");
                                }

                                @Override
                                public void onSuccess(Message message) {
                                    //消息通过网络发送成功的回调
                                    Log.i(TAG, "onSuccess: 消息通过网络发送成功的回调");
                                }

                                @Override
                                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                                    //消息发送失败的回调
                                    Log.i(TAG, "onError: 消息发送失败的回调");
                                }
                            });
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            Toast.makeText(UIUtil.getContext(),"加入聊天室失败"+errorCode.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            return button;
        }
    }
}
