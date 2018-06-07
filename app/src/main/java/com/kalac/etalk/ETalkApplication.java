package com.kalac.etalk;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.util.Log;

import com.kalac.etalk.Utils.FontUtils;

import org.json.JSONObject;

import io.rong.imkit.RongIM;
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
        RongIMClient.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                MessageContent content = message.getContent();
                Log.i(TAG, "onReceived: content"+content);
                Conversation.ConversationType conversationType = message.getConversationType();
                Log.i(TAG, "onReceived: conversationType"+conversationType.getName());

                return false;
            }
        });
    }
}
