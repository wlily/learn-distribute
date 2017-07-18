package com.wll.test;

import java.io.Serializable;

/**
 * Created by wll on 2016/05/10.
 */
public class DemoBean implements Serializable {
    private static final long serialVersionUID = -5141784402935371524L;
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
