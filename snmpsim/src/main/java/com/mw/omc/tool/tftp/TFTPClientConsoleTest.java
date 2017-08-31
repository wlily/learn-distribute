package com.mw.omc.tool.tftp;

import org.apache.commons.net.tftp.TFTP;

import java.util.Random;

/**
 * Created by 00054054 on 2016/09/14.
 */
public class TFTPClientConsoleTest {
    static final String USAGE =
            "Usage: tftp [options] hostname localfile remotefile\n\n" +
                    "hostname   - The name of the remote host\n" +
                    "localfile  - The name of the local file to send or the name to use for\n" +
                    "\tthe received file\n" +
                    "remotefile - The name of the remote file to receive or the name for\n" +
                    "\tthe remote server to use to name the local file being sent.\n\n" +
                    "options: (The default is to assume -r -b)\n" +
                    "\t-s Send a local file\n" +
                    "\t-r Receive a remote file\n" +
                    "\t-a Use ASCII transfer mode\n" +
                    "\t-b Use binary transfer mode\n";


    private final static int CLIENT_COUNT = 200;
    private final static int DURATION = 300;
    private final static String TASK_PREFIX = "TFTP-client-";

    public static void main(String[] args) {
        int argc = 0;
        String arg = "";
        boolean receiveFile = false;
        int transferMode = TFTP.ASCII_MODE;
        String hostname, localFilename = "", remoteFilename = "";
        // Parse options
        for (argc = 0; argc < args.length; argc++) {
            arg = args[argc];
            if (arg.startsWith("-")) {
                if (arg.equals("-r")) {
                    receiveFile = true;
                } else if (arg.equals("-s")) {
                    receiveFile = false;
                } else if (arg.equals("-a")) {
                    transferMode = TFTP.ASCII_MODE;
                } else if (arg.equals("-b")) {
                    transferMode = TFTP.BINARY_MODE;
                } else {
                    System.err.println("Error: unrecognized option.");
                    System.err.print(USAGE);
                    System.exit(1);
                }
            } else {
                break;
            }
        }

        // Make sure there are enough arguments
        if (args.length - argc < 2 || args.length - argc > 3) {
            System.err.println("Error: invalid number of arguments.");
            System.err.print(USAGE);
            System.exit(1);
        }

        // Get host and file arguments
        hostname = args[argc];
        if (args.length - argc == 2) {
            localFilename = remoteFilename = args[argc + 1];
        }
        if (args.length - argc == 3) {
            localFilename = args[argc + 1];
            remoteFilename = args[argc + 2];
        }


        TaskSchedule schedule = new TaskSchedule(DURATION, CLIENT_COUNT);
        for (int i = 0; i < CLIENT_COUNT; i++) {
            String taskId = TASK_PREFIX + i;
            Random random = new Random();
            //取1-500之间的正整数
            int temp = random.nextInt(SOURCE_FILE_COUNT);
            localFilename = SOURCE_FILE_DIR + PREFIX + temp;
            //remoteFilename = taskId + PREFIX + System.currentTimeMillis();
            remoteFilename = PREFIX + temp;

            TFTPClientTask task = new TFTPClientTask.TFTPClientTaskBuilder(taskId)
                    .host(hostname)
                    .isReceive(receiveFile)
                    .localFilename(localFilename)
                    .remoteFilename(remoteFilename)
                    .mode(transferMode)
                    .barrier(schedule.cyclicBarrier)
                    .builder();
            schedule.schedule(task);
        }
    }

    private final static String SOURCE_FILE_DIR = "D:\\work\\documents\\tftp-test-file\\";
    private final static String PREFIX = "CiscoConfig-";
    //upload file count + 1
    private final static int SOURCE_FILE_COUNT = 501;
}