package com.kalac.etalk;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.MessageContent;
import io.rong.message.StatusMessage;
import io.rong.push.common.ParcelUtils;
import io.rong.push.common.RLog;

@MessageTag(value = "GG:SSts", flag = MessageTag.STATUS)
public class JoinChatRoomStatusMessage extends StatusMessage {

    private String content;
    private String extra;

    /**
     * 默认构造函数。
     */
    protected JoinChatRoomStatusMessage() {

    }

    /**
     * 构造函数。
     *
     * @param in 初始化传入的 Parcel。
     */
    public JoinChatRoomStatusMessage(Parcel in) {
        setExtra(ParcelUtils.readFromParcel(in));
        setContent(ParcelUtils.readFromParcel(in));

    }

    /**
     * 构造函数。
     *
     * @param data 初始化传入的二进制数据。
     */
    public JoinChatRoomStatusMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            RLog.e("JSONException", e.getMessage());
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("content"))
                setContent(jsonObj.getString("content"));

            if (jsonObj.has("extra"))
                setExtra(jsonObj.getString("extra"));
        } catch (JSONException e) {
            RLog.e("JSONException", e.getMessage());
        }
    }

    /**
     * 构建一个文字消息实例。
     *
     * @return 文字消息实例。
     */
    public static JoinChatRoomStatusMessage obtain(String text) {
        JoinChatRoomStatusMessage model = new JoinChatRoomStatusMessage();
        model.setContent(text);

        return model;
    }

    /**
     * 描述了包含在 Parcelable 对象排列信息中的特殊对象的类型。
     *
     * @return 一个标志位，表明 Parcelable 对象特殊对象类型集合的排列。
     */
    public int describeContents() {
        return 0;
    }

    /**
     * 将本地消息对象序列化为消息数据。
     *
     * @return 消息数据。
     */
    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("content", getContent());

            if (!TextUtils.isEmpty(getExtra()))
                jsonObj.put("extra", getExtra());

        } catch (JSONException e) {
            RLog.e("JSONException", e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 将类的数据写入外部提供的 Parcel 中。
     *
     * @param dest  对象被写入的 Parcel。
     * @param flags 对象如何被写入的附加标志，可能是 0 或 PARCELABLE_WRITE_RETURN_VALUE。
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, getExtra());
        ParcelUtils.writeToParcel(dest, content);
    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Parcelable.Creator<JoinChatRoomStatusMessage> CREATOR = new Parcelable.Creator<JoinChatRoomStatusMessage>() {
        @Override
        public JoinChatRoomStatusMessage createFromParcel(Parcel source) {
            return new JoinChatRoomStatusMessage(source);
        }

        @Override
        public JoinChatRoomStatusMessage[] newArray(int size) {
            return new JoinChatRoomStatusMessage[size];
        }
    };

    /**
     * 获取文字消息的内容。
     *
     * @return 文字消息的内容。
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置文字消息的内容。
     *
     * @param content 文字消息的内容。
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取文字消息的内容。
     *
     * @return 文字消息的内容。
     */
    public String getExtra() {
        return extra;
    }

    /**
     * 设置消息的附加内容。
     *
     * @param extra 消息的附加内容。
     */
    public void setExtra(String extra) {
        this.extra = extra;
    }
}