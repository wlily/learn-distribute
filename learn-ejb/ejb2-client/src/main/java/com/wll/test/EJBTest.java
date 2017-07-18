package com.wll.test;

/**
 * Created by wll on 2015/09/17.
 */

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;


public class EJBTest {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty(Context.URL_PKG_PREFIXES,"org.jboss.ejb.client.naming");
        try {
            InitialContext ctx = new InitialContext(props);
            String appName = "";
            String moduleName = "ejb2-server";
            String distinctName = "";
            String beanName = "HaiEJB";
            String viewClassName = "com.wll.test.HaiRemote";

            String lookupStr = "ejb:" +  appName +
                    "/" + moduleName +
                    "/" + distinctName +
                    "/" + beanName +
                    "!" + viewClassName;

            HaiRemote hw = (HaiRemote) ctx.lookup(lookupStr);
            System.out.println(hw.sayHai());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}