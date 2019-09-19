package com.coap.integration.test;

import com.coap.integration.ResourceContent;
import com.coap.integration.ResourceRule;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.io.*;

/**
 * 此demo 根据 传入的 resourceName ，返回文件
 */
public class TestStringResourceRule extends ResourceRule {

    /**
     * 用户重写此方法，用于指定请求结果的返回规则
     * @param resourceName 请求 resource的名称， 用户添加resource时定义的 name
     * @param requestPayload client 请求体中的 payload
     * @param format client携带的payload的类型，类似文件的mine类型  取值参见 {@link MediaTypeRegistry} 类
     * @return 结果的一种封装，包含byte[]类型的payload 和 int类型的format
     *          payload将被放置与响应体的 payload部分
     *
     */
    @Override
    public ResourceContent setRule(String resourceName, byte[] requestPayload, int format) {
        System.out.println("用户根据 资源的名称、client携带的内容、携带内容的格式" +
                "给出相应的返回结果，可以回传文件，也可以返回字符串。 ");
        System.out.println("resourceName: " + resourceName);
        System.out.println("requestPayload: " + new String(requestPayload));
        if (format == MediaTypeRegistry.TEXT_PLAIN) {
            System.out.println("format: TEXT_PLAIN");
        }

        byte[] payload = "payload from server".getBytes();

        return new ResourceContent(payload, MediaTypeRegistry.TEXT_PLAIN);
    }

}
