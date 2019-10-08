package com.communication;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.coap.dtlsTest.CoAPMessage;
import com.communication.common.Message;

import java.util.Date;

/**
 * @Author: bin
 * @Date: 2019/9/26 16:22
 * @Description:
 */
public class Util {
    public static final int MAX_PLAINTEXT_FRAGMENT_LENGTH = 16384; // max. DTLSPlaintext.length (2^14 bytes)

    public static final int PROTOCOL_LENGTH = 1;
    public static int CALLBACK_STATE_LENGTH = 1;
    public static int DEVICE_ID_LENGTH = 12;
    public static int SEND_TIME_LENGTH = 8;
    public static int UUID_LENGTH = 32;
    public static int CURRENT_FRAME_LENGTH = 4;
    public static int TOTOAL_FRAME_LENGTH = 4;
    public static final int MAX_SEND_LENGTH = MAX_PLAINTEXT_FRAGMENT_LENGTH - PROTOCOL_LENGTH
            - CALLBACK_STATE_LENGTH - DEVICE_ID_LENGTH * 2 - SEND_TIME_LENGTH - UUID_LENGTH
            - CURRENT_FRAME_LENGTH - TOTOAL_FRAME_LENGTH; // max. DTLSPlaintext.length (2^14 bytes)

    /**
     * 将byte数组转换为 CoAPMessage
     * @param bytes
     * @return
     */
    public static CoAPMessage transBytesToMessage(byte[] bytes) {
        byte[] to = new byte[DEVICE_ID_LENGTH];
        byte[] from = new byte[DEVICE_ID_LENGTH];
        byte[] sendTime = new byte[SEND_TIME_LENGTH];

        byte[] uuid = new byte[UUID_LENGTH];
        byte[] currentFrame = new byte[CURRENT_FRAME_LENGTH];
        byte[] totalFrame = new byte[TOTOAL_FRAME_LENGTH];
        byte[] body = new byte[MAX_SEND_LENGTH];
        System.arraycopy(bytes, 2, to, 0, DEVICE_ID_LENGTH);
        System.arraycopy(bytes, 14, from, 0, DEVICE_ID_LENGTH);
        System.arraycopy(bytes, 26, sendTime, 0, SEND_TIME_LENGTH);
        System.arraycopy(bytes, 34, uuid, 0, UUID_LENGTH);
        System.arraycopy(bytes, 66, currentFrame, 0, CURRENT_FRAME_LENGTH);
        System.arraycopy(bytes, 70, totalFrame, 0, TOTOAL_FRAME_LENGTH);
        // 从74 开始，是消息体

        System.arraycopy(bytes, 74, body, 0, MAX_SEND_LENGTH);

        CoAPMessage message = new CoAPMessage(new String(to), body);
        message.setFrom(new String(from));

        message.setSendTime(new Date(transBytesToLong(sendTime)));
        message.setUuid(new String(uuid));
        message.setState(bytes[1]);
        message.setProtocol(bytes[0]);
        message.setTotalFrame(transBytesToInt(totalFrame));
        message.setCurrentFrame(transBytesToInt(currentFrame));
        return message;
    }

    /**
     * 将 CoAPMessage 转换为 byte数组
     * @param message
     * @return
     */
    public static byte[] transMessageToBytes(CoAPMessage message) {
        byte[] to = message.getTo().getBytes();
        byte[] from = message.getFrom().getBytes();
        byte[] sendTime = transLongToBytes(message.getSendTime().getTime());
        byte[] uuid = message.getUuid().getBytes();
        byte[] currentFrame = transIntToBytes(message.getCurrentFrame());
        byte[] totalFrame = transIntToBytes(message.getTotalFrame());
        byte[] body = message.getContent();
        byte[] result = new byte[MAX_PLAINTEXT_FRAGMENT_LENGTH];
        result[0] = message.getProtocol();
        result[1] = message.getState();
        System.arraycopy(to, 0, result, 2, DEVICE_ID_LENGTH);
        System.arraycopy(from, 0, result, 14, DEVICE_ID_LENGTH);
        System.arraycopy(sendTime, 0, result, 26, SEND_TIME_LENGTH);
        System.arraycopy(uuid, 0, result, 34, UUID_LENGTH);
        System.arraycopy(currentFrame, 0, result, 66, CURRENT_FRAME_LENGTH);
        System.arraycopy(totalFrame, 0, result, 70, TOTOAL_FRAME_LENGTH);
        // 从74 开始，是消息体
        System.out.println("MAX_SEND_LENGTH: " + MAX_SEND_LENGTH);
        System.out.println("MAX_PLAINTEXT_FRAGMENT_LENGTH: " + MAX_PLAINTEXT_FRAGMENT_LENGTH);
        System.out.println("message.getContent().length: " + message.getContent().length);
        System.arraycopy(body, 0, result, 74, message.getContent().length);

        return result;
    }

    /**
     * 暂时弃用
     * @param bytes
     * @return
     */
    public static CoAPMessage transJsonBytesToMessage(byte[] bytes) {
        String jsonStr = new String(bytes);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        CoAPMessage message = new CoAPMessage(jsonObject.getString("to"), jsonObject.getBytes("content"));
        message.setFrom(jsonObject.getString("from"));
        message.setSendTime(jsonObject.getDate("sendTime"));
        message.setUuid(jsonObject.getString("uuid"));
        message.setState(jsonObject.getByte("state"));
        message.setProtocol(jsonObject.getByte("protocol"));
        message.setTotalFrame(jsonObject.getInteger("totalFrame"));
        message.setCurrentFrame(jsonObject.getInteger("currentFrame"));
        return message;
    }

    /**
     * 暂时弃用
     * @param message
     * @return
     */
    public static byte[] transMessageToJsonBytes(CoAPMessage message) {
        String jsonStr = JSON.toJSONString(message);
//        JSONObject jsonObject =
        return jsonStr.getBytes();
    }


    /**
     * 将long保存为8byte
     * @param values
     * @return
     */
    public static byte[] transLongToBytes(long values) {
        byte[] buffer = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = 64 - (i + 1) * 8;
            buffer[i] = (byte) ((values >> offset) & 0xff);
        }
        return buffer;
    }

    /**
     * 将8byte转换为long
     * @param buffer
     * @return
     */
    public static long transBytesToLong(byte[] buffer) {
        long  values = 0;
        for (int i = 0; i < 8; i++) {
            values <<= 8; values|= (buffer[i] & 0xff);
        }
        return values;
    }

    /**
     * 将int转为4byte
     * @param num
     * @return
     */
    public static byte[] transIntToBytes(int num){
        byte[]bytes=new byte[4];
        bytes[0]=(byte) ((num>>24)&0xff);
        bytes[1]=(byte) ((num>>16)&0xff);
        bytes[2]=(byte) ((num>>8)&0xff);
        bytes[3]=(byte) (num&0xff);
        return bytes;
    }

    /**
     * 将4byte转换为int
     * @param bytes
     * @return
     */
    public static int transBytesToInt(byte[] bytes) {
        return (bytes[0]&0xff)<<24
                | (bytes[1]&0xff)<<16
                | (bytes[2]&0xff)<<8
                | (bytes[3]&0xff);
    }
}
