/*package utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public class ConnectivityStatus {
	
	private NetworkInfo activeNetwork;
	private WifiManager wm;
	
	public ConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		activeNetwork = cm.getActiveNetworkInfo();
	}
	
	public boolean isConnected () {
		boolean status = activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
		return status;
	}
	
	public boolean isMobile() {
		boolean type = false;
		if (isConnected())
			type = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
		return type;
	}
	
	public String getCellularType () {
		int subType = activeNetwork.getSubtype();
		switch(subType) {
        case TelephonyManager.NETWORK_TYPE_1xRTT:
            return "1xRTT"; // ~ 50-100 kbps
        case TelephonyManager.NETWORK_TYPE_CDMA:
            return "CDMA"; // ~ 14-64 kbps
        case TelephonyManager.NETWORK_TYPE_EDGE:
            return "EDGE"; // ~ 50-100 kbps
        case TelephonyManager.NETWORK_TYPE_EVDO_0:
            return "EVDO_0"; // ~ 400-1000 kbps
        case TelephonyManager.NETWORK_TYPE_EVDO_A:
            return "EVDO_A"; // ~ 600-1400 kbps
        case TelephonyManager.NETWORK_TYPE_GPRS:
            return "GPRS"; // ~ 100 kbps
        case TelephonyManager.NETWORK_TYPE_HSDPA:
            return "HSDPA"; // ~ 2-14 Mbps
        case TelephonyManager.NETWORK_TYPE_HSPA:
            return "HSPA"; // ~ 700-1700 kbps
        case TelephonyManager.NETWORK_TYPE_HSUPA:
            return "HSUPA"; // ~ 1-23 Mbps
        case TelephonyManager.NETWORK_TYPE_UMTS:
            return "UMTS"; // ~ 400-7000 kbps
        case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11 
            return "EHRPD"; // ~ 1-2 Mbps
        case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
            return "EVDO_B"; // ~ 5 Mbps
        case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
            return "HSPAP"; // ~ 10-20 Mbps
        case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
            return "IDEN"; // ~25 kbps 
        case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
            return "LTE"; // ~ 10+ Mbps
        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
        	return "unknow";
        default:
            return "unknow";
        }
	}
	
	public boolean isWiFi () {
		boolean type = false;
		if (isConnected())
			type = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
		return type;
	}
	
	public String getSSID() {
		return wm.getConnectionInfo().getSSID();
	}
}*/
package cuitx.edu.com.trade.util.trace;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class ConnectivityStatus {
	
	private NetworkInfo activeNetwork;
	private WifiManager wm;
	private Context context;
	ConnectivityManager connectivity;
	
	@SuppressLint("NewApi")
	public ConnectivityStatus(Context context) {
		this.context = context;
		wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	}
	
	public String  getTypeInfo(){
		if (isOpenNetwork()) {
			activeNetwork = connectivity.getActiveNetworkInfo();
			return activeNetwork.getSubtypeName();
		}
		else return null;
		
	}
	
	public String getSSID() {
		String ssid = wm.getConnectionInfo().getSSID();
		System.out.println("ssid>>>>>>>>>>>>>>>>>>"+ssid);
		return ssid;
	}


	public boolean isOpenNetwork() {
		 connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}
	

		public boolean isOpenWifi() {
			ConnectivityManager connManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWifi = connManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			return mWifi.isConnected();
		}
	
	
		public boolean isConnected () {
			boolean status = isOpenNetwork()||isOpenWifi();
			return status;
		}
	
	
}