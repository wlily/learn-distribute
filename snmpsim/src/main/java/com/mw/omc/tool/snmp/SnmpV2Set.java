package com.mw.omc.tool.snmp;

import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by 00054054 on 2015/09/09.
 */
public class SnmpV2Set {
    public static final String TARGET_ADDR = "108.28.170.32/161";
    public static final String SET_COMMUNITY = "public";
    private Snmp snmp;

    public SnmpV2Set() throws IOException {
        snmp = new Snmp(new DefaultUdpTransportMapping());
        snmp.listen();
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        SnmpV2Set snmpV2Set = new SnmpV2Set();
        snmpV2Set.registerEmsIP();
    }


    private void registerEmsIP() throws IOException {
        Target target = Snmp4JHelper.createCommunityTarget(new UdpAddress(TARGET_ADDR), SET_COMMUNITY);
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version2c, PDU.SET);
        pdu.setType(PDU.SET);

        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.119.2.3.69.3.4.1.1.3.2.1"), new Integer32(1)));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.119.2.3.69.3.4.1.1.3.2.2"), new Integer32(1)));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.119.2.3.69.3.4.1.1.3.2.3"), new Integer32(1)));

        sendRequest(pdu, target);
    }

    private void test() throws IOException {
        Target target = Snmp4JHelper.createCommunityTarget(new UdpAddress(TARGET_ADDR), SET_COMMUNITY);
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version2c, PDU.SET);
        pdu.setType(PDU.SET);
        byte a1 = (byte)0xec;
        byte a2 = (byte)0xc0;
        byte a3 = (byte)0x00;
        byte a4 = (byte)0x00;
        byte a5 = (byte)0x00;
        byte a6 = (byte)0xec;
        byte a7 = (byte)0x05;
        byte a8 = (byte)0x00;
        byte a9 = (byte)0x02;
        byte[] value = new byte[]{a1, a2, a3, a4, a5, a6, a7, a8, a9};
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.119.2.3.69.401.6.1.1.0"), new Integer32(1)));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.119.2.3.69.401.6.2.1.2.1"), new Integer32(0)));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.119.2.3.69.401.5.8.1.1.2.1"), new OctetString(value)));
        sendRequest(pdu, target);
    }

    private void setNeId(PDU pdu) {
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.2.1"), new UnsignedInteger32(32)));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.2.2"), new OctetString("8250-155-test")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.2.3"), new OctetString("6C1CAA9B")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.2.4"), new OctetString("FF000000")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.2.5"), new OctetString("00000000")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.2.6"), new OctetString("0;0;0;0;0;0;")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.2.7"), new UnsignedInteger32(0)));
    }

    private void setPR10FTPInfo(PDU pdu) {
        //PR10 FTP路径设置
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.8000.1.41.4.1.0"), new OctetString("108.28.170.223")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.8000.1.41.4.2.0"), new OctetString("mwRM")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.8000.1.41.4.3.0"), new OctetString("9B1E7816FAAE7702")));
    }

    private void setS340FTPInfo(PDU pdu){
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2281.10.11.1.10.1.0"), new IpAddress("108.28.170.223")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2281.10.11.1.10.2.0"), new OctetString("/cm/neconfigfile/s340/108.28.170.143"))); //
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2281.10.11.1.10.3.0"), new UnsignedInteger32(0))); // FTP(0)/SFTP(1)
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2281.10.11.1.10.4.0"), new OctetString("mwRM")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2281.10.11.1.10.5.0"), new OctetString("9B1E7816FAAE7702")));
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
}
