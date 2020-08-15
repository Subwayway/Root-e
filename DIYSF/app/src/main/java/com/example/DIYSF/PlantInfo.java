package com.example.DIYSF;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PlantInfo {

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

    public PlantInfo(){

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

    public Map<String, Object> toMap()
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
                           String led_on, String water, String day, String plant, String start, int number)
    {
        PlantInfo info = new PlantInfo(bright, camera, env, led_off, led_on, water, day, plant, start);
        info.toMap();
        DBReference = FirebaseDatabase.getInstance().getReference().child("setting")
                .child("id 1").child("setting" + number);
        DBReference.setValue(info).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
    }

}
