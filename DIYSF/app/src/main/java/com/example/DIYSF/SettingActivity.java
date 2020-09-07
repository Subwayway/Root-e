package com.example.DIYSF;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

public class SettingActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener{

    public String KEY_ID;
    public Preference KEY;
    public PreferenceScreen screen;
    public EditTextPreference PW;
    public EditTextPreference ID;
    public static Context sContext;
    public String id;
    public String password ;
    public String key;
    private bluetooth bluetoothService_obj = null;

    public static final int MODE_REQUEST = 1;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
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

                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    if(readMessage.contains("id"))
                    {
                       ReadId(readMessage);
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        sContext = this;
        screen = getPreferenceScreen();

        ID = (EditTextPreference) screen.findPreference("ID");
        PW = (EditTextPreference) screen.findPreference("Password");
        KEY = screen.findPreference("Key");
        PW.setOnPreferenceChangeListener(this);
        ID.setOnPreferenceChangeListener(this);

        if (bluetoothService_obj == null){
            bluetoothService_obj = new bluetooth(this, mHandler);
            mOutStringBuffer = new StringBuffer("");
        }
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        SharedPreferences PW_prefs = getSharedPreferences("PW_prefs",MODE_PRIVATE);
        SharedPreferences ID_prefs = getSharedPreferences("ID_prefs",MODE_PRIVATE);
        SharedPreferences.Editor PW_editor = PW_prefs.edit();
        SharedPreferences.Editor ID_editor = ID_prefs.edit();
        String value = (String) newValue;
        if(preference == PW)
        {
            PW_editor.putString("Password",value);
            PW_editor.commit();
            PW.setSummary(value);
        }
        else if(preference == ID)
        {
            ID_editor.putString("ID",value);
            ID_editor.commit();
            ID.setSummary(value);
        }
        ID_editor.apply();
        PW_editor.apply();
        return true;
    }

    @Override
    protected void onResume()       // edittextpreference에 입력한 값 유지
    {
        super.onResume();
        SharedPreferences PW_prefs = getSharedPreferences("PW_prefs",MODE_PRIVATE);
        SharedPreferences ID_prefs = getSharedPreferences("ID_prefs",MODE_PRIVATE);
        password = PW_prefs.getString("Password","");
        id = ID_prefs.getString("ID","");
        PW.setSummary(password);
        ID.setSummary(id);

        SharedPreferences Key_prefs = getSharedPreferences("Key_prefs",MODE_PRIVATE);
        String key = Key_prefs.getString("KEY_ID", "");
        KEY.setSummary(key);
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference)  // preference를 버튼으로 사용
    {
        if(preference.getKey().equals("OnOff"))
        {
            BtnOn();
        }
        else if(preference.getKey().equals("list"))
        {
            BtnList();
        }
        else if(preference.getKey().equals("BtnSend"))
        {
            SharedPreferences PW_prefs = getSharedPreferences("PW_prefs",MODE_PRIVATE);
            SharedPreferences ID_prefs = getSharedPreferences("ID_prefs",MODE_PRIVATE);
            password = PW_prefs.getString("Password","");
            id = ID_prefs.getString("ID","");
            BtnSend("WF " + id + " " + password);
        }
        else if(preference.getKey().equals("BtnExit"))
        {
            BtnSend("SD 0");
        }
        else if(preference.getKey().equals("BtnRestart"))
        {
            BtnSend("RB 0");
        }
        else if(preference.getKey().equals("delete"))
        {
            SharedPreferences PW_prefs = getSharedPreferences("PW_prefs",MODE_PRIVATE);
            SharedPreferences ID_prefs = getSharedPreferences("ID_prefs",MODE_PRIVATE);
            SharedPreferences Key_prefs = getSharedPreferences("Key_prefs",MODE_PRIVATE);

            SharedPreferences.Editor PW_editor = PW_prefs.edit();
            SharedPreferences.Editor ID_editor = ID_prefs.edit();
            SharedPreferences.Editor Key_editor = Key_prefs.edit();
            PW_editor.remove("Password");
            ID_editor.remove("ID");
            Key_editor.remove("KEY_ID");
            PW_editor.commit();
            ID_editor.commit();
            Key_editor.commit();
        }
        return false;
    }

    public void BtnOn()
    {                         // 불루투스 onoff 함수
        if(bluetoothService_obj.getDeviceState()) // 블루투스 기기의 지원여부가 true 일때
        {
            bluetoothService_obj.enableBluetooth(); //블루투스 활성화 시작.
        }
        else
        {
            Toast.makeText(getApplicationContext(),"블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void BtnList()
    {
        if(bluetoothService_obj.getDeviceState()) // 블루투스 기기의 지원여부가 true 일때
        {
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(i, REQUEST_ENABLE_BT);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"불루투스가 연결되어 있지않습니다.",Toast.LENGTH_SHORT).show();
        }
    }

    public void BtnSend(String code)
    {                         // 불루투스 문자 전송 함수
        if( bluetoothService_obj.getState() == bluetooth.STATE_CONNECTED)
        {
            bluetoothService_obj.write(code);
        }
        else {
            Toast.makeText(getApplicationContext(), "블루투스 연결을 먼저 해 주세요!! ", Toast.LENGTH_SHORT).show();
        }
    }

    public void ReadId(String code)
    {
        Toast.makeText(getApplicationContext(), "id : " + code, Toast.LENGTH_LONG).show();
        KEY_ID = code;

        SharedPreferences Key_prefs = getSharedPreferences("Key_prefs",MODE_PRIVATE);
        SharedPreferences.Editor Key_editor = Key_prefs.edit();

        Key_editor.putString("KEY_ID", KEY_ID);
        Key_editor.commit();
        KEY.setSummary(KEY_ID);
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

