package com.kalac.etalk;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;

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
    }
}
