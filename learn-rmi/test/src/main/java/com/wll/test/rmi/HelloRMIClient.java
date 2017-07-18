package com.wll.test.rmi;

import java.rmi.Naming;

/**
 * Created by wll on 2016/03/02.
 */
public class HelloRMIClient {

    public static void main(String[] args) {
        try {
            IHello hello = (IHello) Naming.lookup("rmi://10.86.38.22:8888/Hello");
//            IHello hello = (IHello) Naming.lookup("rmi://localhost:8888/Hello");
            System.out.println(hello.sayHello("wll"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
