//package com.coap;
//
//import org.eclipse.californium.core.coap.BlockOption;
//import org.eclipse.californium.core.coap.CoAP;
//import org.eclipse.californium.core.coap.Request;
//import org.eclipse.californium.core.network.Exchange;
//import org.eclipse.californium.core.network.stack.BlockwiseStatus;
//
///**
// * @Author: bin
// * @Date: 2019/9/14 15:35
// * @Description: 参考文档
// * https://www.cnblogs.com/littleatp/p/6417567.html
// */
//public class CoAPServer2 {
//
////    消息大小阈值，当发送的消息大于该阈值时需采用分块传输，该值必须小于MTU；
//    private static final int maxMessageSize = 1024;
//
//    public void sendRequest(final Exchange exchange, final Request request) {
//        BlockOption block2 = request.getOptions().getBlock2();
//        if (block2 != null && block2.getNum() > 0) {
//            //应用层指定的分块..
//        } else if (requiresBlockwise(request)) {
//            //自动计算分块
//            startBlockwiseUpload(exchange, request);
//        } else {
//            //不需要分块
//            exchange.setCurrentRequest(request);
//            lower().sendRequest(exchange, request);
//        }
//    }
//
//    //实现分块阈值判断
//    private boolean requiresBlockwise(final Request request) {
//        boolean blockwiseRequired = false;
//        if (request.getCode() == CoAP.Code.PUT || request.getCode() == CoAP.Code.POST) {
//            blockwiseRequired = request.getPayloadSize() > maxMessageSize;
//        }
//
//        return blockwiseRequired;
//    }
//
//        //startBlockwiseUpload实现了request分块逻辑，通过在请求的Option中加入Block1作为标识
//        private void startBlockwiseUpload(final Exchange exchange, final Request request) {
//            BlockwiseStatus status = findRequestBlockStatus(exchange, request);
//            final Request block = getNextRequestBlock(request, status);
//            block.getOptions().setSize1(request.getPayloadSize());
//
//            lower().sendRequest(exchange, block);
//        }
//
//
//    }
