package com.communication.coap;

import com.communication.Util;

import java.util.Date;

/**
 * @Author: bin
 * @Date: 2019/10/8 15:03
 * @Description:
 * 分块传输 信息的组装器
 */
public class MessageCombiner {
    private static final byte IS_RECIVED = 1;
//    private static final byte IS_NOT_RECIVED = 0;

    // 分块传输的信息素组。 response[0][0] 用于存放response[0]是否已接收数据；
    // response[0][1] 用于存放response[0]的结束位置。
    private byte[][] response;
    // 开始接收时间，也就是创建时间
    private Date createTime;

    // 上一次接收时间
    private Date updateTime;
    // 接收到的分组数
    private int recivedFragmentNum;
    // fragment总数
    private int totalFragmentNum;
    // 此message的总长度
    private int totalLength;

    public void initResponse(int totalFragmentNum, int dataLength) {
        this.totalFragmentNum = totalFragmentNum;
        this.recivedFragmentNum = 0;
        this.createTime = new Date();
        // 这里加5 是因为 response[0][0] 和 response[0][1]-[4] 存放了特殊数据
        this.response = new byte[totalFragmentNum][Util.MAX_SEND_LENGTH + 5];
    }

    /**
     * 所有数据接收完毕后，对数据进行合并
     * @return
     */
    public byte[] getAllData() {
        return new byte[1];
    }

    /**
     * 判断此数据在接收过程中是否超时，以便请求重发
     * @return
     */
    public boolean isOverTime() {
        return true;
    }

    /**
     * 获取所有未收到的数据包的编号
     */
    public int[] getUnRecivedNum() {

        return new int[1];
    }

    /**
     * 保存一个数据包, 如果之前保存过， 则丢弃新的
     * @param fragment
     */
    public void reciveData(MessageFragment fragment) {
        // 帧的长度
        int length = fragment.getBodyLength();
        // 帧的编号
        int frameNum = fragment.getCurrentFrame();
        // 如果当前帧已接收
        if (response[frameNum][0] == IS_RECIVED) {
            return;
        }
        // 设置当前帧为已接收
        response[frameNum][0] = IS_RECIVED;
        // 将帧的长度转为 bytes
        byte[] byteLength = Util.transIntToBytes(length);
        // 将bytes格式的 帧长度保存至response
        System.arraycopy(byteLength, 0, response[frameNum], 1, 4);
        // 将bytes格式的body保存至response
        System.arraycopy(fragment.getBody(), 0, response[frameNum], 5, length);
        // 接收到的分组数 + 1
        recivedFragmentNum ++;

    }
}
