package com.wll.test.rmi;

import java.util.Calendar;

/**
 * Created by 00054054 on 2016/05/11.
 */
public class Test {
//Difference between the CORBA Epoch and the Unix Epoch: the time from 1582/10/15 00:00 until 1970/01/01 00:00 in 100 ns units.
    @org.junit.Test
    public void test(){
        Calendar date = Calendar.getInstance();
        date.set(1582,Calendar.OCTOBER,15,0,0);
        System.out.println(date.getTime().getTime());

        Calendar date2 = Calendar.getInstance();
        date2.set(1970,Calendar.JANUARY,1,0,0);
        System.out.println(date2.getTime().getTime());

        System.out.println(date2.getTime().getTime() - date.getTime().getTime());
    }
}
