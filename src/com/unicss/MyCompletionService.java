package com.unicss;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 当向Executor提交批处理任务时，并且希望在它们完成后获得结果，如果用FutureTask，你可以循环获取task，并用future.get()去获取结果，但是如果这个task没有完成，你就得阻塞在这里，这个实效性不高，其实在很多场合，其实你拿第一个任务结果时，此时结果并没有生成并阻塞，其实在阻塞在第一个任务时，第二个task的任务已经早就完成了，显然这种情况用future task不合适的，效率也不高。
 *        自己维护list和CompletionService的区别：
 *
 * 从list中遍历的每个Future对象并不一定处于完成状态，这时调用get()方法就会被阻塞住，如果系统是设计成每个线程完成后就能根据其结果继续做后面的事，这样对于处于list后面的但是先完成的线程就会增加了额外的等待时间。
 * 而CompletionService的实现是维护一个保存Future对象的BlockingQueue。只有当这个Future对象状态是结束的时候，才会加入到这个Queue中，take()方法其实就是Producer-Consumer中的Consumer。它会从Queue中取出Future对象，如果Queue是空的，就会阻塞在那里，直到有完成的Future对象加入到Queue中。
 *        CompletionService采取的是BlockingQueue<Future<V>>无界队列来管理Future。则有一个线程执行完毕把返回结果放到BlockingQueue<Future<V>>里面。就可以通过completionServcie.take().get()取出结果。
 *          方法区别：
 *
 * take 方获取并移除表示下一个已完成任务的 Future，如果目前不存在这样的任务，则等待。<如果需要用到返回值建议用take>
 * poll 获取并移除表示下一个已完成任务的 Future，如果不存在这样的任务，则返回null。
 */
public class MyCompletionService implements Callable<String> {
    private int id;

    public MyCompletionService(int i) {
        this.id = i;
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletionService<String> completion = new ExecutorCompletionService<String>(executorService);
        for (int i = 0; i < 10; i++) {
            completion.submit(new MyCompletionService(i));
        }

        for (int i = 0; i < 10; i++) {
            System.out.println(completion.take().get());
        }

        executorService.shutdown();
    }

    @Override
    public String call() throws Exception {
        Integer time = (int) (Math.random() * 1000);
        try {
            System.out.println(this.id + " start");
            Thread.sleep(time);
            System.out.println(this.id + " end");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.id + ":" + time;
    }
}