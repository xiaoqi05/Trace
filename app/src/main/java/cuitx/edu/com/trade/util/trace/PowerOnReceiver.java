package cuitx.edu.com.trade.util.trace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import cuitx.edu.com.trade.activities.MainActivity;


public class PowerOnReceiver extends BroadcastReceiver {
	 SharedPreferences sp;
	@Override
	public void onReceive(Context context, Intent intent) {
		sp = context.getSharedPreferences("myTrace",Context.MODE_PRIVATE);
		SharedPreferences.Editor ed = sp.edit();
		ed.putBoolean("powerState",true);
		ed.commit();
		Intent intents = new Intent(context, MainActivity.class);
		intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intents);
		sp.edit().clear().commit();
	}
	
}