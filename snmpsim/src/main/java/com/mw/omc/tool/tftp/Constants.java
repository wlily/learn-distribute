package com.mw.omc.tool.tftp;

/**
 * Created by 00054054 on 2016/09/14.
 */
public class Constants {
    public static final int DEFAULT_TFTP_PORT = 69;
    public static final String SERVER_READ_DIR;
    public static final String SERVER_WRITE_DIR;

    static {
        SERVER_READ_DIR = System.getProperty("tftp.server.read_dir",System.getProperty("java.io.tmpdir"));
        SERVER_WRITE_DIR = System.getProperty("tftp.server.write_dir",System.getProperty("java.io.tmpdir"));
    }
}
