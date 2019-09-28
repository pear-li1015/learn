package com.communication.coap;

import com.coap.dtlsTest.CoAPMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: bin
 * @Date: 2019/9/26 15:56
 * @Description:
 * 采用单例模式
 */
public class MessageList {
    private static List<CoAPMessage> preSendList = new ArrayList<>();
    private static List<CoAPMessage> preHandList = new ArrayList<>();
    private static List<CoAPMessage> preCallBackList = new ArrayList<>();

    private MessageList() {}

    public static List<CoAPMessage> getPreSendList() {
        return preSendList;
    }
    public static List<CoAPMessage> getPreHandList() {
        return preHandList;
    }
    public static List<CoAPMessage> getPreCallBackList() {
        return preCallBackList;
    }

    // 根据 uuid 发送方 接收方 查找 preCallBackList 中，唯一 Message
    public static CoAPMessage getByUuid(String uuid, String from, String to) {
        while (preCallBackList.iterator().hasNext()) {
            CoAPMessage message = preCallBackList.iterator().next();
            if (message.getUuid().equals(uuid) && (message.getFrom().equals(from) || message.getFrom().equals(to))
                    && (message.getTo().equals(from) || message.getTo().equals(to))) {
                preCallBackList.remove(message);
                return message;
            }
        }
        System.out.println("没有找到 相应的 可回调的内容");
        return null;
    }
}
