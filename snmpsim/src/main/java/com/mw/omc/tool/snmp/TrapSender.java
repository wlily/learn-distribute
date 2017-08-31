package com.mw.omc.tool.snmp;

import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by 00054054 on 2015/07/29.
 */
public class TrapSender {
    private Snmp snmp = null;
    public static final String USER_NAMME = "zte";
    public static final String AUTH_PWD = "ztezteztezte";
    public static final String PRIV_PWD = "ztezteztezte";

    // 设置管理进程的IP和端口
//    private Address targetAddress = GenericAddress.parse("udp:10.86.49.36/162");
    private Address targetAddress = GenericAddress.parse("udp:108.28.170.205/162");

    public void initComm() throws IOException {
        snmp = Snmp4JHelper.createSnmpSession(targetAddress);
    }

    /**
     * 向管理进程发送Trap报文
     */
    public void sendV2Trap() throws IOException {
        Target target = Snmp4JHelper.createCommunityTarget(targetAddress, "public");

        // 创建 PDU
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version2c, PDU.TRAP);
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.3377.10.1.1.1.1"), new OctetString("SnmpTrap")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.3377.10.1.1.1.2"), new OctetString("JavaEE")));

        // 向Agent发送PDU，并接收Response
        ResponseEvent respEvnt = snmp.send(pdu, target);

        // 解析Response
        if (respEvnt != null && respEvnt.getResponse() != null) {
            Vector<VariableBinding> recVBs = respEvnt.getResponse().getVariableBindings();
            for (int i = 0; i < recVBs.size(); i++) {
                VariableBinding recVB = recVBs.elementAt(i);
                System.out.println(recVB.getOid() + " : " + recVB.getVariable());
            }
        }
    }

       /**
     * 向管理进程发送SNMP V3 Trap报文
     */
    public void sendV3Trap() throws IOException {
        Target target = Snmp4JHelper.createUseryTarget(targetAddress, USER_NAMME);

        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);

        // Add User
        UsmUser user = new UsmUser(new OctetString(USER_NAMME), AuthMD5.ID, new OctetString(AUTH_PWD), PrivDES.ID, new OctetString(PRIV_PWD));
        //If the specified SNMP engine id is specified, this user can only be used with the specified engine ID
        //So if it's not correct, will get an error that can't find a user from the user table.
        //snmp.getUSM().addUser(new OctetString("nmsAdmin"), new OctetString("0002651100"), user);
        snmp.getUSM().addUser(new OctetString(USER_NAMME), user);

        // 创建 PDU
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version3, PDU.TRAP);
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.3377.10.1.1.1.1"), new OctetString("SnmpTrapV3")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.3377.10.1.1.1.2"), new OctetString("JavaEEV3")));

        // 向Agent发送PDU，并接收Response
        ResponseEvent respEvnt = snmp.send(pdu, target);

        // 解析Response
        if (respEvnt != null && respEvnt.getResponse() != null) {
            Vector<VariableBinding> recVBs = respEvnt.getResponse().getVariableBindings();
            for (int i = 0; i < recVBs.size(); i++) {
                VariableBinding recVB = recVBs.elementAt(i);
                System.out.println(recVB.getOid() + " : " + recVB.getVariable());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            TrapSender util = new TrapSender();
            util.initComm();
            while(true){
                util.sendV2Trap();
                Thread.currentThread().sleep(1*1000);
                util.sendV3Trap();
                Thread.currentThread().sleep(3*1000);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
