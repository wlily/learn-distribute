package com.mw.omc.tool.snmp.simulator;

import com.mw.omc.tool.ftp.FTPUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.snmp4j.PDU;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by 00054054 on 2016/09/01.
 */
public class PR10Simulator extends SnmpV2Entity {
    private String trapTarget = "10.86.99.178/162";
    private String neCfg = "Config.dat";

    public PR10Simulator(final String localIP, String storageFile, String trapTarget, String neCfg) throws IOException {
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
                if (vb.containsKey("1.3.6.1.4.1.3902.8000.1.41.4.1.0")) {
                    reverse(vb);
                }
            }
        }).start();
    }

    private void reverse(Map<String, Variable> vb) {
        String ipAddress = vb.get("1.3.6.1.4.1.3902.8000.1.41.4.1.0").toString();
        String userName = vb.get("1.3.6.1.4.1.3902.8000.1.41.4.2.0").toString();
        String userPwd = vb.get("1.3.6.1.4.1.3902.8000.1.41.4.3.0").toString();

        System.out.println(String.format("%s %s %s", ipAddress, userName, userPwd));

        try {
            String localFile = new File(neCfg).getAbsolutePath();
            FTPClient ftpClient = FTPUtil.getFTPClient(ipAddress, userName, userPwd, 21);
            FTPUtil.putFile(ftpClient, "", localFile);
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scheduleAlarm() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<VariableBinding> list = new ArrayList<VariableBinding>();
                list.add(new VariableBinding(new OID(".1.1.1.1"), new OctetString("xxx")));
                list.add(new VariableBinding(new OID(".1.1.1.2"), new OctetString("yyy")));

                try {
                     sendTrap(list, trapTarget);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1000, 60000);
    }

    public static void main(String[] args) throws IOException {
        Properties property = new Properties();
        property.load(new FileInputStream(new File("PR10Cfg.properties")));
        String neIp = property.get("neip").toString();
        String neMib = property.get("nemib").toString();
        String trapTarget = property.get("traptarget").toString();
        String neCfg = property.get("necfg").toString();

        PR10Simulator simulator = new PR10Simulator(neIp, neMib, trapTarget, neCfg);
        simulator.startAgent();
    }
}
