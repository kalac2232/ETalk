package com.kalac.etalk.Fragments.TrainPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kalac.etalk.Fragments.BaseFragment;
import com.kalac.etalk.R;

public class AllHomeFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allhome, null);
        return view;
    }
}
