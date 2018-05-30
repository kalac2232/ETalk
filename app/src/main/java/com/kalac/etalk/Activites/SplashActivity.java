package com.kalac.etalk.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kalac.etalk.R;
import com.kalac.etalk.Utils.ConstantValue;
import com.kalac.etalk.Utils.SharePreferenceUtil;
import com.kalac.etalk.Utils.UIUtil;

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //延迟一秒后进入登陆界面或主界面
        UIUtil.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isLogin = SharePreferenceUtil.getBoolean(UIUtil.getContext(), ConstantValue.ISLOGIN, false);
                if (isLogin) {
                    //如果登陆过了就跳主界面
                    Intent intent = new Intent(UIUtil.getContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(UIUtil.getContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        },1000);
    }
}
