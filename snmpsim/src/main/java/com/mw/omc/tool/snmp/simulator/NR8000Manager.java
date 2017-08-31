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
public class NR8000Manager extends SnmpV2Entity {
    private String agentUrl = "108.28.170.9/161";
    private String readCommunity = "public";
    private String writeCommunity = "private";

    public NR8000Manager() throws IOException {
        super();
    }

    public NR8000Manager(final String localIP, String storageFile) throws IOException {
        super(localIP, storageFile);
    }

    private void poll() throws IOException {
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version2c, PDU.GET);
        pdu.setType(PDU.GET);
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.1.1.2.0"))); //sysDesc
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.1.1.1.0"))); //sysName

        sendRequest(pdu, agentUrl, readCommunity);
    }

    private void getNeVersion() throws IOException {
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version2c, PDU.GET);
        pdu.setType(PDU.GET);
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.3.1.0"))); //sysDesc
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.3.2.0"))); //sysDesc
//        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.6.3.3.0"))); //sysDesc

        sendRequest(pdu, agentUrl, readCommunity);
    }

    private void reverse() throws IOException {
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version2c, PDU.SET);
        pdu.setType(PDU.SET);
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.8000.1.41.4.1.0"), new OctetString("10.86.95.16"))); //ip
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.8000.1.41.4.2.0"), new OctetString("mwCM"))); //name
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.8000.1.41.4.3.0"), new OctetString("mwCM"))); //pwd
        sendRequest(pdu, agentUrl, writeCommunity);
    }

    private void getCommunityswitch() throws IOException {
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version2c, PDU.GET);
        pdu.setType(PDU.GET);
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.13.3.1"))); //communityEnable
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.13.3.2")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.13.3.3")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.13.3.4")));
        sendRequest(pdu, agentUrl, readCommunity);
    }

    private void setCommunityswitch() throws IOException {
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version2c, PDU.SET);
        pdu.setType(PDU.SET);
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.13.3.1"), new Integer32(1))); //communityEnable
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.13.3.2"), new OctetString("public")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.13.3.3"), new OctetString("private")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.13.3.4"), new OctetString("public")));
        sendRequest(pdu, agentUrl, writeCommunity);
    }

    private void getSnmpSwitch() throws IOException {
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version2c, PDU.GET);
        pdu.setType(PDU.GET);
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.13.4"))); //snmpManagerAllEnable

        sendRequest(pdu, agentUrl, readCommunity);
    }

    private void getTrapSwitch() throws IOException {
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version2c, PDU.GET);
        pdu.setType(PDU.GET);
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.13.5"))); //trapAllEnable

        sendRequest(pdu, agentUrl, readCommunity);
    }

    private void setSnmpSwitch() throws IOException {
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version2c, PDU.SET);
        pdu.setType(PDU.SET);
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.13.4"), new Integer32(1))); //communityEnable

        sendRequest(pdu, agentUrl, writeCommunity);
    }

    private void setTrapSwitch() throws IOException {
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version2c, PDU.SET);
        pdu.setType(PDU.SET);
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.13.5"), new Integer32(1)));

        sendRequest(pdu, agentUrl, writeCommunity);
    }

    public static void main(String[] args) {
        try {
            NR8000Manager manager = new NR8000Manager();
            manager.poll();
//            manager.getNeVersion();
//            manager.getCommunityswitch();
//            manager.setSnmpSwitch();
            manager.getSnmpSwitch();
//            manager.setTrapSwitch();
            manager.getTrapSwitch();
            manager.startManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
