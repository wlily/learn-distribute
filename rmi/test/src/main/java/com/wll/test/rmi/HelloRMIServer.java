package com.wll.test.rmi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by 00054054 on 2016/03/02.
 */
public class HelloRMIServer extends UnicastRemoteObject implements IHello {
    private static final long serialVersionUID = 4077329331699640331L;

    // 这个实现必须有一个显式的构造函数，并且要抛出一个RemoteException异常
    protected HelloRMIServer() throws RemoteException{
        super();
    }

    /**
     * 说明清楚此属性的业务含义
     */
    @Override
    public String sayHello(String name) {
        System.out.println("hello");
        return "Hello " + name + " ^_^ ";
    }

    public static void main(String[] args) {
        try {
            //创建一个远程对象
            IHello hello = new HelloRMIServer();

            System.setProperty("java.rmi.server.hostname" , "10.86.38.22" );//指定监听IP(当服务器有多个IP时)

//            绑定数据通信端口?
            RMISocketFactory.setSocketFactory(new RMISocketFactory() {
                @Override
                public Socket createSocket(String host, int port) throws IOException {
                    System.out.println("ok,a client connection is established...port:" + port + " host:" + host);
                    return new Socket(host, port);
                }

                @Override
                public ServerSocket createServerSocket(int port) throws IOException {
                    System.out.println("createServerSocket: port is " + port);
                    if (port == 0) {
                        port = 8081;
                    }
                    return new ServerSocket(port);
                }
            });

            // 本地主机上的远程对象注册表Registry的实例，并指定端口为8888，
            // 这一步必不可少，缺少注册表创建，则无法绑定对象到远程注册表上
            LocateRegistry.createRegistry(8888);

            //把远程对象注册到RMI注册服务器上，并命名为RHello, port不指定的话默认是1099
            //绑定的URL标准格式为：rmi://host:port/name(其中协议名可以省略，下面两种写法都是正确的）
            Naming.bind("rmi://10.86.38.22:8888/Hello", hello);
//            Naming.bind("rmi://localhost:8888/Hello", hello);
//            Naming.bind("//localhost:8888/Hello", hello);

            System.out.println("Remote Hello object bind successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
