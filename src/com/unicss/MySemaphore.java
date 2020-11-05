package com.unicss;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Semaphore主要用于保护资源
 */
public class MySemaphore extends Thread {

    Semaphore position;
    private int id;

    public MySemaphore(int i, Semaphore s) {
        this.id = i;
        this.position = s;
    }

    @Override
    public void run() {
        try {
            if (position.availablePermits() > 0) {
                System.out.println("顾客[" + this.id + "]进入厕所，有空位");
            } else {
                System.out.println("顾客[" + this.id + "]进入厕所，没空位，排队");
            }

            Thread.sleep((int) (Math.random() * 1000));
            System.out.println("顾客[" + this.id + "]使用完毕");

            position.release();//释放资源

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Semaphore position = new Semaphore(2);

        for (int i = 0; i < 10; i++) {
            executorService.submit(new MySemaphore(i + 1, position));
        }


        executorService.shutdown();

        position.acquireUninterruptibly(2);
        //有点问题
        System.out.println("使用完毕，需要清扫了");
        System.out.println("剩余队列大小:" + position.getQueueLength());
        position.release(2);
    }
}