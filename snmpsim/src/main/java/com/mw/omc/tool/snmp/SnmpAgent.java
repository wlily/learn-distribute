package com.mw.omc.tool.snmp;

import org.snmp4j.*;
import org.snmp4j.mp.StatusInformation;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

/**
 * Created by 00090943 on 15-8-3.
 */
public class SnmpAgent implements CommandResponder {
    protected TransportMapping transport = null;
    protected Snmp snmp = null;
    private Address address = GenericAddress.parse("udp:10.86.38.11/161");

    public void start() throws IOException {
        transport = new DefaultUdpTransportMapping((UdpAddress) address);
        snmp = new Snmp(transport);
        snmp.addCommandResponder(this);
        snmp.listen();
        System.out.println("begin listen");

        while(true){
            try {
                Thread.currentThread().sleep(60*60*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void processPdu(CommandResponderEvent aEvent) {
        PDU pdu = aEvent.getPDU();
        switch (pdu.getType()) {
            case PDU.GET:
                System.out.println("receive get PDU " + pdu.toString());
                for(int i=0; i<pdu.size(); i++){
                    pdu.set(i, new VariableBinding(pdu.get(i).getOid(), new OctetString("test")));
                }
                break;
            case PDU.GETNEXT:
                break;
            case PDU.SET:
                System.out.println("receive Set PDU " + pdu.toString());
                break;
        }

        pdu.setType(PDU.RESPONSE);
        pdu.setErrorIndex(0);
        pdu.setErrorStatus(0);

        try {
            System.out.println("Send Response!!");
            aEvent.getMessageDispatcher().returnResponsePdu(
                    aEvent.getMessageProcessingModel(),
                    aEvent.getSecurityModel(),
                    aEvent.getSecurityName(),
                    aEvent.getSecurityLevel(),
                    pdu,
                    aEvent.getMaxSizeResponsePDU(),
                    aEvent.getStateReference(),
                    new StatusInformation());
        } catch (MessageException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        SnmpAgent handler = new SnmpAgent();
        try {
            handler.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


