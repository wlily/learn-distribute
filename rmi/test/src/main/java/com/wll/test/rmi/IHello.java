package com.wll.test.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by 00054054 on 2016/03/02.
 */
public interface IHello extends Remote {
    public String sayHello(String name) throws RemoteException;
}
