package com.coap.integration.test;

import com.coap.integration.ResourceContent;
import com.coap.integration.ResourceRule;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Response;

import java.io.*;

/**
 * 此demo 根据 传入的 resourceName ，返回文件
 */
public class TestFileResourceRule extends ResourceRule {

    /**
     * 用户重写此方法，用于指定请求结果的返回规则
     * @param resourceName 请求 resource的名称， 用户添加resource时定义的 name
     * @param requestPayload client 请求体中的 payload
     * @param format requestContentType
     * @return 结果的一种封装，包含byte[]类型的payload 和 int类型的format
     *          payload将被放置与响应体的 payload部分
     *          format类似文件的mine类型  取值参见 {@link MediaTypeRegistry} 类
     */
    // TODO 添加请求的 mime类型
    @Override
    public ResourceContent setRule(String resourceName, byte[] requestPayload, int format) {
        File file = new File("D:\\test\\file.jpg");
        byte[] payload = transFileToBytes(file);
        return new ResourceContent(payload, MediaTypeRegistry.IMAGE_JPEG);
//        return new ResourceContent(payload, MediaTypeRegistry.APPLICATION_OCTET_STREAM);
    }

    private byte[] transFileToBytes(File file) {
        InputStream in = null;
        byte[] content = new byte[(int) file.length()];
        try {
            in = new FileInputStream(file);
            int r = in.read(content);
            // 这里 r 应该 等于 file.length()
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return content;
        }

    }
}
