package com.communication.common;

import com.coap.dtlsTest.CoAPCallBack;
import com.communication.ConstUtil;

import java.util.Arrays;
import java.util.Date;

/**
 * @Author: bin
 * @Date: 2019/9/26 14:21
 * @Description:
 */
public abstract class Message {
    // 此消息的协议
    private byte protocol;
    // 消息类型的几种可能
    private byte state;
    // 消息的发送方
    private String from;
    // 消息的接收方
    private String to;
    // 消息体
    private byte[] content;
    // 消息的发送时间
    private Date sendTime;
    // 消息的唯一标识， 用户回调函数的跟踪。
    private String uuid;
    // 消息返回后的回调函数
    private CallBack callBack;
    // 标识此Message是否有回调函数
    // 因为Client在send时，不会将 CoAPCallBack带到Server，需要告诉Server,
    // 在将此Message返回来的时候是否有回调函数在列表里等待。
//    private boolean haveCallBack = false;

    Message() {
    }

    public Message(String to, byte[] content) {
        this.to = to;
        this.content = content;
        // 告诉对方， 没有回调 。当然 ，如果是设备，应声明是否需要 server进行响应。
        this.state = ConstUtil.MESSAGE_NO_CALLBACK;
    }

    public Message(String to, byte[] content, CallBack callback) {
        this.to = to;
        this.content = content;
        this.callBack = callback;
        // 告诉对方， 我有回调
        this.state = ConstUtil.MESSAGE_HIS_CALLBACK;
    }

    public abstract void send();

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public byte getProtocol() {
        return protocol;
    }

    public void setProtocol(byte protocol) {
        this.protocol = protocol;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

//    public boolean isHaveCallBack() {
//        return haveCallBack;
//    }
//
//    public void setHaveCallBack(boolean haveCallBack) {
//        this.haveCallBack = haveCallBack;
//    }

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
//                ", content=" + Arrays.toString(content) +
                ", sendTime=" + sendTime +
                ", uuid='" + uuid + '\'' +
                ", callBack=" + callBack +
                ", protocol=" + protocol +
                ", state=" + state +
                '}';
    }
}
