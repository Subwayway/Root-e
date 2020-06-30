package com.example.DIYSF;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public static TextView mTvBluetoothStatus;
    EditText mTvSendData;
    private Button btnOn;
    Button mBtnConnect;
    Button mBtnSendData;

    private bluetooth bluetoothService_obj = null;
    //BluetoothService클래스에 접근하기 위한 객체이다.
    private StringBuffer mOutStringBuffer;
    private static final String TAG = "MAIN";

    private static final int MY_PERMISSION_STORAGE = 1111;

    private static final boolean D = true;
    private int mSelectedBtn;

    public static final int MODE_REQUEST = 1;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_WRITE = 2;
    private final static int REQUEST_CONNECT_DEVICE = 1;
    private final static int REQUEST_ENABLE_BT = 2;

    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket = null; // 블루투스 소켓
    private OutputStream outputStream = null; // 블루투스에 데이터를 출력하기 위한 출력 스트림


    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfNow  = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String formatDate = sdfNow.format(date);
    TextView dateNow;

    private final Handler mHandler = new Handler() {
        //핸들러의 기능을 수행할 클래스(handleMessage)
        public void handleMessage(Message msg) {
        //BluetoothService로부터 메시지(msg)를 받는다.
            super.handleMessage(msg);

            switch(msg.what)
            {
                case MESSAGE_STATE_CHANGE :
                    if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);

                    switch (msg.arg1)
                    {
                        case bluetooth.STATE_CONNECTED : // 연결 성공
                            Toast.makeText(getApplicationContext(),"블루투스 연결성공",Toast.LENGTH_SHORT).show();
                            break;

                        case bluetooth.STATE_FAIL :  // 연결 실패
                            Toast.makeText(getApplicationContext(),"블루투스 연결실패",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;

           }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, LoadingActivity.class);
        Log.e(TAG, "onCreate");
        startActivity(intent);

        CheckPermission();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dateNow = findViewById(R.id.dateNow);
        dateNow.setText(formatDate);

        mTvBluetoothStatus =  findViewById(R.id.tvStatus);

        btnOn =  findViewById(R.id.OnOff);
        btnOn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(bluetoothService_obj.getDeviceState()) // 블루투스 기기의 지원여부가 true 일때
                    {
                        bluetoothService_obj.enableBluetooth(); //블루투스 활성화 시작.
                    }
                    else
                    {
                        finish();
                    }
            }
        });
        if (bluetoothService_obj == null){
            bluetoothService_obj = new bluetooth(this, mHandler);
            mOutStringBuffer = new StringBuffer("");
        }
        mSelectedBtn = -1 ;
        mBtnConnect =  findViewById(R.id.btnConnect);
        mBtnConnect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(bluetoothService_obj.getDeviceState())
                {
                    bluetoothService_obj.scanDevice();
                }
                else {
                    Toast.makeText(getApplicationContext(), "블루투스 연결을 먼저 해 주세요!! ", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mTvSendData = findViewById(R.id.etSend);
        mBtnSendData =  findViewById(R.id.btnSend);
        mBtnSendData.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if( bluetoothService_obj.getState() == bluetooth.STATE_CONNECTED){
                    bluetoothService_obj.write(mTvSendData.getText().toString()); ;

                }
                else {
                    Toast.makeText(getApplicationContext(), "블루투스 연결을 먼저 해 주세요!! ", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void CheckPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용히셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case MY_PERMISSION_STORAGE:
                for(int i = 0; i<grantResults.length; i++){
                    if (grantResults[i]<0){
                        Toast.makeText(MainActivity.this,"해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d(TAG, "onActivityResult" + resultCode);

        switch (requestCode)
        {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK)
                { // 블루투스 활성화를 확인을 클릭하였다면
                    Toast.makeText(getApplicationContext(), "블루투스 활성화", Toast.LENGTH_LONG).show();
                    mTvBluetoothStatus.setText("활성화");
                    bluetoothService_obj.scanDevice();
                }
                else
                { // 블루투스 활성화를 취소를 클릭하였다면
                    Toast.makeText(getApplicationContext(), "블루투스 비활성화", Toast.LENGTH_LONG).show();
                    mTvBluetoothStatus.setText("비활성화");
                }
                break;

            case REQUEST_CONNECT_DEVICE:
                if(resultCode==Activity.RESULT_OK)
                {
                    bluetoothService_obj.getDeviceInfo(data);
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
