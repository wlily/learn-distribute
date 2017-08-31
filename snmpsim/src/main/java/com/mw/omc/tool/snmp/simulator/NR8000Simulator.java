package com.mw.omc.tool.snmp.simulator;

import com.mw.omc.tool.ftp.FTPUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.snmp4j.PDU;
import org.snmp4j.smi.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by 00054054 on 2016/09/08.
 */
public class NR8000Simulator  extends SnmpV2Entity {
    private String trapTarget = "10.86.99.178/162";
    private String neCfg = "nr8250v2040213.xml";

    public NR8000Simulator(final String localIP, String storageFile, String trapTarget, String neCfg) throws IOException {
        super(localIP, storageFile);
        this.neCfg = neCfg;
        this.trapTarget = trapTarget;
        scheduleAlarm();
    }

    public void processSetRequest(PDU pdu) {
        final Map<String, Variable> vb = toMap(pdu);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (vb.containsKey("1.3.6.1.4.1.3902.2400.1.1.1.1.3.1")) {
                    reverse(vb);
                }
            }
        }).start();
    }

    private void reverse(Map<String, Variable> vb) {
        String id = vb.get("1.3.6.1.4.1.3902.2400.1.1.1.1.3.1").toString();
        String ipAddress = vb.get("1.3.6.1.4.1.3902.2400.1.1.1.1.3.2").toString();
        String userName = vb.get("1.3.6.1.4.1.3902.2400.1.1.1.1.3.3").toString();
        String userPwd = vb.get("1.3.6.1.4.1.3902.2400.1.1.1.1.3.4").toString();
        String port = vb.get("1.3.6.1.4.1.3902.2400.1.1.1.1.3.5").toString();
        String ftpPath = vb.get("1.3.6.1.4.1.3902.2400.1.1.1.1.3.6").toString();
        String seq = vb.get("1.3.6.1.4.1.3902.2400.1.1.1.1.3.7").toString();

        System.out.println(String.format("%s %s %s", ipAddress, userName, userPwd));

        try {
            String localFile = new File(neCfg).getAbsolutePath();
            FTPClient ftpClient = FTPUtil.getFTPClient(ipAddress, userName, userPwd, 21);
            FTPUtil.putFile(ftpClient, ftpPath, localFile);
            ftpClient.disconnect();

            try {
                List<VariableBinding> list = new ArrayList<VariableBinding>();
                list.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.14.1.4.1"), new OctetString(seq)));
                list.add(new VariableBinding(new OID(".1.3.6.1.4.1.3902.2400.1.1.9.1.14.1.4.2"), new Integer32(0)));
                sendTrap(list, trapTarget);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scheduleAlarm() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            }
        }, 1000, 60000);
    }

    protected void processGetBulkRequest(PDU pdu) {
        List<VariableBinding> list = new ArrayList<VariableBinding>();

        for(int j=0; j<oids.size(); j++){
            for (int i = 0; i < pdu.size(); i++) {
                String tmpOid = pdu.get(i).getOid().toString();
                String oid = oids.get(j);
                if(oid.startsWith(tmpOid.replace(".0","")) && !oid.equals(tmpOid)){
                    Object value = p.get(oid);
                    list.add(new VariableBinding(new OID(oid), new OctetString(value == null ? "" : value.toString())));
                }
            }
        }
        int requestSize = pdu.size();
        int groupSize = list.size()/requestSize;
        pdu.getVariableBindings().clear();

        for(int i=0; i<groupSize; i++){
            for(int j=0; j<requestSize; j++){
                pdu.add(list.get(groupSize*j+i));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Properties property = new Properties();
        property.load(new FileInputStream(new File("NR8000Cfg.properties")));
        String neIp = property.get("neip").toString();
        String neMib = property.get("nemib").toString();
        String trapTarget = property.get("traptarget").toString();
        String neCfg = property.get("necfg").toString();

        NR8000Simulator simulator = new NR8000Simulator(neIp, neMib, trapTarget, neCfg);
        simulator.startAgent();
    }

}