package com.kalac.etalk.Adapter;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kalac.etalk.R;
import com.kalac.etalk.Utils.UIUtil;
import com.lqr.audio.AudioPlayManager;
import com.lqr.audio.IAudioPlayListener;

import java.util.ArrayList;

import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

public class ChatAdapter extends BaseAdapter {
    ArrayList<Message> mMessageArray;
    private static final int TYPE_TEXT = 0;
    private static final int TYPE_VOICE = 1;
    public ChatAdapter(ArrayList<Message> mMessageArray) {
        this.mMessageArray = mMessageArray;
    }

    private static final String TAG = "ChatAdapter";
    @Override
    public int getCount() {
        return mMessageArray.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessageArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        MessageContent messageContent = mMessageArray.get(position).getContent();
        if (messageContent instanceof TextMessage) {
            return TYPE_TEXT;
        } else if (messageContent instanceof VoiceMessage){
            return TYPE_VOICE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextViewHolder textViewHolder = null;
        VoiceViewHolder voiceViewHolder = null;
        final MessageContent messageContent = mMessageArray.get(position).getContent();
        switch (getItemViewType(position)){
            case TYPE_TEXT:
                textViewHolder = new TextViewHolder();
                if(convertView == null){
                    convertView = UIUtil.inflate(R.layout.item_textmessage);
                    textViewHolder.tvChatText = convertView.findViewById(R.id.tv_speech_chat_text);
                    //setTag()
                    convertView.setTag(textViewHolder);
                }else{
                    //getTag();
                    textViewHolder = (TextViewHolder) convertView.getTag();
                }

                final String textMessage = ((TextMessage) messageContent).getContent();
                textViewHolder.tvChatText.setText(textMessage);
                break;
            case TYPE_VOICE:
                voiceViewHolder = new VoiceViewHolder();
                if(convertView == null){
                    convertView = UIUtil.inflate(R.layout.item_voicemessage);
                    voiceViewHolder.ivVoice = (ImageView) convertView.findViewById(R.id.iv_voice);
                    convertView.setTag(voiceViewHolder);
                }else{
                    voiceViewHolder = (VoiceViewHolder) convertView.getTag();
                }
                final VoiceMessage voiceMessage = (VoiceMessage) messageContent;
                int duration = voiceMessage.getDuration();
                voiceViewHolder.tvDuration.setText(duration);
                voiceViewHolder.ivVoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Uri uri = voiceMessage.getUri();
                        AudioPlayManager.getInstance().stopPlay();
                        AudioPlayManager.getInstance().startPlay(UIUtil.getContext(), uri, new IAudioPlayListener() {
                            @Override
                            public void onStart(Uri var1) {
                                Log.i(TAG, "onStart: 开始播放");
                            }

                            @Override
                            public void onStop(Uri var1) {
                                Log.i(TAG, "onStart: 停止播放");
                            }

                            @Override
                            public void onComplete(Uri var1) {
                                Log.i(TAG, "onStart: 完成播放");
                            }
                        });
                    }
                });
                break;
        }
        return convertView;
    }
    static class TextViewHolder{
        TextView tvChatText;
    }

    static class VoiceViewHolder{
        ImageView ivVoice;
        TextView tvDuration;
    }
}
