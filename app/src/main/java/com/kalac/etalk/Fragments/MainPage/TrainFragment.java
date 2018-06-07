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