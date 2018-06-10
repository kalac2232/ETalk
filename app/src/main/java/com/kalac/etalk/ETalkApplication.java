package com.kalac.etalk;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.util.Log;

import com.kalac.etalk.Activites.SpeechActivity;
import com.kalac.etalk.Utils.FontUtils;

import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.AnnotationNotFoundException;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

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
    private static final String TAG = "ETalkApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = Process.myTid();
        //全局替换字体
        FontUtils.getInstance().replaceSystemDefaultFontFromAsset(this, "fonts/mini_yuan.ttf");
        RongIMClient.init(this);
        try {
            RongIMClient.registerMessageType(JoinChatRoomStatusMessage.class);
        } catch (AnnotationNotFoundException e) {
            e.printStackTrace();
        }

    }
}
