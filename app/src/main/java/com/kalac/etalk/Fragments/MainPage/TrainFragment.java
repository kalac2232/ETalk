package com.kalac.etalk.Fragments.MainPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kalac.etalk.Fragments.BaseFragment;
import com.kalac.etalk.Fragments.TrainPage.AllHomeFragment;
import com.kalac.etalk.Fragments.TrainPage.EntertainmentFragment;
import com.kalac.etalk.Fragments.TrainPage.LiveFragment;
import com.kalac.etalk.Fragments.TrainPage.SpeechFragment;
import com.kalac.etalk.R;
import com.kalac.etalk.Utils.UIUtil;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class TrainFragment extends BaseFragment {
    private static final String TAG = "TrainFragment";
    private static final String[] CONTENT = new String[] { "全部房间", "娱乐房间", "直播房间", "演讲房间" };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train, null);
        ViewPager vpTrain = view.findViewById(R.id.vp_train);
        TabPageIndicator indicator = view.findViewById(R.id.indicator);

        //构造适配器
        List<Fragment> fragments=new ArrayList<Fragment>();
        fragments.add(new AllHomeFragment());
        fragments.add(new EntertainmentFragment());
        fragments.add(new LiveFragment());
        fragments.add(new SpeechFragment());
        PagerAdapter adapter = new PagerAdapter(getFragmentManager(), fragments);

        //设定适配器
        vpTrain.setAdapter(adapter);
        indicator.setViewPager(vpTrain);

//        Button viewById = view.findViewById(R.id.bt);
//        viewById.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RongIMClient.getInstance().joinChatRoom("123456", 0, new RongIMClient.OperationCallback() {
//                    @Override
//                    public void onSuccess() {
//                        Toast.makeText(UIUtil.getContext(),"加入聊天室成功",Toast.LENGTH_SHORT).show();
//                        // 构造 TextMessage 实例
//                        TextMessage myTextMessage = TextMessage.obtain("我是消息内容");
//                        Message myMessage = Message.obtain("123456", Conversation.ConversationType.CHATROOM,myTextMessage);
//                        RongIMClient.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
//
//                            public void onAttached(Message message) {
//                                //消息本地数据库存储成功的回调
//                                Log.i(TAG, "onAttached: 消息本地数据库存储成功的回调");
//                            }
//
//                            @Override
//                            public void onSuccess(Message message) {
//                                //消息通过网络发送成功的回调
//                                Log.i(TAG, "onSuccess: 消息通过网络发送成功的回调");
//                            }
//
//                            @Override
//                            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
//                                //消息发送失败的回调
//                                Log.i(TAG, "onError: 消息发送失败的回调");
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onError(RongIMClient.ErrorCode errorCode) {
//                        Toast.makeText(UIUtil.getContext(),"加入聊天室失败"+errorCode.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
        return view;
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            // TODO Auto-generated constructor stub
            mFragments = fragments;
        }


        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }
        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}