//package com.camer.transport.decode;
//
//
//import com.xuggle.mediatool.IMediaListener;
//import com.xuggle.mediatool.IMediaReader;
//import com.xuggle.mediatool.MediaListenerAdapter;
//import com.xuggle.mediatool.ToolFactory;
//import com.xuggle.mediatool.event.*;
//
//import java.awt.image.BufferedImage;
//
///**
// * @Author: bin
// * @Date: 2019/10/11 22:03
// * @Description:
// */
//public class CarmerToBrowser implements IMediaListener {
//
//
//    public static void main(String[] args) {
//        IMediaReader mediaReader = ToolFactory.makeReader("rtsp://211.87.235.74:8554/vlc");
//
//
//        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
//
//
//        mediaReader.addListener(this);
//
//
//        while
//        (mediaReader.readPacket()
//                ==
//                null
//                &&
//                running
//        )
//            ;
//
//
//        mediaReader.close();
//
//    }
//
//
//    @Override
//    public void onVideoPicture(IVideoPictureEvent iVideoPictureEvent) {
//
//    }
//
//    @Override
//    public void onAudioSamples(IAudioSamplesEvent iAudioSamplesEvent) {
//
//    }
//
//    @Override
//    public void onOpen(IOpenEvent iOpenEvent) {
//
//    }
//
//    @Override
//    public void onClose(ICloseEvent iCloseEvent) {
//
//    }
//
//    @Override
//    public void onAddStream(IAddStreamEvent iAddStreamEvent) {
//
//    }
//
//    @Override
//    public void onOpenCoder(IOpenCoderEvent iOpenCoderEvent) {
//
//    }
//
//    @Override
//    public void onCloseCoder(ICloseCoderEvent iCloseCoderEvent) {
//
//    }
//
//    @Override
//    public void onReadPacket(IReadPacketEvent iReadPacketEvent) {
//
//    }
//
//    @Override
//    public void onWritePacket(IWritePacketEvent iWritePacketEvent) {
//
//    }
//
//    @Override
//    public void onWriteHeader(IWriteHeaderEvent iWriteHeaderEvent) {
//
//    }
//
//    @Override
//    public void onFlush(IFlushEvent iFlushEvent) {
//
//    }
//
//    @Override
//    public void onWriteTrailer(IWriteTrailerEvent iWriteTrailerEvent) {
//
//    }
//}
