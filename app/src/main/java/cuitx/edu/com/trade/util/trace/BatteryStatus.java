package cuitx.edu.com.trade.util.trace;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class BatteryStatus {
	private Intent batteryStatus;
	
	public BatteryStatus(Context context) {
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		batteryStatus = context.registerReceiver(null, ifilter);
	}
	
	public boolean isCharging() {
		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		if (status == BatteryManager.BATTERY_STATUS_CHARGING)
			return true;
		else
			return false;
	}
	
	public boolean isFull() {
		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		if (status == BatteryManager.BATTERY_STATUS_FULL)
			return true;
		else
			return false;
	}
	
	public String getBatteryLevel() {
		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		float batteryPct = level / (float)scale;
		
		return String.valueOf(batteryPct);
	}
	
	public String getPlug() {
		String res = "";
		int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
		switch (chargePlug) {
		case BatteryManager.BATTERY_PLUGGED_USB:
			res = "USB";
			break;
		case BatteryManager.BATTERY_PLUGGED_AC:
			res = "AC";
			break;
		}
        return res;
	}
}