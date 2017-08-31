package com.mw.omc.tool.snmp;

/**
 * Created by 00054054 on 2015/09/09.
 */

import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

import java.io.IOException;
import java.util.List;
import java.util.Vector;


public class SnmpV3Get {
    public static final String USER_NAMME = "zte";
    public static final String AUTH_PWD = "ztezteztezte";
    public static final String PRIV_PWD = "ztezteztezte";
    public static final String TARGET_ADDR = "10.86.38.41/161";
    public static final String CONTEXT_ENGINE_ID = "";
    public static final String CONTEXT_NAME = "";

    public static void main(String[] args) throws IOException, InterruptedException {
        Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);
        snmp.listen();

        // Add User
        UsmUser user = new UsmUser(
                new OctetString(USER_NAMME),
                AuthMD5.ID, new OctetString(AUTH_PWD),
                PrivDES.ID, new OctetString(PRIV_PWD));
        //If the specified SNMP engine id is specified, this user can only be used with the specified engine ID
        //So if it's not correct, will get an error that can't find a user from the user table.
        //snmp.getUSM().addUser(new OctetString("nmsAdmin"), new OctetString("0002651100"), user);
        snmp.getUSM().addUser(new OctetString(USER_NAMME), user);
        Target target = Snmp4JHelper.createUseryTarget(new UdpAddress(TARGET_ADDR), USER_NAMME);
        sendRequest(snmp, createGetPdu(), target);
    }

    private static PDU createGetPdu() {
        PDU pdu = Snmp4JHelper.createPDU(SnmpConstants.version3, PDU.GET);
        pdu.setType(PDU.GET);
//        pdu.setContextEngineID(new OctetString(CONTEXT_ENGINE_ID));    //if not set, will be SNMP engine id
//        pdu.setContextName(new OctetString(CONTEXT_NAME));  //must be same as SNMP agent
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.1.0"))); //sysDesc
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.5.0"))); //sysName
        return pdu;
    }

    private static void sendRequest(Snmp snmp, PDU pdu, Target target) throws IOException {
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

    private static void snmpWalk(Snmp snmp, UserTarget target, OctetString contextEngineId) {
        TableUtils utils = new TableUtils(snmp,
                new MyDefaultPDUFactory(PDU.GETNEXT, //GETNEXT or GETBULK)
                        contextEngineId)
        );
        utils.setMaxNumRowsPerPDU(5);   //only for GETBULK, set max-repetitions, default is 10
        OID[] columnOids = new OID[]{
                new OID("1.3.6.1.2.1.1.9.1.2"), //sysORID
                new OID("1.3.6.1.2.1.1.9.1.3")  //sysORDescr
        };
        // If not null, all returned rows have an index in a range (lowerBoundIndex, upperBoundIndex]
        List<TableEvent> l = utils.getTable(target, columnOids, new OID("3"), new OID("10"));
        for (TableEvent e : l) {
            System.out.println(e);
        }
    }

    private static class MyDefaultPDUFactory extends DefaultPDUFactory {
        private OctetString contextEngineId = null;

        public MyDefaultPDUFactory(int pduType, OctetString contextEngineId) {
            super(pduType);
            this.contextEngineId = contextEngineId;
        }

        @Override
        public PDU createPDU(Target target) {
            PDU pdu = super.createPDU(target);
            if (target.getVersion() == SnmpConstants.version3) {
                ((ScopedPDU) pdu).setContextEngineID(contextEngineId);
            }
            return pdu;
        }
    }
}
