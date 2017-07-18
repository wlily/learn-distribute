package com.wll.test;

/**
 * Created by wll on 2015/09/17.
 */

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface HaiRemoteHome extends EJBHome {
    HaiRemote create() throws RemoteException, CreateException;
}