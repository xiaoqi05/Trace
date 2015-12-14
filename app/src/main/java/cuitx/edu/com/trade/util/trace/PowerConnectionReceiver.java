package cuitx.edu.com.trade.util.trace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import java.util.ArrayList;

import cuitx.edu.com.trade.bean.KeyValue;


public class PowerConnectionReceiver extends BroadcastReceiver {
	
	@Override
    public void onReceive(Context context, Intent intent) {
		
		XML xml = new XML();
		
		ArrayList<KeyValue> battery =  new ArrayList<KeyValue>();
		
		TelephonyManager tm =(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String userID = tm.getDeviceId();
		
		BatteryStatus bs = new BatteryStatus(context.getApplicationContext());
		
		KeyValue userKeyValue = new KeyValue();
    	userKeyValue.setKey("IMEI");
    	userKeyValue.setValue(userID);
    	battery.add(userKeyValue);
		
		KeyValue levelKeyValue = new KeyValue();
		levelKeyValue.setKey("BatteryLevel");
		levelKeyValue.setValue(bs.getBatteryLevel());
    	battery.add(levelKeyValue);
    	KeyValue fullKeyValue = new KeyValue();
    	fullKeyValue.setKey("Full");
    	fullKeyValue.setValue(String.valueOf(bs.isFull()));
    	battery.add(fullKeyValue);
    	KeyValue chKeyValue = new KeyValue();
    	chKeyValue.setKey("Charging");
    	chKeyValue.setValue(String.valueOf(bs.isCharging()));
    	battery.add(chKeyValue);
    	if (bs.isCharging()) {
    		KeyValue plugKeyValue = new KeyValue();
        	plugKeyValue.setKey("Plug");
        	plugKeyValue.setValue(bs.getPlug());
        	battery.add(plugKeyValue);
    	}
    	
    	if (xml.genNewFile())
    		xml.WriteXMLFile(xml.genXMLString("battery", battery));
		/*
		Intent batteryIntent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
    
        int chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
		
		Toast.makeText(
				context,
				"is Charging " + String.valueOf(isCharging) + " is USB "
						+ String.valueOf(usbCharge) + " is AC "
						+ String.valueOf(acCharge), Toast.LENGTH_LONG).show();
        */
    }
}