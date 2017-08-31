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
public class SnmpV2GetBulk {
    public static final String TARGET_ADDR = "108.28.170.118/161";
    public static final String READ_COMMUNITY = "public";

    public static void main(String[] args) throws IOException, InterruptedException {
        Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
        snmp.listen();

        Target target = Snmp4JHelper.createCommunityTarget(new UdpAddress(TARGET_ADDR), READ_COMMUNITY);
        sendRequest(snmp, createGetBulkPdu(), target);
    }

    private static PDU createGetBulkPdu() {
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version2c, PDU.GET);
        pdu.setType(PDU.GETBULK);
        pdu.setMaxRepetitions(10);
        pdu.setNonRepeaters(0);

        //ODU软件版本
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.11.1.1.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.11.1.2.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.11.1.3.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.11.1.4.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.11.1.5.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.11.1.6.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.11.1.7.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.11.1.8.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.11.1.9.0")));

        //MODSTATUS
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.15.1.1.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.15.1.2.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.15.1.3.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.15.1.4.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.15.1.5.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.15.1.6.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.15.1.7.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.15.1.8.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.15.1.9.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.15.1.11.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.15.1.12.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.15.1.13.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.15.1.14.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.15.1.15.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.15.1.16.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.15.1.17.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.15.1.18.0")));

        //RFSTATUS
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.16.1.1.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.16.1.2.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.16.1.3.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.16.1.4.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.16.1.5.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.16.1.6.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.16.1.7.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.16.1.8.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.16.1.9.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.16.1.10.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.16.1.12.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.16.1.13.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.16.1.14.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.16.1.15.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.16.1.16.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.16.1.17.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.16.1.18.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.16.1.19.0")));

//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.1.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.2.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.3.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.4.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.5.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.6.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.7.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.8.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.9.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.10.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.12.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.13.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.14.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.15.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.16.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.17.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.18.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.19.0")));

//                pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.24.1.1.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.24.1.2.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.24.1.3.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.24.1.4.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.24.1.5.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.24.1.6.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.24.1.7.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.24.1.8.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.24.1.9.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.24.1.10.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.24.1.11.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.15.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.16.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.17.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.18.0")));
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.3.18.1.19.0")));

//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.23.1.1.1.0"))); //sysName
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.23.1.1.2.0"))); //sysName

        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.10.23.1.7.1.1.1.50397955.0.255.255.0.0.0.0.0.0.0.0"))); //sysName

        //NR8250_R_TULCNS  WCCapacity 1.00.010a之前版本

        return pdu;
    }

    private static void sendRequest(Snmp snmp, PDU pdu, Target target) throws IOException {
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
}
