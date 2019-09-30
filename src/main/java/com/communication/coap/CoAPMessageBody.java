package com.communication.coap;

/**
 * @Author: bin
 * @Date: 2019/9/30 13:44
 * @Description:
 */
public class CoAPMessageBody {
    byte[] body;

    public CoAPMessageBody(byte[] body) {
        this.body = body;
        byte[] test = new byte[5];
        body = new byte[]{1,1,1,1, };

    }
}
