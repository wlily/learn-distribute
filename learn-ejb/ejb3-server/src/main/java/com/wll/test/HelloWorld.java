package com.wll.test;

import javax.ejb.Remote;

/**
 * Created by wll on 2015/09/17.
 */
@Remote
public interface HelloWorld {
    public String sayHello(String name);
}
