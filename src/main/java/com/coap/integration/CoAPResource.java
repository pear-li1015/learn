package com.coap.integration;

import com.coap.MyResourceRule;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.network.Exchange;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.network.config.NetworkConfigDefaultHandler;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.io.File;


public class CoAPResource extends CoapResource {


    private static final File CONFIG_FILE = new File("Californium.properties");
    private static final String CONFIG_HEADER = "Californium CoAP Properties file for Fileserver";
    private static final int DEFAULT_MAX_RESOURCE_SIZE = 2 * 1024 * 1024; // 2 MB
    private static final int DEFAULT_BLOCK_SIZE = 512;

    private static NetworkConfigDefaultHandler DEFAULTS = new NetworkConfigDefaultHandler() {

        @Override
        public void applyDefaults(NetworkConfig config) {
            config.setInt(NetworkConfig.Keys.MAX_RESOURCE_BODY_SIZE, DEFAULT_MAX_RESOURCE_SIZE);
            config.setInt(NetworkConfig.Keys.MAX_MESSAGE_SIZE, DEFAULT_BLOCK_SIZE);
            config.setInt(NetworkConfig.Keys.PREFERRED_BLOCK_SIZE, DEFAULT_BLOCK_SIZE);
        }
    };

    private final NetworkConfig config = NetworkConfig.createWithFile(CONFIG_FILE, CONFIG_HEADER, DEFAULTS);
    private ResourceRule rule;
    private CoAPResource(String name) {
        super(name);
    }

    private CoAPResource(String name, boolean visible) {
        super(name, visible);
    }


    public CoAPResource(String name, ResourceRule rule) {
        super(name);
        this.rule = rule;
    }

    @Override
    public final void handleGET(CoapExchange exchange) {
        System.out.println("handle get");
        Request request = exchange.advanced().getRequest();


//        String payload = bytesToString(request.getPayload());
        byte[] payload = request.getPayload();
        // client发送的内容的格式
        int format = request.getOptions().getContentFormat();
        // 将 payload 从此传入。
        Response response = rule.getResponse(this.getName(), payload, format);
        // 检查配置文件中限制的 单次文件传输的最大值
        long maxLength = Long.parseLong(NetworkConfig.Keys.MAX_RESOURCE_BODY_SIZE);
        if (response.getPayload().length > maxLength) {
            System.out.println("file " + " is too large "  + " max: " + maxLength);
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
            return;
        }
        exchange.respond(response);
    }

    @Override
    public final void handleRequest(Exchange exchange) {
        System.out.println("handle request");
        // 从路由器中拿到 请求体
        Request request = exchange.getRequest();

        // 从请求体中拿到 payload
//        String payload = request.getPayloadString();
        byte[] payload = request.getPayload();
        // client发送的内容的格式
        int format = request.getOptions().getContentFormat();
        // 根据用户配置的规则，得到响应内容
        Response response = rule.getResponse(this.getName(), payload, format);

        // 检查配置文件中限制的 单次文件传输的最大值
        int maxLength = config.getInt(NetworkConfig.Keys.MAX_RESOURCE_BODY_SIZE);
        // 如果响应的长度 超出了单次传输的限制
        if (response.getPayload().length > maxLength) {
            System.out.println("file " + " is too large " + response.getPayload().length  + " max: " + maxLength);
            // 发送错误响应
            exchange.sendResponse(new Response(CoAP.ResponseCode.INTERNAL_SERVER_ERROR));
            return;
        }
        // 发送响应
        exchange.sendResponse(response);
    }

    String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        } else {
            return bytes.toString();
        }
    }
}
