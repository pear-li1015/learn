package com.coap.dtlsTest;

import com.communication.ConstUtil;
import com.communication.coap.MessageList;
import com.communication.common.CallBack;
import com.communication.common.Message;

import java.util.Date;
import java.util.UUID;

/**
 * @Author: bin
 * @Date: 2019/9/25 11:11
 * @Description:
 */
public class CoAPMessage extends Message {
//    // 消息的发送方
//    private String from;
//    // 消息的接收方
//    private String to;
//    // 消息体
//    private byte[] content;
//    // 消息的发送时间
//    private Date sendTime;
//    // 消息的唯一标识， 用户回调函数的跟踪。
//    private String uuid;
//    // 消息返回后的回调函数
//    private CoAPCallBack callBack;
//    // 标识此Message是否有回调函数
//    // 因为Client在send时，不会将 CoAPCallBack带到Server，需要告诉Server,
//    // 在将此Message返回来的时候是否有回调函数在列表里等待。
//    private boolean haveCallBack;

    // 分组接收的返回结果
    private byte[][] response;
    private int currentFrame;
    private int totalFrame;
    // 已保存的帧数
    public int inFrame = 0;


    public void setResponse(int row, byte[] rowContent) {
        this.response[row] = rowContent;
        inFrame ++;
    }
    public void initResponse(int row, int col, int totalFrame) {
        this.response = new byte[row][col];
        this.totalFrame = totalFrame;
    }
    public CoAPMessage deepCopy() {
        CoAPMessage result = new CoAPMessage(this.getTo(), this.getContent());
        result.setCurrentFrame(this.getCurrentFrame());
        byte[] newByte = new byte[this.getContent().length];
        System.arraycopy(this.getContent(), 0, newByte, 0, this.getContent().length);
        result.setContent(newByte);
        result.setUuid(this.getUuid());
        result.setFrom(this.getFrom());
        result.setState(this.getState());
        result.setTotalFrame(this.getTotalFrame());
        result.setSendTime(this.getSendTime());
        return result;
    }

    public CoAPMessage(String to, byte[] content) {
        super(to, content);
        this.setProtocol(ConstUtil.COAP);
//        this.to = to;
//        this.content = content;
    }

    public CoAPMessage(String to, byte[] content, CallBack callback) {
        super(to, content, callback);
        this.setProtocol(ConstUtil.COAP);
//        this.to = to;
//        this.content = content;
//        this.callBack = callback;
    }

    public void send() {
        // 1、设置消息的发送方
        this.setFrom("123456789012");
        // 2、设置消息发送时间
        this.setSendTime(new Date());
        // 3、生成消息的唯一标识
        this.setUuid(getUUID());
        // 4、如果callBack为null， 直接发送
        if (this.getCallBack() != null) {
            System.out.println("coap message .send()");
            System.out.println(this.toString());
            // 存放进列表
            MessageList.getPreSendList().add(this);

        }
        //send
        // 如果callback不为null，发送消息并将此类存放于 待回调列表中。

    }
    // 当接收到消息的时候，
    // 1、判断 是否 haveCallBack
    // 2、如果有，对比 此消息的uuid 与 待回调列表中消息的uuid
    // 如果没有，进入待处理列表。结束。
    // 3、如果找到一致的uuid 校验 from 和 to的正确性，
    // 如果找不到一致的uuid， 考虑 如何处理。（报错）
    // 4、校验通过后，移出待回调列表 并 调用回调函数
    // 如果校验不通过，检查待回调列表中是否还有其他消息的uuid一致。重复执行
    // 5、如果回调列表中没有其他消息的uuid一致， 考虑如何处理 （报错）


    public byte[][] getResponse() {
        return response;
    }

    public void setResponse(byte[][] response) {
        this.response = response;
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

    /**
     * 生成token
     * @return
     */
    // TODO 这个方法最好改成 getNewUUID
    public static String getUUID()
    {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
