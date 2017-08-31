package com.mw.omc.tool.tftp;

import java.io.File;
import java.io.InputStreamReader;

import static com.mw.omc.tool.tftp.TFTPServer.ServerMode.GET_AND_PUT;

/**
 * Created by 00054054 on 2016/09/14.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("You must provide 1 argument - the base path for the server to serve from.");
            System.exit(1);
        }

        TFTPServer ts = new TFTPServer(new File(args[0]), new File(args[0]), GET_AND_PUT);
        ts.setSocketTimeout(2000);

        System.out.println("TFTP Server running.  Press enter to stop.");
        new InputStreamReader(System.in).read();

        ts.shutdown();
        System.out.println("Server shut down.");
        System.exit(0);
    }
}
