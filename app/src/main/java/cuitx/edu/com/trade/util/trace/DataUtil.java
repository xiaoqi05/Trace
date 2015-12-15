package cuitx.edu.com.trade.util.trace;


import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtil {
    public static String getCurrentTime() {
        @SuppressLint("SimpleDateFormat") 
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }
}