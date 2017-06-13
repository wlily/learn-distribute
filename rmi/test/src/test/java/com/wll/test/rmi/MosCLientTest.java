package com.wll.test.rmi;

import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 00054054 on 2016/05/27.
 */
public class MosCLientTest {

    @org.junit.Test
    public void test(){

        while(true){
            try {
                Naming.lookup("rmi://10.86.56.141:5000/client/APP_TOPO_CLIENT");
                System.out.println(now() + ": still connected");
            } catch (Exception e) {
                System.out.println(now() + ": cannot connected ");
            }

            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String now(){
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        return timeFormat.format(new Date());
    }
}
