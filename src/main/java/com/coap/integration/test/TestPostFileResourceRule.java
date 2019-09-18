package com.coap.integration.test;

import com.coap.integration.ResourceContent;
import com.coap.integration.ResourceRule;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.io.*;

/**
 * 此demo 根据 传入的 resourceName ，返回文件
 */
public class TestPostFileResourceRule extends ResourceRule {

    /**
     * 用户重写此方法，用于指定请求结果的返回规则
     * @param resourceName 请求 resource的名称， 用户添加resource时定义的 name
     * @param requestPayload client 请求体中的 payload
     * @return 结果的一种封装，包含byte[]类型的payload 和 int类型的format
     *          payload将被放置与响应体的 payload部分
     *          format类似文件的mine类型  取值参见 {@link MediaTypeRegistry} 类
     */
    @Override
    public ResourceContent setRule(String resourceName, byte[] requestPayload) {
        System.out.println("resourceName: " + resourceName);
//        System.out.println("requestPayload: " + requestPayload);

//        File file = new File("D:\\test\\postFile.txt");
        File file = new File("D:\\test\\postFile.mp4");
        try {
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(requestPayload);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] payload = "transFileToBytes(file)".getBytes();
        return new ResourceContent(payload, MediaTypeRegistry.TEXT_PLAIN);
    }

}
