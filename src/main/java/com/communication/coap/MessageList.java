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
}
