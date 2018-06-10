package com.kalac.etalk.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.kalac.etalk.Utils.UIUtil;
import com.lqr.audio.AudioRecordManager;

public class SendVoiceButton extends android.support.v7.widget.AppCompatButton {
    public SendVoiceButton(Context context) {
        super(context);
        init();
    }


    public SendVoiceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SendVoiceButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                AudioRecordManager.getInstance(UIUtil.getContext()).startRecord();
                performClick();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isCancelled(this, event)) {
                    AudioRecordManager.getInstance(UIUtil.getContext()).willCancelRecord();
                } else {
                    AudioRecordManager.getInstance(UIUtil.getContext()).continueRecord();
                }
                break;
            case MotionEvent.ACTION_UP:
                AudioRecordManager.getInstance(UIUtil.getContext()).stopRecord();
                AudioRecordManager.getInstance(UIUtil.getContext()).destroyRecord();
                break;
        }
        return super.onTouchEvent(event);
    }
    private boolean isCancelled(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        if (event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth() || event.getRawY() < location[1] - 40) {
            return true;
        }
        return false;
    }
    public boolean performClick() {
        return super.performClick();
    }
}
