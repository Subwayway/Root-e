package com.example.DIYSF;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PlantInfo         // DB의 데이터를 수정(업로드)하는 클래스
{                              // DB에 이미 있는 value 를 수정만하고 키는 추가 안하는 형태로 사용
    public String Bright;
    public String Camera;
    public String Env;
    public String Ledoff;
    public String Ledon;
    public String Water;
    public String day;
    public String plant;
    public String start;

    DatabaseReference DBReference;

    public PlantInfo(){       // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public PlantInfo(String bright, String camera, String env, String led_off,
                     String led_on, String water, String day, String plant, String start)
    {
        this.Bright = bright;
        this.Camera = camera;
        this.Env = env;
        this.Ledoff = led_off;
        this.Ledon = led_on;
        this.Water = water;
        this.day = day;
        this.plant = plant;
        this.start = start;
    }

    public Map<String, Object> toMap()           // DB 업로드를 위해 key-value 형식으로 해쉬맵에 저장
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Bright", Bright);
        result.put("Camera", Camera);
        result.put("Env", Env);
        result.put("Ledoff", Ledoff);
        result.put("Ledon", Ledon);
        result.put("Water", Water);
        result.put("day", day);
        result.put("plant", plant);
        result.put("start", start);
        return result;
    }

    public void writePlant(String bright, String camera, String env, String led_off,
                           String led_on, String water, String day, String plant, String start, int number, String key) // number와 key는 child 위치 지정을 위해 받아옴
    {

        PlantInfo info = new PlantInfo(bright, camera, env, led_off, led_on, water, day, plant, start);
        info.toMap();                                                                       // 받아온 문자열들을 해쉬맵에 저장
        DBReference = FirebaseDatabase.getInstance().getReference().child("setting")
                .child(key).child("setting" + number);                                      // 데이터를 입력받을 child 지정
        DBReference.setValue(info).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
    }

}
