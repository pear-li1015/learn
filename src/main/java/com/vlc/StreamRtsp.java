//package com.vlc;
//
///**
// * @Author: bin
// * @Date: 2019/10/14 10:15
// * @Description:
// */
//import com.camer.transport.VLCUtil;
//import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
//import uk.co.caprica.vlcj.player.base.MediaPlayer;
//
// /**
// * An example of how to stream a media file using RTSP.
// * <p>
// * The client specifies an MRL of <code>rtsp://@127.0.0.1:5555/demo</code>
// */
// // TODO 将 视频文件转为 rtsp流
//public class StreamRtsp extends VlcjTest {
//
//    public static void main(String[] args) throws Exception {
////        if(args.length != 1) {
////            System.out.println("Specify a single MRL to stream");
////            System.exit(1);
////        }
//
//        String media = VLCUtil.MP4_PATH;
//        String options = formatRtspStream("127.0.0.1", 5555, "demo");
//
//        System.out.println("Streaming '" + media + "' to '" + options + "'");
//
//        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(args);
//        MediaPlayer mediaPlayer = mediaPlayerFactory.mediaPlayers().newMediaPlayer();
//        mediaPlayer.media().play(media,
//                options,
//                ":no-sout-rtp-sap",
//                ":no-sout-standard-sap",
//                ":sout-all",
//                ":sout-keep"
//        );
//
//        // Don't exit
//        Thread.currentThread().join();
//    }
//
//    private static String formatRtspStream(String serverAddress, int serverPort, String id) {
//        StringBuilder sb = new StringBuilder(60);
//        sb.append(":sout=#rtp{sdp=rtsp://@");
//        sb.append(serverAddress);
//        sb.append(':');
//        sb.append(serverPort);
//        sb.append('/');
//        sb.append(id);
//        sb.append("}");
//        return sb.toString();
//    }
//}
