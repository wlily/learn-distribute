package com.mw.omc.tool.tftp;

import java.util.concurrent.CyclicBarrier;

/**
 * Created by 00054054 on 2016/09/14.
 */
public abstract class AbstractTask implements Runnable {
    String taskId;
    long successTimes;
    long failTimes;
    static long totalTimes;
    static CyclicBarrier cyclicBarrier;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public long getSuccessTimes() {
        return successTimes;
    }

    public void setSuccessTimes(long successTimes) {
        this.successTimes = successTimes;
    }

    public long getFailTimes() {
        return failTimes;
    }

    public void setFailTimes(long failTimes) {
        this.failTimes = failTimes;
    }

    public long getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(long totalTimes) {
        this.totalTimes = totalTimes;
    }

    public CyclicBarrier getCyclicBarrier() {
        return cyclicBarrier;
    }

    public void setCyclicBarrier(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }
}