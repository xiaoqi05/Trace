package cuitx.edu.com.trade.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UploadXmlService extends IntentService {


    public UploadXmlService() {
        super("uploadXml");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("time","time up");

    }
}
