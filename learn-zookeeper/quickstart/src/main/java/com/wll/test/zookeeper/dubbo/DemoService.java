package com.wll.test.zookeeper.dubbo;

import java.util.List;

/**
 * Created by wll on 17-7-12.
 */
public interface DemoService {
    String sayHello(String name);
    public List getUsers();
}
