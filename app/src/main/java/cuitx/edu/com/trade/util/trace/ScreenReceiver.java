package cuitx.edu.com.trade.util.trace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;

import java.util.ArrayList;

import cuitx.edu.com.trade.bean.KeyValue;


public class ScreenReceiver  {
	long startTime;
	boolean screenStatus;
	long sustainTime,endTime;
	boolean timeStatus;
	Context context;
	XML xml;

	public ScreenReceiver(Context context){
		this.context= context;
		xml = new XML();
	}

	private void writeXml() {
		ArrayList<KeyValue> list = new ArrayList<KeyValue>();

		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String id = tm.getDeviceId();
		KeyValue key2 = new KeyValue();
		key2.setKey("IMEI");
		key2.setValue(id);
		list.add(key2);

		KeyValue key = new KeyValue();
		key.setKey("screen");
		key.setValue(screenStatus+"");
		list.add(key);

		if(xml.genNewFile()){
			System.out.println("写入屏幕状态信息");
			xml.WriteXMLFile(xml.genXMLString("screen", list));
		}

	}
	/**
	 *
	 * 当屏幕状态改变时自动写入相应状态信息
	 */
	public void initBroadcastReceiver(){
		IntentFilter inFilter = new IntentFilter();
		inFilter.addAction(Intent.ACTION_SCREEN_ON);
		inFilter.addAction(Intent.ACTION_SCREEN_OFF);

		BroadcastReceiver broadcastReceivers = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(Intent.ACTION_SCREEN_ON)) {
					//startTime = System.currentTimeMillis();
					screenStatus = true;
					System.out.println("屏幕亮");
					writeXml();
				} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
					System.out.println("屏幕灭");
					screenStatus = false;
					writeXml();

				}

			}

		};
		// 动态注册广播接收者
		context.registerReceiver(broadcastReceivers, inFilter);
	}




}
	

