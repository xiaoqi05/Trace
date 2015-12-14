package cuitx.edu.com.trade.service;

import android.R;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;

import java.util.ArrayList;

import cuitx.edu.com.trade.activities.MainActivity;
import cuitx.edu.com.trade.bean.KeyValue;
import cuitx.edu.com.trade.util.trace.BatteryStatus;
import cuitx.edu.com.trade.util.trace.ConnectivityStatus;
import cuitx.edu.com.trade.util.trace.ScreenReceiver;
import cuitx.edu.com.trade.util.trace.TrafficStat;
import cuitx.edu.com.trade.util.trace.XML;


/**
 * 周期性任务
 *
 * @author xiao
 */
public class TraceMonitorService extends Service {

    Handler handler = new Handler();
    int i = 0;
    BatteryStatus bs;
    ConnectivityStatus cs;
    XML xml;
    String userID;
    boolean status;
    boolean screenStatus;
    String key, value;
    long time1;
    long time2;

    @Override
    public IBinder onBind(Intent arg0) {
        // System.out.println("bind");
        return null;
    }

    @Override
    public void onCreate() {
        bs = new BatteryStatus(this);
        cs = new ConnectivityStatus(this);
        xml = new XML();
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        userID = tm.getDeviceId();
        status = false;
        screenStatus = true;
        initBroadcastReceiver();
        ScreenReceiver screenReceiver = new ScreenReceiver(
                getApplicationContext());
        screenReceiver.initBroadcastReceiver();
        super.onCreate();
    }

