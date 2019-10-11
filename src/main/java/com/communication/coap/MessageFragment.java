package com.communication.coap;

import java.util.Date;

/**
 * @Author: bin
 * @Date: 2019/10/8 15:02
 * @Description:
 * 最好使用滑动窗口协议
 */

public class MessageFragment {

    // 消息的接收方
    private String to;
    // 消息的发送方
    private String from;
    // 消息的发送时间
    private Date sendTime;
    // 请求的唯一标识， 用户回调函数的跟踪。
    private String uuid;
    // 当前帧的
    private int currentFrame;
    // 总帧数
    private int totalFrame;
    // 消息体的长度
    private int bodyLength;
    // 消息体
    private byte[] body;


    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public int getTotalFrame() {
        return totalFrame;
    }

    public void setTotalFrame(int totalFrame) {
        this.totalFrame = totalFrame;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
