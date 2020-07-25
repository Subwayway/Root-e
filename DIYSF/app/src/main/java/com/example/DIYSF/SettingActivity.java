package com.example.DIYSF;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

public class SettingActivity extends PreferenceActivity {

    public String password ;
    private bluetooth bluetoothService_obj = null;


    public static final int MODE_REQUEST = 1;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_WRITE = 2;
    private final static int REQUEST_CONNECT_DEVICE = 1;
    private final static int REQUEST_ENABLE_BT = 2;

    private static final boolean D = true;
    private StringBuffer mOutStringBuffer;

    private static final String TAG = "MAIN";

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
        addPreferencesFromResource(R.xml.preferences);

        if (bluetoothService_obj == null){
            bluetoothService_obj = new bluetooth(this, mHandler);
            mOutStringBuffer = new StringBuffer("");
        }

    }

    @Override
    protected void onResume()       // edittextpreference에 입력한 값 유지
    {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        password = prefs.getString("Password","");
    }


    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference)  // preference를 버튼으로 사용
    {
        String key = preference.getKey();
        Log.d("tag","클릭된 Preference의 key는 "+key);
        if(preference.getKey().equals("OnOff"))
        {
            Log.d("tag","블루투스 onoff");
            BtnOn();

        }
        else if(preference.getKey().equals("BtnSend"))
        {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            password = prefs.getString("Password","");
            Log.d("tag","문자 보내기"+password);
            BtnSend();
        }

        return false;
    }

    public void BtnOn() {                         // 불루투스 onoff 함수
        if(bluetoothService_obj.getDeviceState()) // 블루투스 기기의 지원여부가 true 일때
        {
            Log.d("Tag","블루투스 on");
            bluetoothService_obj.enableBluetooth(); //블루투스 활성화 시작.
        }
        else
        {
            Log.d("Tag","블루투스 off");

        }
    }

    public void BtnSend() {                         // 불루투스 문자 전송 함수
        if( bluetoothService_obj.getState() == bluetooth.STATE_CONNECTED){
            SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(this);
            bluetoothService_obj.write(prefer.getString("Password",""));
            Log.d("Tag",prefer.getString("Password",""));
        }
        else {
            Toast.makeText(getApplicationContext(), "블루투스 연결을 먼저 해 주세요!! ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  // bluetooth 클래스의 요청 결과 수신
    {
        Log.d(TAG, "onActivityResult" + resultCode);

        switch (requestCode)
        {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK)
                { // 블루투스 활성화를 확인을 클릭하였다면
                    Toast.makeText(getApplicationContext(), "블루투스 활성화", Toast.LENGTH_LONG).show();

                    bluetoothService_obj.scanDevice();
                }
                else
                { // 블루투스 활성화를 취소를 클릭하였다면
                    Toast.makeText(getApplicationContext(), "블루투스 비활성화", Toast.LENGTH_LONG).show();

                }
                break;

            case REQUEST_CONNECT_DEVICE:
                if(resultCode==Activity.RESULT_OK)
                {
                    bluetoothService_obj.getDeviceInfo(data);
                }
        }
    }
}

