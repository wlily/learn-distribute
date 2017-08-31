package com.mw.omc.tool.tftp;

import java.io.*;

/**
 * Created by 00054054 on 2016/09/14.
 */
public class TFTPServerConsoleTest {

    private final static String WRITE_PATH = "E:\\tftp";
    private final static String READ_PATH = "E:\\tftp";

    private final static String LOG_FILE="e:\\log\\tftp.log";
    private final static String LOG_ERROR_FILE="e:\\log\\tftp.err";

    public static void main(String[] args) throws Exception {
        TFTPServerConsole console = TFTPServerConsole.getInstance();
        console.startServer();
        System.out.println("TFTP Server running.  Press enter to stop.");
        new InputStreamReader(System.in).read();
        console.stopServer();
        System.exit(0);
    }


    public void startServer() throws Exception {
        {
            PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream(LOG_FILE)));
            PrintStream err = new PrintStream(new BufferedOutputStream(new FileOutputStream(LOG_ERROR_FILE)));
            TFTPServer ts = new TFTPServer(new File(READ_PATH), new File(WRITE_PATH),6900, TFTPServer.ServerMode.GET_AND_PUT,
                    out,err );
            ts.setSocketTimeout(2000);
            System.out.println("TFTP Server running.  Press enter to stop.");
            new InputStreamReader(System.in).read();
            ts.shutdown();
            System.out.println("Server shut down.");
            System.exit(0);
        }
    }
}
