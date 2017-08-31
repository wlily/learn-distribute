package com.mw.omc.tool.snmp.simulator;

import com.mw.omc.tool.snmp.Snmp4JHelper;
import org.snmp4j.PDU;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

import java.io.IOException;

/**
 * Created by 00054054 on 2016/09/01.
 */
public class S220Manager extends SnmpV2Entity {
    private String agentUrl = "108.28.170.32/161";
    private String readCommunity = "public";
    private String writeCommunity = "public";

    public S220Manager() throws IOException {
        super();
    }

    public S220Manager(final String localIP, String storageFile) throws IOException {
        super(localIP, storageFile);
    }

    private void registerEmsIP() throws IOException {
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version2c, PDU.SET);
        pdu.setType(PDU.SET);

        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.119.2.3.69.3.4.1.1.3.2.1"), new Integer32(1)));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.119.2.3.69.3.4.1.1.3.2.2"), new Integer32(1)));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.119.2.3.69.3.4.1.1.3.2.3"), new Integer32(1)));

        sendRequest(pdu, agentUrl, writeCommunity);
    }

    private void batchmodify() throws IOException {
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
        sendRequest(pdu, agentUrl, writeCommunity);
    }

    private void poll() throws IOException {
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version2c, PDU.GET);
        pdu.setType(PDU.GET);
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.1.1.2.0")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.119.2.3.69.3.1.1.7.0")));
        sendRequest(pdu, agentUrl, readCommunity);
    }

    public static void main(String[] args) {
        try {
            S220Manager manager = new S220Manager();
            manager.poll();
            manager.registerEmsIP();
            manager.batchmodify();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
