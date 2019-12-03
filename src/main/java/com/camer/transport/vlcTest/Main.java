package com.camer.transport.vlcTest;

import com.camer.transport.VLCUtil;
import com.sun.media.sound.JavaSoundAudioClip;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.swing.*;
import java.io.InputStream;

/**
 * @Author: bin
 * @Date: 2019/10/11 15:28
 * @Description:
 * 使用播放器播放 rtsp资源
 * 启动成功
 * 这里应该可以试试。将rtsp保存为视频文件。
 */
public class Main {

    public static void main(String[] args) throws Exception {
        String file = VLCUtil.VLC_ADDR;
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(file);
        // 这里会导致报错。
//        grabber.setOption("rtsp_transport", "tcp"); // 使用tcp的方式，不然会丢包很严重
        // 一直报错的原因！！！就是因为是 2560 * 1440的太大了。。
        grabber.setImageWidth(960);
        grabber.setImageHeight(540);
        System.out.println("grabber start");
        grabber.start();
        CanvasFrame canvasFrame = new CanvasFrame("sdsaf");
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvasFrame.setAlwaysOnTop(true);
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();       // OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        while (true) {
            Frame frame = grabber.grabImage();
            // 从frame中获取声音并播放。
//            InputStream
//            JavaSoundAudioClip player = new JavaSoundAudioClip();
//            frame.
            // TODO 尝试在这里保存。
            opencv_core.Mat mat = converter.convertToMat(frame);
            canvasFrame.showImage(frame);
        }

    }
}
