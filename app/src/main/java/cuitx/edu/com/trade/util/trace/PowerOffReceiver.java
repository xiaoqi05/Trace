package cuitx.edu.com.trade.util.trace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.TrafficStats;
import android.telephony.TelephonyManager;

import java.util.ArrayList;

import cuitx.edu.com.trade.bean.KeyValue;


public class PowerOffReceiver extends BroadcastReceiver {
	private long mobileRP;
	private long mobileRB;
	private long mobileTP;
	private long mobileTB;
	private long wifiRP;
	private long wifiRB;
	private long wifiTP;
	private long wifiTB;

	private SharedPreferences sp;
	@Override
	public void onReceive(Context context, Intent intent) {
		XML xml = new XML();
		System.out.println("关机广播");
		sp = context.getSharedPreferences("myTrace",Context.MODE_PRIVATE);
		sp.edit().putBoolean("isPowerOff",true).commit();
		mobileRP = TrafficStats.getMobileRxPackets()-sp.getLong("lastMobileRP", 0);
		mobileRB = TrafficStats.getMobileRxBytes()  -sp.getLong("lastMobileRB", 0);
		mobileTP = TrafficStats.getMobileTxPackets()-sp.getLong("lastMobileTP", 0);
		mobileTB = TrafficStats.getMobileTxBytes() -sp.getLong("lastMobileTB", 0);

		wifiRP = TrafficStats.getTotalRxPackets() - TrafficStats.getMobileRxPackets()-sp.getLong("lastWifiRP", 0);
		wifiRB = (TrafficStats.getTotalRxBytes() - TrafficStats.getMobileRxBytes()) -sp.getLong("lastWifiRB", 0);
		wifiTP = TrafficStats.getTotalTxPackets() - TrafficStats.getMobileTxPackets()-sp.getLong("lastWifiTP", 0);
		wifiTB = (TrafficStats.getTotalTxBytes() - TrafficStats.getMobileTxBytes())-sp.getLong("lastWifiTB", 0);
		System.out.println("mobileRP---------------"+mobileRP+"mobileRB------------------"+mobileRB);
		System.out.println("mobileTP---------------"+mobileTP+"mobileTB------------------"+mobileTB);
		System.out.println("wifiRP---------------"+wifiRP+"wifiRB------------------"+wifiRB);
		System.out.println("wifiTP---------------"+wifiTP+"wifiTB------------------"+wifiTB);
		ArrayList<KeyValue> traffic = new ArrayList<KeyValue>();

		TelephonyManager tm =(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String userID = tm.getDeviceId();

		KeyValue userKeyValue = new KeyValue();
		userKeyValue.setKey("IMEI");
		userKeyValue.setValue(userID);
		traffic.add(userKeyValue);

		KeyValue connKeyValue = new KeyValue();
		connKeyValue.setKey("MobileRXPackets");
		connKeyValue.setValue(String.valueOf(mobileRP));
		System.out.println("mobileRP---------------"+String.valueOf(mobileRP)+"mobileRB------------------"+String.valueOf(mobileRB/1024));
		traffic.add(connKeyValue);

		KeyValue connKeyValue2 = new KeyValue();
		connKeyValue2.setKey("MobileRXBytes");
		connKeyValue2.setValue(String.valueOf(mobileRB/1024));
		traffic.add(connKeyValue2);

		KeyValue connKeyValue3 = new KeyValue();
		connKeyValue3.setKey("MobileTXPackets");
		connKeyValue3.setValue(String.valueOf(mobileTP));
		traffic.add(connKeyValue3);

		KeyValue connKeyValue4 = new KeyValue();
		connKeyValue4.setKey("MobileTXBytes");
		connKeyValue4.setValue(String.valueOf(mobileTB/1024));
		traffic.add(connKeyValue4);

		KeyValue connKeyValue5 = new KeyValue();
		connKeyValue5.setKey("WiFiRXPackets");
		connKeyValue5.setValue(String.valueOf(wifiRP));
		traffic.add(connKeyValue5);

		KeyValue connKeyValue6 = new KeyValue();
		connKeyValue6.setKey("WiFiRXBytes");
		connKeyValue6.setValue(String.valueOf(wifiRB/1024));
		traffic.add(connKeyValue6);

		KeyValue connKeyValue7 = new KeyValue();
		connKeyValue7.setKey("WiFiTXPackets");
		connKeyValue7.setValue(String.valueOf(wifiTP));
		traffic.add(connKeyValue7);

		KeyValue connKeyValue8 = new KeyValue();
		connKeyValue8.setKey("WiFiTXBytes");
		connKeyValue8.setValue(String.valueOf(wifiTB/1024));
		traffic.add(connKeyValue8);

		System.out.println("WiFiRXPackets---------------"+String.valueOf(wifiRP)+"WiFiRXBytes-----------------"+String.valueOf(wifiRB/1024));

		if (xml.genNewFile())
			xml.WriteXMLFile(xml.genXMLString("traffic", traffic));

	}
}