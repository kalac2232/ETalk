package com.kalac.etalk;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;

import com.kalac.etalk.Utils.FontUtils;

public class ETalkApplication extends Application {
    public static int getMainThreadId() {
        return mainThreadId;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static Context getContext() {
        return context;
    }

    private static int mainThreadId;
    private static Handler handler;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = Process.myTid();
        //全局替换字体
        FontUtils.getInstance().replaceSystemDefaultFontFromAsset(this, "fonts/mini_yuan.ttf");
    }
}
