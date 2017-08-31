package com.mw.omc.tool.snmp;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by 00054054 on 2016/08/31.
 */
public class SnmpV3Server implements CommandResponder {
    public static final String USER_NAMME = "zte";
    public static final String AUTH_PWD = "ztezteztezte";
    public static final String PRIV_PWD = "ztezteztezte";

    // 本地IP与监听端口
    private Address listenAddress = GenericAddress.parse("udp:10.86.38.41/162");

    private void start() throws IOException {
        /**********多线程接收****************/
//        ThreadPool threadPool = ThreadPool.create("Trap", 2);
//        MultiThreadedMessageDispatcher dispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());

        TransportMapping transport = new DefaultUdpTransportMapping((UdpAddress) listenAddress);
//        Snmp snmp = new Snmp(dispatcher, transport);
        Snmp snmp = new Snmp(transport);
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
        // add all security protocols
        SecurityProtocols.getInstance().addDefaultProtocols();

        /**********单线程接收******************/
        //Snmp snmp = new Snmp(transport);

        /**********V3参数设置*******************/
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);

        // Add User
        UsmUser user = new UsmUser(
                new OctetString(USER_NAMME),
                AuthMD5.ID, new OctetString(AUTH_PWD),
                PrivDES.ID, new OctetString(PRIV_PWD));
        //If the specified SNMP engine id is specified, this user can only be used with the specified engine ID
        //So if it's not correct, will get an error that can't find a user from the user table.
        byte[] engineID = new byte[]{
                (byte) 0x80, (byte) 0x00, (byte) 0x1F, (byte) 0x88,
                (byte) 0x80, (byte) 0x3A, (byte) 0xD2, (byte) 0xC4,
                (byte) 0x9B, (byte) 0x55, (byte) 0xB7, (byte) 0x1A, (byte) 0xE9};
        snmp.getUSM().addUser(new OctetString(USER_NAMME), new OctetString(engineID), user);
        snmp.getUSM().addUser(new OctetString(USER_NAMME), user);

        //设置Trap接收处理类
        snmp.addCommandResponder(this);

        //启动监听
        snmp.listen();
        System.out.println("Begin Listen.");

        while(true){
        }
    }

    /**
     * 实现CommandResponder的processPdu方法, 用于处理传入的请求、PDU等信息
     * 当接收到trap时，会自动进入这个方法
     *
     * @param respEvnt
     */
    public void processPdu(CommandResponderEvent respEvnt) {
        System.out.println("receive something");
        // 解析Response
        if (respEvnt != null && respEvnt.getPDU() != null) {
            Vector<VariableBinding> recVBs = respEvnt.getPDU().getVariableBindings();
            for (int i = 0; i < recVBs.size(); i++) {
                VariableBinding recVB = recVBs.elementAt(i);
                System.out.println(recVB.getOid() + " : " + recVB.getVariable());
            }
        }
    }

    public static void main(String[] args) {
        try {
            new SnmpV3Server().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
