package com.mw.omc.tool.snmp;

import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by 00054054 on 2015/09/09.
 */

public class SnmpV2Get {
    private String target_addr="108.28.170.32/161";
    private String read_community="public";
    private Snmp snmp;

    public SnmpV2Get() throws IOException {
        snmp = new Snmp(new DefaultUdpTransportMapping());
        snmp.listen();
    }

    public void test() throws IOException {
        Target target = Snmp4JHelper.createCommunityTarget(new UdpAddress(target_addr), read_community);
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version2c, PDU.GET);
        pdu.setType(PDU.GET);
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.119.2.3.69.401.6.1.1.0")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.119.2.3.69.401.6.2.1.2.1")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.119.2.3.69.401.6.2.1.3.1")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.119.2.3.69.401.5.8.1.1.2.1")));
        sendRequest(pdu, target);
    }

    private void sendRequest(PDU pdu, Target target) throws IOException {
        ResponseEvent responseEvent = snmp.send(pdu, target);
        PDU response = responseEvent.getResponse();

        if (response == null) {
            System.out.println("TimeOut...");
        } else {
            if (response.getErrorStatus() == PDU.noError) {
                Vector<? extends VariableBinding> vbs = response.getVariableBindings();
                for (VariableBinding vb : vbs) {
                    System.out.println(vb + " ," + vb.getVariable().getSyntaxString());
                }
            } else {
                System.out.println("Error:" + response.getErrorStatusText());
            }
        }
    }

    private void testDevicePara(PDU pdu) {
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.2.1")));  //网元ID
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.2.2")));  //网元名称
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.2.3"))); 
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.2.4"))); 
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.2.5"))); 
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.2.6"))); 
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.2.7")));
    }

    private void testNeVersion(PDU pdu) {
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.3.1.0")));  //接口版本号
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.3.2.0")));  //
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.3.3.0")));
    }

    private void testPoll(PDU pdu) {
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.1.0"))); //sysDesc
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.5.0"))); //sysName
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SnmpV2Get snmpV2Get = new SnmpV2Get();
        snmpV2Get.test();
    }
}
