package com.mw.omc.tool.tftp;

import java.io.File;

import static com.mw.omc.tool.tftp.Constants.DEFAULT_TFTP_PORT;
import static com.mw.omc.tool.tftp.Constants.SERVER_READ_DIR;
import static com.mw.omc.tool.tftp.Constants.SERVER_WRITE_DIR;

/**
 * Created by 00054054 on 2016/09/14.
 */
public class TFTPServerConsole {
    private TFTPServer server;
    private String serverReadDir;
    private String serverWriteDir;
    private int port;
    private int timeout = 30000;
    private TFTPServer.ServerMode serverMode;
    //single instance
    private volatile static TFTPServerConsole console;

    private TFTPServerConsole() throws Exception {
        //set default value;
        serverReadDir = SERVER_READ_DIR;
        serverWriteDir = SERVER_WRITE_DIR;
        port = DEFAULT_TFTP_PORT;
        serverMode = TFTPServer.ServerMode.GET_AND_PUT;

        server = new TFTPServer(new File(serverReadDir), new File(serverWriteDir), port, serverMode,
                null, null);
        server.setSocketTimeout(timeout);
    }

    public static TFTPServerConsole getInstance() throws Exception {
        if (console == null) {
            synchronized (Object.class) {
                if (console == null) {
                    console = new TFTPServerConsole();
                }
            }
        }
        return console;
    }

    public void startServer() {
        try {
            if (server != null && !server.isRunning()) {
                server.launch();
            } else {
                System.out.println("TFTP server has been running already!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        try {
            if (server != null && server.isRunning()) {
                server.shutdown();
                System.out.println("TFTP Server shutdown.");
            } else {
                System.out.println("TFTP server is not running");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setServerReadDir(String serverReadDir) {
        this.serverReadDir = serverReadDir;
    }

    public void setServerWriteDir(String serverWriteDir) {
        this.serverWriteDir = serverWriteDir;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setServerMode(TFTPServer.ServerMode serverMode) {
        this.serverMode = serverMode;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}