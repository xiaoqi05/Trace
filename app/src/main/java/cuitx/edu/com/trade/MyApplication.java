package cuitx.edu.com.trade;

import android.app.Application;
import android.content.Intent;

import cuitx.edu.com.trade.service.TraceMonitorService;


/**
 * @AUTHOR by 肖齐
 * Created by 2015/12/1 22:14.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Intent i = new Intent(this, TraceMonitorService.class);
        startService(i);
    }
}