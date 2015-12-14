package cuitx.edu.com.trade.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cuitx.edu.com.trade.activities.MainActivity;


public class BootBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent mainActivityIntent = new Intent(context, MainActivity.class);
			mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(mainActivityIntent);
		}
	}
	
}