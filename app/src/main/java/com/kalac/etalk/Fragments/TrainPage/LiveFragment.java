package com.kalac.etalk.Fragments.TrainPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kalac.etalk.Fragments.BaseFragment;
import com.kalac.etalk.R;

public class LiveFragment extends BaseFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live, null);
        return view;
    }
}
