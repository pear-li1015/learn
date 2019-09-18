package com.coap.integration;

import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Response;

import java.io.File;

/**
 *
 */
public abstract class ResourceRule {

    /**
     * 根据 resource 名称， 指定规则，生成byte[] ，并将 返回码、内容、等信息装入 response
     * @param resourceName
     * @return
     */
    public abstract ResourceContent setRule(String resourceName, String requestPayload);

    /**
     * 通过 为 resource指定的名称， 根据重写的 规则，返回Response，准备发往 client
     * 用户不需要重写此方法
     * @param resourceName
     * @param requestPayload
     * @return
     */
    public Response getResponse(String resourceName, String requestPayload) {
        // 调用用户定义的规则，返回传输内容和类型
        ResourceContent content = setRule(resourceName, requestPayload);
        // 创建响应
        Response response = new Response(CoAP.ResponseCode.CONTENT);
        // 设置响应的内容
        response.setPayload(content.payload);
        // 设置响应体的长度
        response.getOptions().setSize2(content.payload.length);
        // 设置响应体的类型（类似于mime类型）
        response.getOptions().setContentFormat(content.format);
        return response;
    }



}
