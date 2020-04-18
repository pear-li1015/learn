package jvm;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Author: bin
 * @Date: 2020/2/28 10:38
 * @Description:
 */
public class 调优 {
    public static void main(String[] args) {

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        List<String> list = new LinkedList<>();
        list.forEach((String o) -> {

        });
        // Java虚拟机试图使用的最大内存
        long maxMemory = Runtime.getRuntime().maxMemory();
        // Java虚拟机的内存总量
        long totalMemory = Runtime.getRuntime().totalMemory();
        System.out.println("MAX_MEMORY = " + maxMemory + " byte " + (maxMemory/(double)1024/1024) + " MB");
        System.out.println("MAX_MEMORY = " + totalMemory + " byte " + (totalMemory/(double)1024/1024) + " MB");
    }
}





