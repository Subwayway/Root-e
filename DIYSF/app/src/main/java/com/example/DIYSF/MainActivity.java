package com.example.DIYSF;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN";

    private static final int MY_PERMISSION_STORAGE = 1111;       // 스토리지 허가 번호

    private Button btnSelect;
    private Button btnRevise;
    TextView name;
    TextView bright;
    TextView camera;
    TextView env;
    TextView led_off;
    TextView led_on;
    TextView water;
    TextView day;
    TextView start;
    int num;

    public static Context mActivity;

    long now = System.currentTimeMillis();     // 현재 시간 출력을 위한 인스턴스 선언
    Date date = new Date(now);
    SimpleDateFormat sdfNow  = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String formatDate = sdfNow.format(date);
    TextView dateNow;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, LoadingActivity.class);  // 로딩 화면 불러오기
        Log.e(TAG, "onCreate");
        startActivity(intent);

        CheckPermission();                                // 저장소 사용 메소드
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActivity = this;

        dateNow = findViewById(R.id.dateNow);             // 시간 출력
        dateNow.setText(formatDate);

        btnSelect = findViewById(R.id.btnSelect);

        btnSelect.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent plantintent = new Intent(getApplicationContext(), PlantListActivity.class);
                startActivity(plantintent);
            }
        });

        btnRevise = findViewById(R.id.btnRevise);

        btnRevise.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                if (name.getText().equals(""))
                {
                    Toast.makeText(MainActivity.this,"식물을 먼저 선택해 주세요",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.d("test", "number : " + num);
                    Intent pintent = new Intent(getApplicationContext(), PlantRevise.class);
                    startActivity(pintent);
                }
            }
        });
        name = findViewById(R.id.Name);
        bright = findViewById(R.id.Bright);
        camera = findViewById(R.id.Camera);
        env = findViewById(R.id.Env);
        led_off = findViewById(R.id.LedOff);
        led_on = findViewById(R.id.LedOn);
        water = findViewById(R.id.Water);
        day = findViewById(R.id.Day);
        start = findViewById(R.id.Start);

    }

    public void SetText(ArrayList<String> arrayList, int number)
    {
        name.setText(arrayList.get(7));
        bright.setText(arrayList.get(0));
        camera.setText(arrayList.get(1));
        env.setText(arrayList.get(2));
        led_on.setText(arrayList.get(3));
        led_off.setText(arrayList.get(4));
        water.setText(arrayList.get(5));
        day.setText(arrayList.get(6));
        start.setText(arrayList.get(8));
        num = number;
    }

    private void CheckPermission()                             // 저장소 권한 허가 설정을 위한 메소드
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용히셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)    // 허가 요청 결과 수신 메소드
    {
        switch (requestCode)
        {
            case MY_PERMISSION_STORAGE:
                for(int i = 0; i<grantResults.length; i++)
                {
                    if (grantResults[i]<0)
                    {
                        Toast.makeText(MainActivity.this,"해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)      // 메인 화면에 옵션창을 만들기 위한 메소드
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)   //  setting 버튼을 눌럿을때의 이벤트 부여
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)                    // 화면 전환
        {
            Intent SettingActivity = new Intent(this, SettingActivity.class);
            startActivity(SettingActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
