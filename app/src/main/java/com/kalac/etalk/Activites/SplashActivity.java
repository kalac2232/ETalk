package com.kalac.etalk.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kalac.etalk.R;
import com.kalac.etalk.Utils.ConstantValue;
import com.kalac.etalk.Utils.SharePreferenceUtil;
import com.kalac.etalk.Utils.UIUtil;

import io.rong.imlib.RongIMClient;

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
                    //String token = "MimZR7hBk9F/M7MNM+QMgQEe5pwFA7hxdJPBO9xGiHoaYiiFpSgy1U+cKytdX2XR8Z9iPyLuh57h03cpuM8A8MQstPJ89e1i";
                    String token = "i/szuVxPlPyfE9qIo2B7HYSD09h7G7tzzZeiKbeLF0BF3j8SHe4ZUuf13ZBcYG97u15ko9dyd1twgg0w3Y6jlxS21K1avh/p";
                    RongIMClient.connect(token, new RongIMClient.ConnectCallback(){
                        @Override
                        public void onSuccess(String s) {

                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }

                        @Override
                        public void onTokenIncorrect() {

                        }
                    });
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
