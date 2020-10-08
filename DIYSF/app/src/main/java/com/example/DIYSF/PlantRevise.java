package com.example.DIYSF;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PlantRevise extends Activity           // 식물의 설정값 변경을 위한 클래스
{
    EditText R_name;
    EditText R_bright;
    EditText R_camera;
    EditText R_env;
    EditText R_led_off;
    EditText R_led_on;
    EditText R_water;
    EditText R_day;
    Button revise;

    int number = ((MainActivity)MainActivity.mActivity).num;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_revise);

        R_name = findViewById(R.id.Name);
        R_bright = findViewById(R.id.Bright);
        R_camera = findViewById(R.id.Camera);
        R_env = findViewById(R.id.Env);
        R_led_off = findViewById(R.id.LedOff);
        R_led_on = findViewById(R.id.LedOn);
        R_water = findViewById(R.id.Water);
        R_day = findViewById(R.id.Day);

        R_name.setText(((MainActivity)MainActivity.mActivity).name.getText());          // 원래 설정 값들을 가져와 EditText에 띄움
        R_bright.setText(((MainActivity)MainActivity.mActivity).bright.getText());
        R_camera.setText(((MainActivity)MainActivity.mActivity).camera.getText());
        R_env.setText(((MainActivity)MainActivity.mActivity).env.getText());
        R_led_off.setText(((MainActivity)MainActivity.mActivity).led_off.getText());
        R_led_on.setText(((MainActivity)MainActivity.mActivity).led_on.getText());
        R_water.setText(((MainActivity)MainActivity.mActivity).water.getText());
        R_day.setText(((MainActivity)MainActivity.mActivity).day.getText());

        revise = findViewById(R.id.Revise);
        revise.setOnClickListener(new Button.OnClickListener(){                        // 버튼을 누르면 EditText로 바꾼 값들을 적용
            @Override
            public void onClick(View v) {
                final Intent pintent = getIntent();
                String key = pintent.getStringExtra("key");

                ((MainActivity)MainActivity.mActivity).name.setText(R_name.getText());      // 메인창에 텍스트뷰에 변경된 값을 적용
                ((MainActivity)MainActivity.mActivity).bright.setText(R_bright.getText());
                ((MainActivity)MainActivity.mActivity).camera.setText(R_camera.getText());
                ((MainActivity)MainActivity.mActivity).env.setText(R_env.getText());
                ((MainActivity)MainActivity.mActivity).led_off.setText(R_led_off.getText());
                ((MainActivity)MainActivity.mActivity).led_on.setText(R_led_on.getText());
                ((MainActivity)MainActivity.mActivity).water.setText(R_water.getText());
                ((MainActivity)MainActivity.mActivity).day.setText(R_day.getText());
                Toast.makeText(PlantRevise.this,"설정 값이 변경 되었습니다.",Toast.LENGTH_SHORT).show();
                finish();

                String new_name = R_name.getText().toString();
                String new_bright = R_bright.getText().toString();
                String new_camera = R_camera.getText().toString();
                String new_env = R_env.getText().toString();
                String new_led_off = R_led_off.getText().toString();
                String new_led_on = R_led_on.getText().toString();
                String new_water = R_water.getText().toString();
                String new_day = R_day.getText().toString();
                String start = ((MainActivity)MainActivity.mActivity).start.getText().toString();
                PlantInfo info = new PlantInfo();
                info.writePlant(new_bright, new_camera, new_env, new_led_off,           // DB 값도 수정하기 위해 DB 수정 클래스 호출
                        new_led_on, new_water, new_day, new_name, start, number, key);
            }
        });
    }
}
