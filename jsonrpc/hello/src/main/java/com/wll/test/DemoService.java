package com.wll.test;

/**
 * Created by 00054054 on 2016/05/10.
 */
public interface DemoService {

    public DemoBean getDemo(String code, String msg);

    public Integer getInt(Integer code);

    public String getString(String msg);

    public void doSomething();
}