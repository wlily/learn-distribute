package com.wll.test.redis;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wll on 17-7-4.
 */
public class MethodCacheInterceptor implements MethodInterceptor {
    @Autowired
    private RedisUtil redisUtil;
    private List targetNamesList = new ArrayList();  //不加入缓存的service名称
    private List methodNamesList = new ArrayList();  //不加入缓存的方法名称
    private Long defaultCacheExpireTime = 10000L; //缓存默认的过期时间

    public MethodCacheInterceptor(){

    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object value = null;
        String targetName = invocation.getThis().getClass().getName();
        String methodName = invocation.getMethod().getName();

        if(!isAddCache(targetName, methodName)){
            return  invocation.proceed();
        }

        Object[] arguments = invocation.getArguments();
        String key = getCacheKey(targetName, methodName, arguments);
        System.out.println(key);

        if(redisUtil.exists(key)){
            return redisUtil.get(key);
        }
        //写入缓存
        value = invocation.proceed();
        if(value != null){
            final String tKey = key;
            final Object tValue = value;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    redisUtil.set(tKey, tValue, defaultCacheExpireTime);
                }
            }).start();
        }
        else{
            return invocation.proceed();
        }
        return value;
    }

    private boolean isAddCache(String targetName, String methodName){
        boolean flag = true;
        if(targetNamesList.contains(targetName) || methodNamesList.contains(methodName)){
            flag = false;
        }
        return flag;
    }

    private String getCacheKey(String targetName, String methodName, Object[] arguments) {
        StringBuffer sbu = new StringBuffer();
        sbu.append(targetName).append("_").append(methodName);
        if((arguments != null) && arguments.length != 0){
            for(int i=0; i<arguments.length; i++){
                sbu.append("_").append(arguments[i]);
            }
        }
        return sbu.toString();
    }
}
