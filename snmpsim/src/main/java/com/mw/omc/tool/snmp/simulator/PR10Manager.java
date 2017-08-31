package com.mw.omc.tool.snmp.simulator;

import com.mw.omc.tool.snmp.Snmp4JHelper;
import org.snmp4j.PDU;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

import java.io.IOException;

/**
 * Created by 00054054 on 2016/09/01.
 */
public class PR10Manager extends SnmpV2Entity {
    private String agentUrl = "108.28.170.27/161";
    private String readCommunity = "public";
    private String writeCommunity = "public";

    public PR10Manager() throws IOException {
        super();
    }

    public PR10Manager(final String localIP, String storageFile) throws IOException {
        super(localIP, storageFile);
    }

    private void poll() throws IOException {
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version2c, PDU.GET);
        pdu.setType(PDU.GET);
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.1.1.2.0"))); //sysDesc
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.1.47.1.1.1.1.8.0"))); //sysName

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

    public static void main(String[] args) {
        try {
            PR10Manager manager = new PR10Manager();
            manager.poll();
            manager.reverse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
