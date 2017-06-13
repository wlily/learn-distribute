package com.wll.test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

/**
 * Created by 00054054 on 2015/09/17.
 */
public class EJBClient {

    public static void main(String[] args) {
        final Hashtable jndiProperties = new Hashtable();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        try {
            InitialContext ctx = new InitialContext(jndiProperties);
            String appName = "";
            String moduleName = "ejb3-server";
            String distinctName = "";
            String beanName = "HelloWorldBean";
            String viewClassName = "com.wll.test.HelloWorld";
            String lookupStr = "ejb:" + appName +
                    "/" + moduleName +
                    "/" + distinctName +
                    "/" + beanName +
                    "!" + viewClassName;
            HelloWorld hw = (HelloWorld) ctx.lookup(lookupStr);
            System.out.println(hw.sayHello("I"));
        } catch (NamingException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

}
