package com.example.DIYSF;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN";

    private static final int MY_PERMISSION_STORAGE = 1111;       // 스토리지 허가 번호

    private File file, dir;
    private String FileName = null;

    Button btnSelect;
    Button btnRevise;
    Button btnGraph;
    Button btnSave;

    TextView name;
    TextView bright;
    TextView camera;
    TextView env;
    TextView led_off;
    TextView led_on;
    TextView water;
    TextView day;
    TextView start;
    ImageView gif;
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

        CheckPermission();                                // 저장소 사용 허가 메소드
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActivity = this;

        dateNow = findViewById(R.id.dateNow);             // 텍스트뷰에 시간 출력
        dateNow.setText(formatDate);

        btnSelect = findViewById(R.id.btnSelect);         // 선택창을 띄우는 이벤트 (PlantListActivity.class 로 화면전환)
        btnSelect.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                SharedPreferences Key_prefs = getSharedPreferences("Key_prefs",MODE_PRIVATE);
                String ke = Key_prefs.getString("KEY_ID", "");
                if(!ke.equals(""))                        // id 키를 먼저 받아와야 실행
                {
                    String key = ke.substring(0,2) + " " + ke.substring(2);
                    Intent plantintent = new Intent(getApplicationContext(), PlantListActivity.class);
                    plantintent.putExtra("key", key);
                    startActivity(plantintent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"제품 key를 먼저 받으세요",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRevise = findViewById(R.id.btnRevise);          // 수정창을 띄우는 이벤트 (PlantRevise.class 로 화면전환)
        btnRevise.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                SharedPreferences Key_prefs = getSharedPreferences("Key_prefs",MODE_PRIVATE);
                String ke = Key_prefs.getString("KEY_ID", "");
                if (name.getText().equals(""))             // 식물을 먼저 선택해야 수정가능
                {
                    Toast.makeText(MainActivity.this,"식물을 먼저 선택해 주세요",Toast.LENGTH_SHORT).show();
                }
                else if(!ke.equals(""))                    // id 키를 먼저 받아와야 실행
                {
                    String key = ke.substring(0,2) + " " + ke.substring(2);
                    Intent pintent = new Intent(getApplicationContext(), PlantRevise.class);
                    pintent.putExtra("key", key);
                    startActivity(pintent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"제품 key를 먼저 받으세요",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGraph = findViewById(R.id.btnGraph);             // 그래프창을 띄우는 이벤트 (GraphActivity.class 로 화면전환)
        btnGraph.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                SharedPreferences Key_prefs = getSharedPreferences("Key_prefs",MODE_PRIVATE);
                String ke = Key_prefs.getString("KEY_ID", "");
                if(!ke.equals(""))                         // id 키를 먼저 받아와야 실행
                {
                    String key = ke.substring(0,2) + " " + ke.substring(2);
                    Intent gintent = new Intent(getApplicationContext(), GraphActivity.class);
                    gintent.putExtra("key", key);
                    startActivity(gintent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"제품 key를 먼저 받으세요",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new Button.OnClickListener() {       // 이미지뷰에 있는 gif 사진을 저장하는 이벤트, URL을 통해 다운로드
            @Override
            public void onClick(View view)
            {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                SharedPreferences Key_prefs = getSharedPreferences("Key_prefs",MODE_PRIVATE);
                String ke = Key_prefs.getString("KEY_ID", "");
                if(!ke.equals(""))                         // id 키를 먼저 받아와야 실행
                {
                    String key = ke.substring(0,2) + " " + ke.substring(2);
                    showGif(ref, new FirebaseCallback() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            ArrayList<String> gifList = new ArrayList<>();
                            if(dataSnapshot.exists()) {             // DB에 존재하는 모든 gif URL을 리스트에 저장
                                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                                    String value = messageData.getValue().toString();
                                    gifList.add(value);
                                }
                               saveGif(gifList);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"저장된 사진이 없습니다.",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onFailure() {

                        }
                    }, key);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"제품 key를 먼저 받으세요",Toast.LENGTH_SHORT).show();
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

        gif = findViewById(R.id.Plant_Gif);
    }

    public void SetText(ArrayList<String> arrayList, int number)       // 받아온 리스트를 사용해 텍스트뷰에 식물 데이터를 띄움
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

    public void SetGif(ArrayList<String> gifList)           // glide 라이브러리를 이용해 이미지뷰에 gif를 띄움
    {
        String imageUrl = gifList.get(gifList.size()-1);
        Glide.with(this)             // 현재 액티비티에
                .load(imageUrl)             // Url을 사용하여
                .asGif()                    // gif 형태로
                .into(gif);                 // 이미지뷰에 띄움
    }

    public void saveGif(ArrayList<String> gifList)          // 저장할 파일이름과 위치를 지정하는 메소드
    {
        String imageUrl = gifList.get(gifList.size()-1);    // 리스트에 있는 Url 중 가장 최근것을 사용

        FileName = imageUrl.substring(87,102)+ ".gif";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString(); // 핸드폰 저장소중 DCIM 파일을 저장 위치로 지정
        dir = new File(path);
        dir.mkdirs();
        if (!dir.exists()) {    // 파일이 없으면 생성
            dir.mkdirs();
            Toast.makeText(getApplicationContext(), "파일 생성", Toast.LENGTH_SHORT).show();
        }

        DownloadPhotoFromURL downloadPhotoFromURL = new DownloadPhotoFromURL();

        if(new File(dir.getPath() + File.separator + FileName).exists() == false)   // 지정한 위치에 사진이 없으면 클래스 실행, 있으면 스킾
        {
            downloadPhotoFromURL.execute(imageUrl,FileName);
        }
        else
            {
            Toast.makeText(getApplicationContext(), "파일이 이미 존재합니다", Toast.LENGTH_SHORT).show();

        }
    }

    class DownloadPhotoFromURL extends AsyncTask<String, Integer, String> {                   // Url을 통해 사진을 저장하는 클래스
        int count;
        int lenghtOfFile = 0;
        InputStream input = null;
        OutputStream output = null;
        String tempFileName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setProgress(0);
        }

        @Override
        protected String doInBackground(String... params) {
            tempFileName = params[1];
            file = new File(dir, params[1]); // 다운로드할 파일명
            try {
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                lenghtOfFile = connection.getContentLength(); // 파일 크기를 가져옴

                if (file.exists()) {                          // 파일이 존재할경우 지우고 다시 생성하지만 현재 사용 X
                    file.delete();
                    Log.d(TAG, "file deleted...");
                }

                input = new BufferedInputStream(url.openStream());  // 스트림을 통해 url을 열어 파일을 받음
                output = new FileOutputStream(file);
                byte data[] = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return String.valueOf(-1);
                    }
                    total = total + count;
                    if (lenghtOfFile > 0) { // 파일 총 크기가 0 보다 크면
                        publishProgress((int) (total * 100 / lenghtOfFile));
                    }
                    output.write(data, 0, count); // 파일에 데이터를 기록
                }

                output.flush();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    }
                    catch(IOException ioex) {
                    }
                }
                if (output != null) {
                    try {
                        output.close();
                    }
                    catch(IOException ioex) {
                    }
                }
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // 백그라운드 작업의 진행상태를 표시하기 위해서 호출하는 메소드
            //progressBar.setProgress(progress[0]);
            //textView.setText("다운로드 : " + progress[0] + "%");
        }

        protected void onPostExecute(String result) {
            // pdLoading.dismiss();
            if (result == null) {
                Toast.makeText(getApplicationContext(), "다운로드 완료되었습니다.", Toast.LENGTH_LONG).show();

                File file = new File(dir + "/" + tempFileName);
                //이미지 스캔해서 갤러리 업데이트
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

            } else {
                Toast.makeText(getApplicationContext(), "다운로드 에러", Toast.LENGTH_LONG).show();
            }
        }
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

    private void showGif(DatabaseReference ref, final FirebaseCallback listener, String key)   // DB에서 읽어올 데이터의 위치를 정하고 datasnapshot에서 읽어오는 메서드(gif URL)}
    {
        ref.child("gif").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private interface FirebaseCallback{    // onDatachange()의 비동기적 특성을 해결하기위한 콜백 매커니즘 인터페이스
        void onSuccess(DataSnapshot dataSnapshot);
        void onStart();
        void onFailure();
    }
}
