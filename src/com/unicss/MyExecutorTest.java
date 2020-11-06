package com.unicss;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class MyExecutorTest extends Thread {

    public static AtomicLong i = new AtomicLong(0);

    private static final int COUNT = 1000000;

    private static CountDownLatch countDownLatch = new CountDownLatch(COUNT);

    @Override
    public void run() {
        try {

            while (i.get() < COUNT) {
                System.out.println(i.incrementAndGet());
                //如果发生了异常，导致countDown未执行，则主线程将会一直阻塞
                //一般来说，最好将countDown放在finally块中
                //可以测试一下，new CountDownLatch(1)，然后countDown移出while块中，看下耗时差多少
                //CountDownLatch是线程安全的吧？
                countDownLatch.countDown();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            service.execute(new MyExecutorTest());
        }

        service.shutdown();

        try {
            countDownLatch.await();
            System.out.println("final value:" + i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
