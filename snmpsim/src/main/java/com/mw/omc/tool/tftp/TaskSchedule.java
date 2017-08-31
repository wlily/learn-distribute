package com.mw.omc.tool.tftp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 00054054 on 2016/09/14.
 */
public class TaskSchedule<T extends AbstractTask> {
    CyclicBarrier cyclicBarrier;
    long begin = System.currentTimeMillis();
    List<T> tasks = new ArrayList<T>();
    ExecutorService executor = Executors.newCachedThreadPool();

    public TaskSchedule(final long duration, int taskCount) {
        System.out.println("TFTP client task is running...");
        System.out.println("Total task : " + taskCount);
        System.out.println("Time of duration :" + duration + "s");
        cyclicBarrier = new CyclicBarrier(taskCount, new Runnable() {
            @Override
            public void run() {
                long times = 0L;
                long successTimes = 0L;
                while (System.currentTimeMillis() - begin > duration * 1000) {
                    for (AbstractTask task : tasks) {
                        times = task.getTotalTimes();
                        successTimes += task.getSuccessTimes();
                        System.out.println("==============================================================");
                        //System.out.println(task.getTaskId() + " execute total times is: " + task.getTotalTimes());
                        System.out.println(task.getTaskId() + " execute success times is: " + task.getSuccessTimes());
                        System.out.println(task.getTaskId() + " execute fail times is: " + task.getFailTimes());
                        //System.out.println(task.getTaskId() + " execute rate is: " + task.getTotalTimes() / duration + "times/s");
                        System.out.println("==============================================================");
                    }
                    System.out.println("All of " + tasks.size() + " tasks execute TFTP transfer file task total count is : " + times);
                    System.out.println("Execute success rate is: " + successTimes / duration + " times/s");
                    executor.shutdown();
                    return;
                }
                System.out.println("Task is execute...");
            }
        });
    }

    public void schedule(T task) {
        tasks.add(task);
        executor.execute(task);
    }
}