    public void initBroadcastReceiver() {

        IntentFilter inFilter = new IntentFilter();
        inFilter.addAction(Intent.ACTION_SCREEN_ON);
        inFilter.addAction(Intent.ACTION_SCREEN_OFF);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(Intent.ACTION_SCREEN_ON)) {
                    screenStatus = true;
                    System.out.println("s屏幕亮");
                } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                    screenStatus = false;
                    System.out.println("s屏幕灭");

                }

            }
        };
        // 动态注册广播接收者
        registerReceiver(broadcastReceiver, inFilter);

    }

    @Override
    public void onDestroy() {
        // System.out.println("destroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // System.out.println("start");
        if (!status) {
            status = true;
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    this);
            builder.setSmallIcon(R.drawable.ic_dialog_info);
            builder.setContentTitle("Trace Collecting");
            builder.setTicker("Collecting");
            Intent intentStart = new Intent(getApplicationContext(),
                    MainActivity.class);
            PendingIntent intentContent = PendingIntent.getActivity(this, 123,
                    intentStart, 1);
            builder.setContentIntent(intentContent);
            Notification notif = builder.build();
            this.startForeground(123, notif);
            handler.post(runnable);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    Runnable runnable = new Runnable() {
        /*
         * (non-Javadoc)
         *
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            time1 = System.currentTimeMillis();
            System.out.println("》》》》》》》》》》》》》开始写入》》》》》》》》》》"
                    + ((time1 - time2) / 1000));
            ArrayList<KeyValue> temp = new ArrayList<KeyValue>();

            // userid
            KeyValue keyValue = new KeyValue();
            keyValue.setKey("IMEI");
            keyValue.setValue(userID);
            temp.add(keyValue);
            // battery
            KeyValue keyValue0 = new KeyValue();
            String bat = bs.getBatteryLevel();
            keyValue0.setKey("BatteryLevel");
            keyValue0.setValue(bat);
            temp.add(keyValue0);

            KeyValue keyValue1 = new KeyValue();
            String isfull = String.valueOf(bs.isFull());
            keyValue1.setKey("Full");
            keyValue1.setValue(isfull);
            temp.add(keyValue1);

            KeyValue keyValue2 = new KeyValue();
            String Charging = String.valueOf(bs.isCharging());
            keyValue2.setKey("Charging");
            keyValue2.setValue(Charging);
            temp.add(keyValue2);

            if (bs.isCharging()) {
                KeyValue keyValue3 = new KeyValue();
                String plug = bs.getPlug();
                keyValue3.setKey("Plug");
                keyValue3.setValue(plug);
                temp.add(keyValue3);
            }

            // network
            KeyValue keyValue4 = new KeyValue();
            String connect = String.valueOf(cs.isConnected());
            System.out.println("连接状态" + connect);
            keyValue4.setKey("Connected");
            keyValue4.setValue(connect);
            temp.add(keyValue4);

            // mobile

            if (cs.isOpenNetwork()) {
                KeyValue keyValue5 = new KeyValue();
                String mobile = String.valueOf(cs.isOpenNetwork());
                keyValue5.setKey("Mobile");
                keyValue5.setValue(mobile);
                temp.add(keyValue5);
                if (!"".equals(cs.getTypeInfo())) {
                    String MobileType = cs.getTypeInfo();
                    KeyValue keyValue52 = new KeyValue();
                    keyValue52.setKey("MobileType");
                    keyValue52.setValue(MobileType);
                    temp.add(keyValue52);
                } else {
                    KeyValue keyValue52 = new KeyValue();
                    keyValue52.setKey("MobileType");
                    keyValue52.setValue("unknown");
                    temp.add(keyValue52);
                }
            } else {
                KeyValue keyValue5 = new KeyValue();
                String mobile = String.valueOf(cs.isOpenNetwork());
                keyValue5.setKey("Mobile");
                keyValue5.setValue(mobile);
                temp.add(keyValue5);

                KeyValue keyValue52 = new KeyValue();
                keyValue52.setKey("MobileType");
                keyValue52.setValue("unknown");
                temp.add(keyValue52);
            }

            if (cs.isOpenWifi()) {
                KeyValue keyValue17 = new KeyValue();
                keyValue17.setKey("WiFi");
                keyValue17.setValue(String.valueOf(cs.isOpenWifi()));
                temp.add(keyValue17);
                if (!cs.getSSID().equals("")) {
                    KeyValue keyValue51 = new KeyValue();
                    keyValue51.setKey("SSID");
                    keyValue51.setValue(cs.getSSID());
                    temp.add(keyValue51);
                }

            } else {
                KeyValue keyValue17 = new KeyValue();
                keyValue17.setKey("WiFi");
                keyValue17.setValue(String.valueOf(cs.isOpenWifi()));
                temp.add(keyValue17);
                KeyValue keyValue51 = new KeyValue();
                keyValue51.setKey("SSID");
                keyValue51.setValue("unknown");
                temp.add(keyValue51);
            }

            TrafficStat ts = new TrafficStat(getApplicationContext());
            // traffic
            KeyValue keyValue6 = new KeyValue();
            keyValue6.setKey("MobileRXBytes");
            keyValue6.setValue(ts.getMobileRecByte());
            temp.add(keyValue6);
            KeyValue keyValue7 = new KeyValue();
            keyValue7.setKey("MobileRXPackets");
            keyValue7.setValue(ts.getMobileRecPacket());
            temp.add(keyValue7);
            KeyValue keyValue8 = new KeyValue();
            keyValue8.setKey("MobileTXBytes");
            keyValue8.setValue(ts.getMobileTransByte());
            temp.add(keyValue8);
            KeyValue keyValue9 = new KeyValue();
            keyValue9.setKey("MobileTXPackets");
            keyValue9.setValue(ts.getMobileTransPacket());
            temp.add(keyValue9);

            KeyValue keyValue12 = new KeyValue();
            keyValue12.setKey("WiFiRXBytes");
            keyValue12.setValue(ts.getWiFiRecByte());
            temp.add(keyValue12);
            KeyValue keyValue13 = new KeyValue();
            keyValue13.setKey("WiFiRXPackets");
            keyValue13.setValue(ts.getWiFiRecPacket());
            temp.add(keyValue13);
            KeyValue keyValue14 = new KeyValue();
            keyValue14.setKey("WiFiTXBytes");
            keyValue14.setValue(ts.getWiFiTransByte());
            temp.add(keyValue14);
            KeyValue keyValue15 = new KeyValue();
            keyValue15.setKey("WiFiTXPackets");
            keyValue15.setValue(ts.getWiFiTransPacket());
            temp.add(keyValue15);

            KeyValue keyvalue16 = new KeyValue();
            keyvalue16.setKey("screen");
            keyvalue16.setValue(screenStatus + "");
            temp.add(keyvalue16);

            // write xml
            if (xml.genNewFile())
                time2 = System.currentTimeMillis();
            System.out.println("》》》》》》》》》》》》》》》》》》》》》》结束写入》》》》》》》》》》》》》》》》》》");
            xml.WriteXMLFile(xml.genXMLString("Periodic", temp));
            // periodically execution after 20 minute  s
            handler.postDelayed(this, 120000);

        }
    };
}