package com.camer;

import com.codeminders.hidapi.HIDDeviceInfo;
import com.codeminders.hidapi.HIDManager;

import javax.usb.*;
import java.util.List;

/**
 * @Author: bin
 * @Date: 2019/12/3 9:40
 * @Description:
 */
public class UsbTest {

    private static short idVendor = (short) 0x045E;
    private static short idProduct = (short) 0x028E;

    public static void main(String[] args) {
//        test1();
        test2();
    }

    public static void test3() {

        List list;
        try {
            list = UsbHostManager.getUsbServices()
                    .getRootUsbHub().getAttachedUsbDevices();
            int a = 10;
            int b = 15;

        } catch (UsbException e) {
            e.printStackTrace();
        }
    }
    public static void test2(){
        try {

            com.codeminders.hidapi.ClassPathLibraryLoader.loadNativeHIDLibrary();
            HIDManager hidManager = HIDManager.getInstance();
            HIDDeviceInfo[] infos = hidManager.listDevices();
            for (HIDDeviceInfo info : infos) {
                System.out.println("info: " + info.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test1() {

        try {
            UsbPipe sendUsbPipe = new UsbTest().useUsb();

            if (sendUsbPipe != null) {
                byte[] buff = new byte[64];
                for (int i = 0; i < 9; i++) {
                    buff[i] = (byte) i;
                    sendMassge(sendUsbPipe, buff);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public UsbPipe useUsb() throws Exception {
        UsbInterface iface = linkDevice();
        if (iface == null) {
            return null;
        }
        UsbEndpoint receivedUsbEndpoint, sendUsbEndpoint;

        sendUsbEndpoint = (UsbEndpoint) iface.getUsbEndpoints().get(0);
        if (!sendUsbEndpoint.getUsbEndpointDescriptor().toString().contains("OUT")) {
            receivedUsbEndpoint = sendUsbEndpoint;
            sendUsbEndpoint = (UsbEndpoint) iface.getUsbEndpoints().get(1);
        } else {
            receivedUsbEndpoint = (UsbEndpoint) iface.getUsbEndpoints().get(1);
        }

        //发送：
        UsbPipe sendUsbPipe = sendUsbEndpoint.getUsbPipe();
        sendUsbPipe.open();

        //接收
        final UsbPipe receivedUsbPipe = receivedUsbEndpoint.getUsbPipe();
        receivedUsbPipe.open();

        new Thread(new Runnable() {
            public void run() {
                try {
                    receivedMassge(receivedUsbPipe);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return sendUsbPipe;
    }

    public UsbInterface linkDevice() throws Exception {

        UsbDevice device = null;
        if (device == null) {
            device = findDevice(UsbHostManager.getUsbServices()
                    .getRootUsbHub());
        }
        if (device == null) {
            System.out.println("设备未找到！");
            return null;
        }
        UsbConfiguration configuration = device.getActiveUsbConfiguration();
        UsbInterface iface = null;
        if (configuration.getUsbInterfaces().size() > 0) {
            iface = configuration.getUsbInterface((byte) 1);
        } else {
            return null;
        }
        iface.claim(new UsbInterfacePolicy() {
            @Override
            public boolean forceClaim(UsbInterface usbInterface) {
                return true;
            }
        });
        return iface;
    }

    public void receivedMassge(UsbPipe usbPipe) throws Exception {
        byte[] b = new byte[64];
        int length = 0;
        while (true) {
            length = usbPipe.syncSubmit(b);//阻塞
            System.out.println("接收长度：" + length);
            for (int i = 0; i < length; i++) {
                System.out.print(Byte.toUnsignedInt(b[i]) + " ");
            }
        }
    }

    public static void sendMassge(UsbPipe usbPipe, byte[] buff) throws Exception {
        usbPipe.syncSubmit(buff);//阻塞
        //usbPipe.asyncSubmit(buff);//非阻塞
    }

    List findAllDevice(UsbHub hub) {
        return hub.getAttachedUsbDevices();
    }

    public UsbDevice findDevice(UsbHub hub) {
        UsbDevice device = null;
        List list = (List) hub.getAttachedUsbDevices();
        for (int i = 0; i < list.size(); i++) {
            device = (UsbDevice) list.get(i);
            UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
            System.out.println(i + "___" + desc.idVendor() + "___" + desc.idProduct());
            if (desc.idVendor() == idVendor && desc.idProduct() == idProduct) {
                return device;
            }
            if (device.isUsbHub()) {
                device = findDevice((UsbHub) device);
                if (device != null) return device;
            }
        }
        return null;
    }
}
