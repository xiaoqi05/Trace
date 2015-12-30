package cuitx.edu.com.trade.activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.cloudstorage.auth.AWSCredentials;
import com.sina.cloudstorage.auth.BasicAWSCredentials;
import com.sina.cloudstorage.services.scs.SCS;
import com.sina.cloudstorage.services.scs.SCSClient;
import com.sina.cloudstorage.services.scs.model.Bucket;
import com.sina.cloudstorage.services.scs.model.PutObjectResult;

import java.io.File;
import java.util.List;

import cuitx.edu.com.trade.R;
import cuitx.edu.com.trade.util.trace.ConnectivityStatus;
import cuitx.edu.com.trade.util.trace.DataUtil;
import cuitx.edu.com.trade.util.trace.FileUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 8090;
    private static final int UPLOAD_FILE = 8090;
    private SCS conn;
    private Button bt_chose;
    private Button bt_upload;
    private String uploadFilePath;
    private TextView tv_result;
    private StringBuffer resultBuffer;
    private ProgressDialog progressDialog;
    private LinearLayout ll_content;
    private boolean isUploaded = true;
    private ConnectivityStatus connectivityStatus;
    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPLOAD_FILE:
                    isUploaded = true;
                    progressDialog.dismiss();
                    String result = msg.obj.toString();
                    if (!result.equals("")) {
                        resultBuffer.append("上传成功：MD5值为").append(msg.obj).append("\r\n").append("\r\n");
                        tv_result.setText(resultBuffer.toString());
                        Snackbar.make(ll_content, "上传成功", Snackbar.LENGTH_LONG).show();
                    } else {
                        resultBuffer.append("上传失败：本地文件不存在，请先点击下载文件").append("\r\n");
                        tv_result.setText(resultBuffer.toString());
                    }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initData();

    }

    private void initData() {
        connectivityStatus = new ConnectivityStatus(this);
        resultBuffer = new StringBuffer();
        String accessKey = "sqt3t2Yg49KXqWvwrRxj";
        String secretKey = "d155aeb9cc905b122333047e77536e2913f8d304";
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        if (connectivityStatus.isConnected()) {
            conn = new SCSClient(credentials);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Bucket> list = getAllBuckets();
                    boolean flag = false;
                    for (Bucket bucket : list) {
                        if (bucket.getName().equals(getLocalBucket_name())) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        createBucket(getLocalBucket_name());
                    }
                }
            }).start();
        }
    }

    private String getLocalBucket_name() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return "user" + tm.getDeviceId();
    }


    private void findView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        bt_upload = (Button) findViewById(R.id.bt_upload);
        bt_chose = (Button) findViewById(R.id.bt_chose);
        bt_chose.setOnClickListener(this);
        bt_upload.setOnClickListener(this);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Me want banana!!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        tv_result = (TextView) findViewById(R.id.tv_result);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("上传中...");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);
                            this.uploadFilePath = path;
                            resultBuffer.append("文件地址：").append(uploadFilePath).append("\r\n");
                            tv_result.setVisibility(View.VISIBLE);
                            tv_result.setText(resultBuffer.toString());
                            isUploaded = false;
                        } catch (Exception e) {
                            Toast.makeText(
                                    this,
                                    getString(R.string.get_upload_file_failed),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    class MyAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            return putUploadObject(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Message msg = new Message();
            msg.what = UPLOAD_FILE;
            msg.obj = s;
            handle.sendMessage(msg);
        }

    }


    public List<Bucket> getAllBuckets() {
        List<Bucket> list = conn.listBuckets();
        //  System.out.println("====Buckets===="+list);
        return list;
    }

    /**
     * 创建bucket
     */
    public void createBucket(String bucket_name) {
        Bucket bucket = conn.createBucket(bucket_name);
        System.out.println("====Buckets====" + bucket);
    }


    /**
     * 上传文件
     */
    public String putUploadObject(String bucket_name, String uploadPath) {
        File file = new File(uploadPath);
        if (file.exists()) {
            PutObjectResult putObjectResult = conn.putObject(bucket_name,
                    DataUtil.getCurrentTime(),
                    new File(uploadPath));
            System.out.println(putObjectResult);
            return putObjectResult.getContentMd5();
        } else {
            return "";
        }
    }


    public void selectUploadFile(View view) {
        Intent target = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(target,
                this.getString(R.string.choose_file));
        try {
            this.startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException ex) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_chose:
                selectUploadFile(v);
                break;
            case R.id.bt_upload:
                if (!isUploaded) {
                    if (!TextUtils.isEmpty(uploadFilePath)) {
                        if (connectivityStatus.isConnected()) {
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            new MyAsyncTask().execute(getLocalBucket_name(), uploadFilePath);
                        }else {
                            Snackbar.make(v, "当前网络不可用，请连接网络", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } else {
                        Snackbar.make(v, "请先选择文件", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(v, "请不要重复上传", Snackbar.LENGTH_LONG).show();
                }
                break;


        }
    }


}
