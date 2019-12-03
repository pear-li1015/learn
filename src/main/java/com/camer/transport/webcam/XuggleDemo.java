package com.camer.transport.webcam;

import java.io.File;

/**
 * @Author: bin
 * @Date: 2019/10/12 14:07
 * @Description:
 * https://blog.csdn.net/FeiXuanZeTi/article/details/50116883
 */
public class XuggleDemo {

    public static void main(String[] args) {
        doCaptureWebCamFrame();
    }

    public static File doCaptureWebCamFrame() {
        return WebCamDialog.getInstance().init();
    }
}
