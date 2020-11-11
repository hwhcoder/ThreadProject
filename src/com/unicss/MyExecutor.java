package com.unicss;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyExecutor extends Thread {
    private int index;

    public MyExecutor(int i) {
        this.index = i;
    }

    @Override
    public void run() {
        try {
            System.out.println("[" + this.index + "] start....");
            Thread.sleep((int) (Math.random() * 10000));
            System.out.println("[" + this.index + "] end.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 10; i++) {
            //void execute(Runnable command);
            executorService.execute(new MyExecutor(i));
//            Future<?> submit(Runnable task);
//            executorService.submit(new MyExecutor(i));
        }
        System.out.println("submit finish");
        executorService.shutdown();
    }
}