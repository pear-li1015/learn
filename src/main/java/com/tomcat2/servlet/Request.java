package com.tomcat2.servlet;

import com.sun.org.apache.xpath.internal.operations.String;

import java.io.InputStream;

/**
 * @Author: bin
 * @Date: 2019/9/8 9:51
 * @Description:
 */
public class Request {
    private InputStream inputStream;

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Request(InputStream inputStream) throws Exception {
        this.inputStream = inputStream;
        int count = 0;
        while(count == 0) {
            count = inputStream.available();
        }
        byte[] bytes = new byte[inputStream.available()];
//        extractFileds(new String(bytes));
    }

    private void extractFileds(String content) {

    }


}
