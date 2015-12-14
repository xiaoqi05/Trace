package cuitx.edu.com.trade.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sina.cloudstorage.auth.AWSCredentials;
import com.sina.cloudstorage.auth.BasicAWSCredentials;
import com.sina.cloudstorage.services.scs.SCS;
import com.sina.cloudstorage.services.scs.SCSClient;
import com.sina.cloudstorage.services.scs.model.Bucket;

import java.util.List;

import cuitx.edu.com.trade.R;

public class MainActivity extends AppCompatActivity {
    private SCS conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String accessKey = "sqt3t2Yg49KXqWvwrRxj";
        String secretKey = "d155aeb9cc905b122333047e77536e2913f8d304";
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        conn = new SCSClient(credentials);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getAllBuckets();
            }
        }).start();

    }
    public void getAllBuckets(){
        List<Bucket> list = conn.listBuckets();
        System.out.println("====getAllBuckets===="+list);
    }




}
