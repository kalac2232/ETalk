package com.kalac.etalk.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kalac.etalk.R;
import com.kalac.etalk.Utils.UIUtil;

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //延迟一秒后进入主界面或问卷调查界面
        UIUtil.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(UIUtil.getContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);
    }
}
