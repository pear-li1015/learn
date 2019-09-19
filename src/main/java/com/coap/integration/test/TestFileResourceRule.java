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
     * @param format client携带的payload的类型，类似文件的mine类型  取值参见 {@link MediaTypeRegistry} 类
     * @return 结果的一种封装，包含byte[]类型的payload 和 int类型的format
     *          payload将被放置与响应体的 payload部分
     */
    @Override
    public ResourceContent setRule(String resourceName, byte[] requestPayload, int format) {
        try {
            return setRuleImpl(resourceName, requestPayload, format);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResourceContent(new byte[10], MediaTypeRegistry.UNDEFINED);
        }
    }

    private ResourceContent setRuleImpl(String resourceName, byte[] requestPayload, int format) throws Exception {
        // 从byte[]写出到磁盘
        if (format == MediaTypeRegistry.IMAGE_JPEG) {
            System.out.println("输入文件的后缀为 .jpg");
        }
        File outFile = new File("D:\\test\\b.jpg");
        OutputStream outputStream = new FileOutputStream(outFile);
        outputStream.write(requestPayload);
        outputStream.close();
        // 从磁盘 写入到byte[]
        File inFile = new File("D:\\test\\file.jpg");
        byte[] inFileBytes = new byte[(int) inFile.length()];
        // byte[] 只能存放 int， 而inFile.length 是long， 存在越界的危险
        if (inFile.length() == inFileBytes.length) {
            InputStream inputStream = new FileInputStream(inFile);
            inputStream.read(inFileBytes);
            inputStream.close();
        }
        // 返回结果(此次传输的内容 byte[], 此次传输内容的类型 类似mime)
        return new ResourceContent(inFileBytes, MediaTypeRegistry.IMAGE_JPEG);
    }

}
