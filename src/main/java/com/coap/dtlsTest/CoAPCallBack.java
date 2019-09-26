package com.coap.dtlsTest;

import com.communication.common.CallBack;

/**
 * @Author: bin
 * @Date: 2019/9/25 11:13
 * @Description:
 */
public interface CoAPCallBack extends CallBack {
    public void callback(CoAPMessage response);
}
