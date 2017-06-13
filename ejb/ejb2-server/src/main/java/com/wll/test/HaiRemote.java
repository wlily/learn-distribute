package com.wll.test;

/**
 * Created by 00054054 on 2015/09/17.
 */

import java.rmi.RemoteException;
import javax.ejb.EJBObject;


public interface HaiRemote extends EJBObject {
    String sayHai() throws RemoteException;
}
