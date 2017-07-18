package com.wll.test;

/**
 * Created by wll on 2015/09/17.
 */

import java.rmi.RemoteException;
import javax.ejb.EJBObject;


public interface HaiRemote extends EJBObject {
    String sayHai() throws RemoteException;
}
