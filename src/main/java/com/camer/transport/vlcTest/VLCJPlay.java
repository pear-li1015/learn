package com.camer.transport.vlcTest;

import cn.hutool.core.thread.ThreadUtil;
import com.camer.transport.VLCUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * @Author: bin
 * @Date: 2019/10/11 18:50
 * @Description:
 * 尝试将rtsp流转换为图片。
 * 参考文档  https://blog.csdn.net/LLittleF/article/details/88688180
 */
public class VLCJPlay {
    private static BufferedImage image;
    private static MediaPlayerFactory mediaPlayerFactory;
    private static MediaPlayer mediaPlayer;
    //--live-caching 0设置播放器缓存为0，保证获取到的都是实时画面，第二个参数可以不加，暂时没看出啥效果
    static String options[] = new String[]{"--live-caching 0", "--avcodec-hr=vaapi_drm"};
    //这两个参数可加可不加，如果想要通过窗口展示视频画面，就不加， 如果不想显示视频画面，就加上
    static String[] VLC_ARGS = {  "--vout", "dummy" };

//    static String videoSources = "rtsp://admin:123123@192.168.1.112:554/h265/ch0/main/av_stream";
    static String videoSources = VLCUtil.VLC_ADDR;

    public static void main(String[] args) {

        new NativeDiscovery().discover();    //自动搜索libvlc路径并初始化，这行代码一定要加，且libvlc要已经安装，否则会报错
        // 创建播放器工厂
        mediaPlayerFactory = new MediaPlayerFactory();    //这样写的话则不展示视频图像， 要想展示图像的话则直接new MediaPlayerFactory()；
        // 创建一个HeadlessMediaPlayer ，在不需要展示视频画面的情况下，使用HeadlessMediaPlayer 是最合适的（尤其是在服务器环境下）
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
//        mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
        String url = videoSources;
        mediaPlayer.playMedia(url, options);    //开始播放视频，这里传入的是rtsp连接， 传入其他格式的链接也是可以的，网络链接、本地路径都行

        //开始播放之后，可以另起一个线程来获取视频帧 （这里使用的hutool框架来开启线程）
//        ThreadUtil.execAsync((Runnable) () -> {
//            while (true) {
//                if (mediaPlayer.isPlaying()) {
//                    image = mediaPlayer.getSnapshot();
//                    // 具体计算逻辑省略
//                    // 这里只是简单的将图片保存至本地
//                    saveImage(image);
//
//                }
//            }
//        });

    }



    private static void saveImage(BufferedImage bufferedImage) {
        OutputStream out = null;
        try {
            out = new FileOutputStream("D:\\test\\vlc\\vlc" + new Date().getTime() + ".jpg");

            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( out ); //This creates an instance of a JPEGImageEncoder that can be used to encode image data as JPEG Data streams.
            encoder.encode(bufferedImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
