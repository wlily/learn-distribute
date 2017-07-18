package com.wll.test;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wll on 2016/05/10.
 */
public class JsonRpcTest {
    static JsonRpcHttpClient client;

    public static void main(String[] args) throws Throwable {
        // 实例化请求地址，注意服务端web.xml中地址的配置
        try {
            client = new JsonRpcHttpClient(new URL("http://127.0.0.1:8080/rpc"));
            // 请求头中添加的信息
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("UserKey", "hjckey");
            // 添加到请求头中去
            client.setHeaders(headers);

            JsonRpcTest test = new JsonRpcTest();
            test.doSomething();
            DemoBean demo = test.getDemo(1, "哈");
            int code = test.getInt(2);
            String msg = test.getString("哈哈哈");
            // print
            System.out.println("===========================javabean");
            System.out.println(demo.getCode());
            System.out.println(demo.getMsg());
            System.out.println("===========================Integer");
            System.out.println(code);
            System.out.println("===========================String");
            System.out.println(msg);
            System.out.println("===========================end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doSomething() throws Throwable {
        client.invoke("doSomething", null);
    }

    public DemoBean getDemo(int code, String msg) throws Throwable {
        String[] params = new String[] { String.valueOf(code), msg };
        DemoBean demo = null;
        demo = client.invoke("getDemo", params, DemoBean.class);
        return demo;
    }

    public int getInt(int code) throws Throwable {
        Integer[] codes = new Integer[] { code };
        return client.invoke("getInt", codes, Integer.class);
    }

    public String getString(String msg) throws Throwable {
        String[] msgs = new String[] { msg };
        return client.invoke("getString", msgs, String.class);
    }
}