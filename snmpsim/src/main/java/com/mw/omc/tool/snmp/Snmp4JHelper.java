package com.mw.omc.tool.snmp;

import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.transport.AbstractTransportMapping;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

/**
 * Created by 00054054 on 2015/07/29.
 */
public class Snmp4JHelper {

    public static Snmp createSnmpSession(Address address) throws IOException {
        AbstractTransportMapping transport;
        if (address instanceof TcpAddress) {
            transport = new DefaultTcpTransportMapping();
        } else {
            transport = new DefaultUdpTransportMapping();
        }
        Snmp snmp = new Snmp(transport);
        return snmp;
    }

    public static Target createCommunityTarget(Address address, String community) {
        CommunityTarget target = new CommunityTarget();
        target.setRetries(2);
        target.setTimeout(3000);
        target.setAddress(address);
        target.setVersion(SnmpConstants.version2c);
        target.setCommunity(new OctetString(community));
        return target;
    }


    public static Target createUseryTarget(Address address, String userName) {
        UserTarget target = new UserTarget();
        target.setRetries(1);
        target.setTimeout(3000);
        target.setAddress(address);
        target.setVersion(SnmpConstants.version3);
        target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
        target.setSecurityName(new OctetString(userName));
        return target;
    }

    public static PDU createPDU(int snmpVersion, int pduType) {
        PDU request;
        switch (snmpVersion) {
            case SnmpConstants.version1:
                request = new PDUv1();
                break;
            case SnmpConstants.version2c:
                request = new PDU();
                break;
            case SnmpConstants.version3:
                request = new ScopedPDU();
                break;
            default:
                request = new PDU();
                break;
        }
        request.setType(pduType);
        return request;
    }

    public static PDU send(Snmp snmp, PDU pdu, Target target) {
        ResponseEvent response = null;
        try {
            response = snmp.send(pdu, target);
            if (response.getResponse() == null) {
                //request time out
            } else {
                System.out.println("Received response from: " + response.getPeerAddress());
                // dump response PDU
                System.out.println(response.getResponse().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.getResponse();
    }


    public static void asynSend(final Snmp snmp, PDU pdu, Target target, ResponseListener listener) {
        try {
            snmp.send(pdu, target, null, listener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ResponseListener listener = new ResponseListener() {
        public void onResponse(ResponseEvent event) {
            PDU response = event.getResponse();
            PDU request = event.getRequest();
            if (response == null) {
                System.out.println("Request " + request + " timed out");
            } else {
                System.out.println("Received response " + response + " on request " +
                        request);
            }
        }
    };
}
