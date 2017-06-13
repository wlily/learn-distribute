package com.wll.test;

import javax.ejb.Stateless;

/**
 * Created by 00054054 on 2015/09/17.
 */
@Stateless

public class HelloWorldBean implements HelloWorld {

    @Override
    public String sayHello(String name) {
        return name + " say hello to ejb3!";
    }

}