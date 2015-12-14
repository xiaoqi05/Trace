package cuitx.edu.com.trade.util.trace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import java.util.ArrayList;

import cuitx.edu.com.trade.bean.KeyValue;
import cuitx.edu.com.trade.service.UploadXmlService;


public class NetworkConnectionReceiver extends BroadcastReceiver {

    ConnectivityStatus cs;
    Context context;
    ArrayList<KeyValue> network;

    @Override
    public void onReceive(Context context, Intent intent) {

        XML xml = new XML();

        ArrayList<KeyValue> network = new ArrayList<>();

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String userID = tm.getDeviceId();
        ConnectivityStatus cs = new ConnectivityStatus(context.getApplicationContext());


        KeyValue userKeyValue = new KeyValue();
        userKeyValue.setKey("IMEI");
        userKeyValue.setValue(userID);
        network.add(userKeyValue);

        KeyValue connKeyValue = new KeyValue();
        connKeyValue.setKey("Connected");
        connKeyValue.setValue(String.valueOf(cs.isConnected()));
        network.add(connKeyValue);

        if (cs.isOpenNetwork()) {
            KeyValue cellularKeyValue = new KeyValue();
            cellularKeyValue.setKey("Mobile");
            cellularKeyValue.setValue(String.valueOf(cs.isOpenNetwork()));
            network.add(cellularKeyValue);
            if (!"".equals(cs.getTypeInfo())) {
                KeyValue cell = new KeyValue();
                String MobileType = cs.getTypeInfo();
                cell.setKey("MobileType");
                cell.setValue(MobileType);
                System.out.println("MobileType>>>>>>" + MobileType);
                network.add(cell);
            } else {
                KeyValue cell = new KeyValue();
                String MobileType = cs.getTypeInfo();
                cell.setKey("MobileType");
                cell.setValue("unknown");
                System.out.println("MobileType>>>>>>" + MobileType);
                network.add(cell);
            }
        } else {
            KeyValue cellularKeyValue = new KeyValue();
            cellularKeyValue.setKey("Mobile");
            cellularKeyValue.setValue(String.valueOf(cs.isOpenNetwork()));
            network.add(cellularKeyValue);

            KeyValue cell = new KeyValue();
            String MobileType = cs.getTypeInfo();
            cell.setKey("MobileType");
            cell.setValue("unknown");
            System.out.println("MobileType>>>>>>" + MobileType);
            network.add(cell);
        }

        if (cs.isOpenWifi()) {
            KeyValue typeKeyValue = new KeyValue();
            typeKeyValue.setKey("WiFi");
            typeKeyValue.setValue(String.valueOf(cs.isOpenWifi()));
            network.add(typeKeyValue);
            if (!cs.getSSID().equals("")) {
                KeyValue ssidKeyValue = new KeyValue();
                ssidKeyValue.setKey("SSID");
                ssidKeyValue.setValue(cs.getSSID());
                network.add(ssidKeyValue);
            } else {
                KeyValue ssidKeyValue = new KeyValue();
                ssidKeyValue.setKey("SSID");
                ssidKeyValue.setValue("unknown");
                network.add(ssidKeyValue);
            }
        } else {
            KeyValue typeKeyValue = new KeyValue();
            typeKeyValue.setKey("WiFi");
            typeKeyValue.setValue(String.valueOf(cs.isOpenWifi()));
            network.add(typeKeyValue);

            KeyValue ssidKeyValue = new KeyValue();
            ssidKeyValue.setKey("SSID");
            ssidKeyValue.setValue("unknown");
            network.add(ssidKeyValue);
        }

        if (xml.genNewFile())
            xml.WriteXMLFile(xml.genXMLString("network", network));

        if (cs.isOpenWifi()) {
            Intent it = new Intent(context, UploadXmlService.class);
            context.startService(it);
        }
    }

}