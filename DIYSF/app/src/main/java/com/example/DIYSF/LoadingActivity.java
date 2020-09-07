package com.example.DIYSF;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class LoadingActivity extends Activity {               // 실행 로딩창을 위한 액티비티
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);            // 로딩화면과 연결
        startLoading();
    }
    private void startLoading() {
        Handler handler = new Handler();         // 핸들러 객체 선언
        handler.postDelayed(new Runnable() {    // 2초뒤에 메인 화면이 뜨도록 설정
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }
}