package com.mw.omc.tool.snmp.simulator;

import org.snmp4j.PDU;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 00054054 on 2016/09/08.
 */
public class S220Simulator extends SnmpV2Entity {
    private String trapTarget = "10.86.95.16/162";

    public S220Simulator(final String localIP, String storageFile) throws IOException {
        super(localIP, storageFile);
        scheduleAlarm();
    }

    public void processSetRequest(PDU pdu) {
    }

    private void scheduleAlarm() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            }
        }, 1000, 60000);
    }

    public static void main(String[] args) throws IOException {
        S220Simulator simulator = new S220Simulator("10.86.38.34", "MibS220.properties");
        simulator.startAgent();
    }
}