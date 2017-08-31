package com.mw.omc.tool.snmp.simulator;

import com.mw.omc.tool.snmp.Snmp4JHelper;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.mp.StatusInformation;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.*;

/**
 * Created by 00054054 on 2016/08/31.
 */
public class SnmpV2Entity implements CommandResponder {
    protected String localIP = "10.86.38.55";
    protected int reqPort = 161;
    protected int trapPort = 162;
    protected String readCommunity = "public";
    protected String writeCommunity = "private";
    protected String trapCommunity = "public";
    protected String storageFile = "D:\\0-ProjectCode\\mwtool\\omctool\\MibPR10.properties";
    protected Properties p;
    protected List<String> oids = new LinkedList<String>();
    private Snmp snmp;

    public SnmpV2Entity() throws IOException {
        this.localIP = Inet4Address.getLocalHost().getHostAddress();
        snmp = new Snmp(new DefaultUdpTransportMapping());
        snmp.listen();
    }

    public SnmpV2Entity(final String localIP, String storageFile) throws IOException {
        this.localIP = localIP;
        this.storageFile = storageFile;
        snmp = new Snmp(new DefaultUdpTransportMapping());
        snmp.listen();
    }

    public void startAgent() throws IOException {
        p = new Properties();
        p.load(new FileInputStream(new File(storageFile)));
        oids.addAll(p.stringPropertyNames());

        sortOids();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = /*"udp:" + */localIP + "/" + reqPort;
                try {
                    Snmp snmp = new Snmp(new DefaultUdpTransportMapping(new UdpAddress(url)));
                    snmp.addCommandResponder(SnmpV2Entity.this);
                    snmp.listen();
                    System.out.println("start listening on " + url);
                    while (true) {
                        Thread.sleep(60*60*1000);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("failed to bind " + url);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    protected void sortOids() {
        Collections.sort(oids, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String[] o1s = o1.split("\\.");
                String[] o2s = o2.split("\\.");

                int len = Math.min(o1s.length, o2s.length);
                for(int i=0; i<len; i++){
                    if(Integer.parseInt(o1s[i]) > Integer.parseInt(o2s[i])){
                        return 1;
                    }
                    if(Integer.parseInt(o1s[i]) < Integer.parseInt(o2s[i])){
                        return -1;
                    }
                }
                if(o1s.length > o2s.length){
                    return 1;
                }
                else if(o1s.length < o2s.length){
                    return -1;
                }
                return 0;
            }
        });
    }

    public void startManager(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = /*"udp:" + */localIP + "/" + trapPort;
                try {
                    Snmp snmp = new Snmp(new DefaultUdpTransportMapping(new UdpAddress(url)));
                    snmp.addCommandResponder(SnmpV2Entity.this);
                    snmp.listen();
                    System.out.println("start listening on " + url);
                    while (true) {
                        Thread.sleep(60*60*1000);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("failed to bind " + url);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 用于处理snmp get, set, getnext, getbulk等消息
     */
    public void processPdu(CommandResponderEvent respEvnt) {
        if (respEvnt == null || respEvnt.getPDU() == null) {
            System.out.println("receive null");
            return;
        }

        final PDU pdu = respEvnt.getPDU();
        printReceiveMsg(pdu);
        switch (pdu.getType()) {
            case PDU.SET:
                processSetRequest(pdu);
                break;
            case PDU.GET:
                processGetRequest(pdu);
                break;
            case PDU.GETNEXT:
                processGetNextRequest(pdu);
                break;
            case PDU.GETBULK:
                processGetBulkRequest(pdu);
                break;
            case PDU.TRAP:
                processTrapRequest(pdu);
                break;
            default:
                break;
        }

        try {
            pdu.setType(PDU.RESPONSE);
            pdu.setErrorIndex(0);
            pdu.setErrorStatus(0);
            System.out.println("Send Response!!");
            respEvnt.getMessageDispatcher().returnResponsePdu(
                    respEvnt.getMessageProcessingModel(),
                    respEvnt.getSecurityModel(),
                    respEvnt.getSecurityName(),
                    respEvnt.getSecurityLevel(),
                    pdu,
                    respEvnt.getMaxSizeResponsePDU(),
                    respEvnt.getStateReference(),
                    new StatusInformation());
        } catch (MessageException e) {
            e.printStackTrace();
        }
    }

    protected void processTrapRequest(PDU pdu) {
        printReceiveMsg(pdu);
    }

    protected void processSetRequest(PDU pdu) {
    }

    public void processGetRequest(PDU pdu) {
        for (int i = 0; i < pdu.size(); i++) {
            OID oid = pdu.get(i).getOid();
            Object value = p.get(oid.toString());
            pdu.set(i, new VariableBinding(oid, new OctetString(value == null ? "" : value.toString())));
        }
    }

    protected void processGetBulkRequest(PDU pdu) {
        int maxRepeats = pdu.getMaxRepetitions();
        int nonRepeats = pdu.getNonRepeaters();

        for (int i = 0; i < pdu.size(); i++) {
            OID oid = pdu.get(i).getOid();
            Object value = p.get(oid.toString());
            pdu.set(i, new VariableBinding(oid, new OctetString(value == null ? "" : value.toString())));
        }

        Map<OID, VariableBinding> map = new HashMap<OID, VariableBinding>();

        for (int i = Math.min(pdu.size(), nonRepeats); i < pdu.size(); i++) {
            int count = 0;
            OID oid = pdu.get(i).getOid();
            while (count < maxRepeats) {
                int index = oids.indexOf(oid.toString());
                if (index != -1 && index < oids.size() - 1) {
                    oid = new OID(oids.get(index + 1));
                    Object value = p.get(oid.toString());
                    map.put(oid, new VariableBinding(oid, new OctetString(value == null ? "" : value.toString())));
                }
                count++;
            }
        }
        pdu.clear();
        pdu.addAll(map.values().toArray(new VariableBinding[map.values().size()]));
    }

    protected void processGetNextRequest(PDU pdu) {
        for (int i = 0; i < pdu.size(); i++) {
            OID oid = pdu.get(i).getOid();
            int index = oids.indexOf(oid.toString());
            if (index != -1 && index < oids.size()) {
                oid = new OID(oids.get((index + 1) % oids.size()));
                Object value = p.get(oid.toString());
                pdu.set(i, new VariableBinding(oid, new OctetString(value == null ? "" : value.toString())));
            } else {
                System.out.println("not found " + oid.toString());
            }
        }
    }

    public void sendTrap(List<VariableBinding> list, String remoteUrl) throws IOException {
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version2c, PDU.TRAP);
        for (VariableBinding vb : list) {
            pdu.add(vb);
        }
        Address managerAddress = new UdpAddress(remoteUrl);
        Target target = Snmp4JHelper.createCommunityTarget(managerAddress, trapCommunity);
        Snmp snmp = Snmp4JHelper.createSnmpSession(managerAddress);
        sendRequest(snmp, pdu, target);
    }

    public void sendRequest(PDU pdu, String remoteUrl, String commnuity) throws IOException {
        Target target = Snmp4JHelper.createCommunityTarget(new UdpAddress(remoteUrl), commnuity);
        sendRequest(snmp, pdu, target);
    }

    protected void sendRequest(Snmp snmp, PDU pdu, Target target) throws IOException {
        ResponseEvent respEvnt = snmp.send(pdu, target);

        if (respEvnt != null && respEvnt.getResponse() != null) {
            if (respEvnt.getResponse() == null) {
                System.out.println("TimeOut...");
            } else {
                printReceiveMsg(respEvnt.getResponse());
            }
        }
    }

    protected void printReceiveMsg(PDU pdu) {
        if (pdu.getErrorStatus() == PDU.noError) {
            System.out.println(String.format("receive %s message:", SnmpType.type(pdu.getType())));
            Vector<VariableBinding> getVBs = pdu.getVariableBindings();
            for (VariableBinding vb : getVBs) {
                System.out.println(vb.getOid() + " : " + vb.getVariable());
            }
        } else {
            System.out.println("Error:" + pdu.getErrorStatusText());
        }
    }

    private String parseIp(CommandResponderEvent respEvnt) {
        return ((Inet4Address) ((UdpAddress) respEvnt.getPeerAddress()).getInetAddress()).getHostAddress();
    }

    enum SnmpType {
        Get(PDU.GET),
        Set(PDU.SET),
        RESPONSE(PDU.RESPONSE),
        GETBULK(PDU.GETBULK),
        INFORM(PDU.INFORM),
        TRAP(PDU.TRAP),
        NOTIFICATION(PDU.NOTIFICATION),
        REPORT(PDU.REPORT),
        GETNEXT(PDU.GETNEXT);

        int type;

        SnmpType(int type) {
            this.type = type;
        }

        public static String type(int type) {
            for (SnmpType one : values()) {
                if (one.type == type) {
                    return one.toString();
                }
            }
            return String.valueOf(type);
        }
    }

    protected Map<String, Variable> toMap(PDU pdu) {
        Vector<VariableBinding> vbs = pdu.getVariableBindings();

        Map<String, Variable> map = new HashMap<String, Variable>();
        for (VariableBinding vb : vbs) {
            map.put(vb.getOid().toString(), vb.getVariable());
        }
        return map;
    }
}
