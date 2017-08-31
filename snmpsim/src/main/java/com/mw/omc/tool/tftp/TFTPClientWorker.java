package com.mw.omc.tool.tftp;

import org.apache.commons.net.tftp.TFTP;
import org.apache.commons.net.tftp.TFTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import static com.mw.omc.tool.tftp.Constants.DEFAULT_TFTP_PORT;
import static com.mw.omc.tool.tftp.Constants.SERVER_READ_DIR;
import static com.mw.omc.tool.tftp.Constants.SERVER_WRITE_DIR;

/**
 * Created by 00054054 on 2016/09/14.
 */
public class TFTPClientWorker {
    private final String host;
    private final int port;
    private final boolean isReceive;
    private final int transferMode;
    private final String localFilename;
    private final String remoteFilename;

    /**
     * TFTPClientTask Builder
     */
    public static class TFTPClientWorkerBuilder {
        private String host = "localhost";
        private int port = DEFAULT_TFTP_PORT;
        private boolean isReceive;
        private int mode = TFTP.BINARY_MODE;// or TFTP.ASCII_MODE
        private String localFilename;
        private String remoteFilename;

        public TFTPClientWorkerBuilder host(String host) {
            this.host = host;
            return this;
        }

        public TFTPClientWorkerBuilder port(int port) {
            this.port = port;
            return this;
        }

        public TFTPClientWorkerBuilder isReceive(boolean isReceiver) {
            this.isReceive = isReceiver;
            return this;
        }

        public TFTPClientWorkerBuilder mode(int mode) {
            this.mode = mode;
            return this;
        }

        public TFTPClientWorkerBuilder localFilename(String localFilename) {
            this.localFilename = localFilename;
            return this;
        }

        public TFTPClientWorkerBuilder remoteFilename(String remoteFilename) {
            this.remoteFilename = remoteFilename;
            return this;
        }

        public TFTPClientWorker builder() {
            return new TFTPClientWorker(this);
        }
    }

    private TFTPClientWorker(TFTPClientWorkerBuilder builder) {
        host = builder.host;
        port = builder.port;
        isReceive = builder.isReceive;
        transferMode = builder.mode;
        localFilename = builder.localFilename;
        remoteFilename = builder.remoteFilename;
    }

    public String getHost() {
        return host;
    }

    public boolean isReceive() {
        return isReceive;
    }

    public int getTransferMode() {
        return transferMode;
    }

    public String getLocalFilename() {
        return localFilename;
    }

    public String getRemoteFilename() {
        return remoteFilename;
    }

    /**
     *
     * @return the File path in TFTP server
     * @throws SocketException
     */
    public String doJob() throws SocketException {
        TFTPClient tftp = new TFTPClient();
        // We want to timeout if a response takes longer than 60 seconds
        tftp.setDefaultTimeout(60000);
        // Open local socket
        try {
            tftp.open();
        } catch (SocketException e) {
            System.err.println("Error: could not open local UDP socket.");
            System.err.println(e.getMessage());
            throw e;
        }
        if (isReceive) {
            received(tftp);
            return SERVER_WRITE_DIR.endsWith(File.separator)?
                    SERVER_WRITE_DIR + remoteFilename:
                    SERVER_WRITE_DIR + File.separator + remoteFilename;
        } else {
            send(tftp);
            return SERVER_READ_DIR.endsWith(File.separator)?
                    SERVER_READ_DIR + remoteFilename:
                    SERVER_READ_DIR + File.separator + remoteFilename;
        }
    }

    private void received(TFTPClient tftp) {
        FileOutputStream output = null;
        File file = new File(localFilename);
        // Try to open local file for writing
        try {
            // If file exists,  overwrite it.
            output = new FileOutputStream(file,false);
        } catch (IOException e) {
            tftp.close();
            System.err.println("Error: could not open local file for writing.");
            System.err.println(e.getMessage());
            return;
        }
        // Try to receive remote file via TFTP
        try {
            tftp.receiveFile(remoteFilename, transferMode, output, host,port);
        } catch (UnknownHostException e) {
            System.err.println("Error: could not resolve hostname.");
            System.err.println(e.getMessage());
            return;
        } catch (IOException e) {
            System.err.println(
                    "Error: I/O exception occurred while receiving file.");
            System.err.println(e.getMessage());
            return;
        } finally {
            // Close local socket and output file
            tftp.close();
            try {
                if (output != null) {
                    output.close();
                }
                //closed = true;
            } catch (IOException e) {
                //closed = false;
                System.err.println("Error: error closing file.");
                System.err.println(e.getMessage());
            }
        }
    }

    private void send(TFTPClient tftp) {
        // We're sending a file
        FileInputStream input = null;
        // Try to open local file for reading
        try {
            input = new FileInputStream(localFilename);
        } catch (IOException e) {
            tftp.close();
            System.err.println("Error: could not open local file for reading.");
            System.err.println(e.getMessage());
            return;
        }
        // Try to send local file via TFTP
        try {
            tftp.sendFile(remoteFilename, transferMode, input, host, port);
        } catch (UnknownHostException e) {
            System.err.println("Error: could not resolve hostname.");
            System.err.println(e.getMessage());
            return;
        } catch (IOException e) {
            System.err.println(
                    "Error: I/O exception occurred while sending file.");
            System.err.println(e.getMessage());
            return;
        } finally {
            // Close local socket and input file
            tftp.close();
            try {
                if (input != null) {
                    input.close();
                }
                //closed = true;
            } catch (IOException e) {
                //closed = false;
                System.err.println("Error: error closing file.");
                System.err.println(e.getMessage());
            }
        }
    }
}
