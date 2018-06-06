package com.kalac.etalk.Fragments.TrainPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.kalac.etalk.Activites.SpeechActivity;
import com.kalac.etalk.Fragments.BaseFragment;
import com.kalac.etalk.R;
import com.kalac.etalk.Utils.UIUtil;

public class SpeechFragment extends BaseFragment {

    private ListView lvSpeechHomeList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speech, null);
        initView(view);

        return view;
    }

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
                }
            });

            return button;
        }
    }
}
