package com.wll.test;

import javax.ejb.Remote;

/**
 * Created by 00054054 on 2015/09/17.
 */
@Remote
public interface HelloWorld {
    public String sayHello(String name);
}
