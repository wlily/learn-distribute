package com.mw.omc.tool.tftp;

import org.apache.commons.net.tftp.TFTP;

import java.net.SocketException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by 00054054 on 2016/09/14.
 */
public class TFTPClientTask extends AbstractTask {
    final TFTPClientWorker worker;

    /**
     * TFTPClientTask Builder
     */
    public static class TFTPClientTaskBuilder {
        private final String taskId;
        private String host = "localhost";
        private int port = Constants.DEFAULT_TFTP_PORT;
        private boolean isReceive;
        private int mode = TFTP.BINARY_MODE;// or TFTP.ASCII_MODE
        private String localFilename;
        private String remoteFilename;
        private CyclicBarrier barrier;

        public TFTPClientTaskBuilder(String taskId) {
            this.taskId = taskId;
        }

        public TFTPClientTaskBuilder host(String host) {
            this.host = host;
            return this;
        }

        public TFTPClientTaskBuilder port(int port) {
            this.port = port;
            return this;
        }

        public TFTPClientTaskBuilder isReceive(boolean isReceiver) {
            this.isReceive = isReceiver;
            return this;
        }

        public TFTPClientTaskBuilder mode(int mode) {
            this.mode = mode;
            return this;
        }

        public TFTPClientTaskBuilder localFilename(String localFilename) {
            this.localFilename = localFilename;
            return this;
        }

        public TFTPClientTaskBuilder remoteFilename(String remoteFilename) {
            this.remoteFilename = remoteFilename;
            return this;
        }

        public TFTPClientTaskBuilder barrier(CyclicBarrier barrier) {
            this.barrier = barrier;
            return this;
        }

        public TFTPClientTask builder() {
            return new TFTPClientTask(this);
        }
    }

    private TFTPClientTask(TFTPClientTaskBuilder builder) {
        TFTPClientWorker.TFTPClientWorkerBuilder jobBuilder = new TFTPClientWorker.TFTPClientWorkerBuilder();
        jobBuilder.host(builder.host)
                .port(builder.port)
                .isReceive(builder.isReceive)
                .mode(builder.mode)
                .localFilename(builder.localFilename)
                .remoteFilename(builder.remoteFilename);
        worker = jobBuilder.builder();
        taskId = builder.taskId;
        cyclicBarrier = builder.barrier;
    }

    public TFTPClientWorker getWorker() {
        return worker;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                //System.out.println("Task :" + taskId + "is executing...");
                totalTimes++;
                String filePath = worker.doJob();
                //System.out.println("Remote file path is : " + filePath);
                successTimes++;
                cyclicBarrier.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            failTimes++;
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
            failTimes++;
        } catch (SocketException e) {
            System.err.println("Error: could not open local UDP socket.");
            System.err.println(e.getMessage());
            failTimes++;
        }
    }

}