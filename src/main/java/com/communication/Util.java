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
    public static CoAPMessage transBytesToMessage(byte[] bytes) {
        String jsonStr = new String(bytes);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        CoAPMessage message = new CoAPMessage(jsonObject.getString("to"), jsonObject.getBytes("content"));
        message.setFrom(jsonObject.getString("from"));
        message.setSendTime(jsonObject.getDate("sendTime"));
        message.setUuid(jsonObject.getString("uuid"));
        message.setHaveCallBack(jsonObject.getBooleanValue("haveCallBack"));
        return message;
    }

    public static byte[] transMessageToBytes(CoAPMessage message) {
        String jsonStr = JSON.toJSONString(message);
//        JSONObject jsonObject =
        return jsonStr.getBytes();
    }

    public static void main(String[] args) {
        CoAPMessage message = new CoAPMessage("to", "content".getBytes());
        message.send();

        System.out.println(message);
        byte[] bytes = transMessageToBytes(message);

        CoAPMessage message1 = transBytesToMessage(bytes);
        System.out.println(message1);


    }
}
