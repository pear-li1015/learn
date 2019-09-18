package com.coap.integration;

import org.eclipse.californium.core.coap.MediaTypeRegistry;

/**
 * 用户重写 {@link ResourceRule} 类 #{setRule} 方法， 返回值的封装
 */
public class ResourceContent {
    // 用户通信内容的byte数组
    byte[] payload;
    // 指明本次所传内容， 文件的格式信息, client接收后，保存文件的后缀.
    /**
     * format 取值，详见
     * {@link MediaTypeRegistry} 类
     */
    int format;

    public ResourceContent(byte[] payload, int format) {
        this.payload = payload;
        this.format = format;
    }
}
