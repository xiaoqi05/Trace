package cuitx.edu.com.trade.util.trace;

import android.content.Context;
import android.net.TrafficStats;

public class TrafficStat {

    private long thisMobileRP;
    private long thisMobileRB;
    private long thisMobileTP;
    private long thisMobileTB;

    private long thisWifiRP;
    private long thisWifiRB;
    private long thisWifiTP;
    private long thisWifiTB;



    public TrafficStat(Context context) {

        //当前手机发送和接受的数据包信息
        thisMobileRP = TrafficStats.getMobileRxPackets();
        thisMobileRB = TrafficStats.getMobileRxBytes();
        thisMobileTP = TrafficStats.getMobileTxPackets();
        thisMobileTB = TrafficStats.getMobileTxBytes();

        //当前手机wifi发送和接受的数据包信息，无法直接得到wifi的信息，只能通过使用总的流量包减去手机蜂窝网产生的数据包
        thisWifiRP = TrafficStats.getTotalRxPackets() - TrafficStats.getMobileRxPackets();
        thisWifiRB = TrafficStats.getTotalRxBytes() - TrafficStats.getMobileRxBytes();
        thisWifiTP = TrafficStats.getTotalTxPackets() - TrafficStats.getMobileTxPackets();
        thisWifiTB = TrafficStats.getTotalTxBytes() - TrafficStats.getMobileTxBytes();
    }

    //所有数据包的数据都表示的是两个周期之前产生的流量
    //特殊处理，当关机后，Traffic的信息将会清零，当重新开机以后，周期计时也会重新开始
    //所以特殊情况的处理应该是：当关机后记录当前的数据包信息减去SharedPreferences中保存的上一次的数据包信息，得到的时这次周期开始到关机这个时间段的数据包信息，并把这个信息以trace的形式写入xml
    //开机后检查是否有开机的情况，如果有，则直接写入当前得到的数据包信息
    public String getMobileRecPacket() {
        System.out.println("getMobileRecPacket()" + String.valueOf(thisMobileRP));
        return String.valueOf(thisMobileRP);
    }

    public String getMobileRecByte() {
        System.out.println("getMobileRecByte()" + String.valueOf(thisMobileRB));
        return String.valueOf(thisMobileRB / 1024);
    }

    public String getMobileTransPacket() {
        System.out.println("getMobileTransPacket()" + String.valueOf(thisMobileTP));
        return String.valueOf(thisMobileTP);
    }

    public String getMobileTransByte() {
        System.out.println("getMobileTransByte()" + String.valueOf(thisMobileTB));
        return String.valueOf(thisMobileTB / 1024);
    }

    public String getWiFiRecPacket() {
        System.out.println("getWiFiRecPacket()" + String.valueOf(thisWifiRP));
        return String.valueOf(thisWifiRP);
    }

    public String getWiFiRecByte() {
        System.out.println("getWiFiRecByte()" + String.valueOf(thisWifiRB));
        return String.valueOf(thisWifiRB / 1024);
    }

    public String getWiFiTransPacket() {
        System.out.println("getWiFiTransPacket()" + String.valueOf(thisWifiTP));
        return String.valueOf(thisWifiTP);
    }

    public String getWiFiTransByte() {
        System.out.println("getWiFiTransByte()" + String.valueOf(thisWifiTB));
        return String.valueOf(thisWifiTB / 1024);
    }

}