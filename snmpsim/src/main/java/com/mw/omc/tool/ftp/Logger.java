package com.mw.omc.tool.ftp;

/**
 * Created by 00054054 on 2016/09/01.
 */
public class Logger {
    private String clazzName;

    public Logger(String clazzName){
        this.clazzName = clazzName;
    }

    public static Logger getLogger(Class clazz){
        return new Logger(clazz.getName());
    }

    public void info(String msg){
        System.out.println(clazzName + ": " + msg);
    }

    public void error(String msg){
        System.out.println(clazzName + ": " +msg);
    }
}
