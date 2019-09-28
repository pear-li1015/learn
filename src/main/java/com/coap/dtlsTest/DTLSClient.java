package com.coap.dtlsTest;

/**
 * @Author: bin
 * @Date: 2019/9/20 9:38
 * @Description:
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.communication.Util;
import com.communication.coap.MessageList;
import org.eclipse.californium.elements.*;
import org.eclipse.californium.elements.util.DaemonThreadFactory;
import org.eclipse.californium.elements.util.SslContextUtil;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.CertificateType;
import org.eclipse.californium.scandium.dtls.pskstore.StaticPskStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DTLSClient {

    private static final int DEFAULT_PORT = 5684;
    private static final long DEFAULT_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(10000);
    private static final Logger LOG = LoggerFactory.getLogger(com.coap.dtlsTest.DTLSClient.class.getName());
    private static final char[] KEY_STORE_PASSWORD = "endPass".toCharArray();
    private static final String KEY_STORE_LOCATION = "certs/keyStore.jks";
    private static final char[] TRUST_STORE_PASSWORD = "rootPass".toCharArray();
    private static final String TRUST_STORE_LOCATION = "certs/trustStore.jks";

    // 这是一个线程安全的计数器
    private static CountDownLatch messageCounter;

    private static String payload = "HELLO WORLD";

    private DTLSConnector dtlsConnector;
    private AtomicInteger clientMessageCounter = new AtomicInteger();

    public DTLSClient() {
        try {
            // load key store
            SslContextUtil.Credentials clientCredentials = SslContextUtil.loadCredentials(
                    SslContextUtil.CLASSPATH_SCHEME + KEY_STORE_LOCATION, "client", KEY_STORE_PASSWORD,
                    KEY_STORE_PASSWORD);
            Certificate[] trustedCertificates = SslContextUtil.loadTrustedCertificates(
                    SslContextUtil.CLASSPATH_SCHEME + TRUST_STORE_LOCATION, "root", TRUST_STORE_PASSWORD);

            DtlsConnectorConfig.Builder builder = new DtlsConnectorConfig.Builder();
            builder.setPskStore(new StaticPskStore("Client_identity", "secretPSK".getBytes()));
            builder.setIdentity(clientCredentials.getPrivateKey(), clientCredentials.getCertificateChain(),
                    CertificateType.RAW_PUBLIC_KEY, CertificateType.X_509);
            builder.setTrustStore(trustedCertificates);
            builder.setRpkTrustAll();
            builder.setConnectionThreadCount(1);
            dtlsConnector = new DTLSConnector(builder.build());
            dtlsConnector.setRawDataReceiver(new RawDataChannel() {

                @Override
                public void receiveData(RawData raw) {
                    System.out.println("receive data " + new String(raw.getBytes()));
                    if (dtlsConnector.isRunning()) {
                        receive(raw);
                    }
                }
            });

        } catch (GeneralSecurityException | IOException e) {
            LOG.error("Could not load the keystore", e);
        }
    }

    private void receive(RawData raw) {

        messageCounter.countDown();
        long c = messageCounter.getCount();
        if (LOG.isInfoEnabled()) {
            LOG.info("Received response: {} {}", new Object[] { new String(raw.getBytes()), c });
        }
        if (0 < c) {
            clientMessageCounter.incrementAndGet();
            try {
                RawData data = RawData.outbound((payload + c + ".").getBytes(), raw.getEndpointContext(), new MessageCallback() {
                    @Override
                    public void onConnecting() {

                        System.out.println("-----receive-------onConnecting-------------");
                    }

                    @Override
                    public void onDtlsRetransmission(int flight) {

                        System.out.println("---receive---------onDtlsRetransmission-------------");
                    }

                    @Override
                    public void onContextEstablished(EndpointContext context) {
                        System.out.println("------receive------onContextEstablished-------------");
                    }

                    @Override
                    public void onSent() {

                        System.out.println("-------receive-----onSent-------------");
                    }

                    @Override
                    public void onError(Throwable error) {

                        System.out.println("----receive--------onError-------------");
                    }
                }, false);
                dtlsConnector.send(data);
            } catch (IllegalStateException e) {
                LOG.debug("send failed after {} messages", (c - 1), e);
            }
        } else {
            dtlsConnector.destroy();
        }

    }

    private void start() {
        try {
            dtlsConnector.start();

        } catch (IOException e) {
            LOG.error("Cannot start connector", e);
        }
    }

    private void startTest(InetSocketAddress peer) {
        CoAPMessage message = new CoAPMessage("to", "content".getBytes());
        message.send();

        RawData data = RawData.outbound(Util.transMessageToBytes(message), new AddressEndpointContext(peer), new MessageCallback() {
            @Override
            public void onConnecting() {

                System.out.println("-----startTest-------onConnecting-------------");
            }

            @Override
            public void onDtlsRetransmission(int flight) {

                System.out.println("-----startTest-------onDtlsRetransmission-------------");
            }

            @Override
            public void onContextEstablished(EndpointContext context) {
                System.out.println("------startTest------onContextEstablished-------------");
            }

            @Override
            public void onSent() {

                System.out.println("-------startTest-----onSent-------------");
            }

            @Override
            public void onError(Throwable error) {

                System.out.println("-----startTest-------onError-------------");
            }
        }, false);
        dtlsConnector.send(data);
    }
    private void sendMessage(InetSocketAddress peer) {
//
//        CoAPMessage message = new CoAPMessage("to", "content".getBytes());
//        message.send();
        List<CoAPMessage> messageList = MessageList.getPreHandList();
        if (messageList.size() > 0) {
            System.out.println("client send a message, 当前 list 的长度为： " + messageList.size());
            CoAPMessage message = messageList.remove(0);
            RawData data = RawData.outbound(Util.transMessageToBytes(message), new AddressEndpointContext(peer), null, false);
            dtlsConnector.send(data);
        }
    }
    private int stop() {
        if (dtlsConnector.isRunning()) {
            dtlsConnector.destroy();
        }
        return clientMessageCounter.get();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Math.round((Math.random() * 100) % 6));
        int clients = 1;
        int messages = 1;
        int maxMessages = (messages * clients);
        messageCounter = new CountDownLatch(maxMessages);

        System.out.println("Create " + clients + " DTLS example clients, expect to send " + maxMessages +" messages overall.");



        final com.coap.dtlsTest.DTLSClient client = new com.coap.dtlsTest.DTLSClient();
        client.start();
        System.out.println(clients + " DTLS example clients started.");

        // Get peer address
        InetSocketAddress peer;
//        if (args.length == 5) {
//            peer = new InetSocketAddress(args[3], Integer.parseInt(args[4]));
//        } else {
//            peer = new InetSocketAddress(InetAddress.getLoopbackAddress(), DEFAULT_PORT);
//            peer = new InetSocketAddress("192.168.1.145", DEFAULT_PORT);
            peer = new InetSocketAddress("localhost", DEFAULT_PORT);
        System.out.println(InetAddress.getLoopbackAddress().getHostAddress() + DEFAULT_PORT);
//        }

        // Start Test
        long lastMessageCountDown = messageCounter.getCount();

        client.startTest(peer);
        // Wait with timeout or all messages send.
        // 这里实际上是在等待别的线程完成通信并打印log,
        // 如果 sleep 也可以
        while (!messageCounter.await(DEFAULT_TIMEOUT_NANOS, TimeUnit.NANOSECONDS)) {
            long current = messageCounter.getCount();
            if (lastMessageCountDown == current && current < maxMessages) {
                // no new messages, clients are stale
                // adjust start time with timeout
                break;
            }
            lastMessageCountDown = current;
        }

        Thread.sleep(5000);
        System.out.println(clients + " DTLS example clients finished.");

//        int statistic[] = new int[clients];
//        for (int index = 0; index < clients; ++index) {
//            statistic[index] = client.stop();
//        }
        while (true) {
            System.out.println("当前list的长度为" + MessageList.getPreHandList().size());
            if (MessageList.getPreHandList().size() > 0) {
                client.sendMessage(peer);
            } else {
                Thread.sleep(1000);
            }
            Thread.sleep(1000);
        }
    }
}
