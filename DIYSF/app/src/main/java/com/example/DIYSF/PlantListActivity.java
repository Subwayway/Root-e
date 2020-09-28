package com.example.DIYSF;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlantListActivity extends Activity {

    int number;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();  // DB 사용을 위한 인스턴스 생성
    ArrayAdapter<String> adapter;
    ListView plantList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_list);  // dialog로 설정한 xml과 연결
        final Intent plantintent = getIntent();
        String key = plantintent.getStringExtra("key");
        plantList = findViewById(R.id.Plant_ListView); // 목록을 띄울 Listview

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());   // Listview에 연결시킬 adapter 선언
        plantList.setAdapter(adapter);  // adapter와 listview를 연결

        setUser(ref, key);   // 식물 목록 띄우는 메소드

        plantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                number = position + 1 ;
                String key = plantintent.getStringExtra("key");
                showData(ref, new FirebaseCallback() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        ArrayList<String> arrayList = new ArrayList<>();
                        for(DataSnapshot messageData : dataSnapshot.getChildren())
                        {
                            String value = messageData.getValue().toString();
                            arrayList.add(value);
                        }
                        ((MainActivity)MainActivity.mActivity).SetText(arrayList, number);
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFailure() {

                    }
                }, number, key);

                showGif(ref, new FirebaseCallback() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        ArrayList<String> gifList = new ArrayList<>();
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                                String value = messageData.getValue().toString();
                                gifList.add(value);
                            }
                            ((MainActivity) MainActivity.mActivity).SetGif(gifList);
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
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setUser(DatabaseReference ref,String key)
    {
        adapter.clear();
        for(int i=0; i<10; i++)
        {
            readData(ref, new FirebaseCallback() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        String value = dataSnapshot.getValue().toString();
                        Log.d("test", value);
                        adapter.add(value);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onStart() {
                    Log.d("Onstart", "start");
                }

                @Override
                public void onFailure() {
                }
            }, i, key);
        }
    }

    private void readData(DatabaseReference ref, final FirebaseCallback listener, int i, String key)
    {
        listener.onStart();

        ref.child("setting").child(key).child("setting" + i).child("plant").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    private void showData(DatabaseReference ref, final FirebaseCallback listener, int number, String key)
    {
        ref.child("setting").child(key).child("setting" + number).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showGif(DatabaseReference ref, final FirebaseCallback listener, String key)
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

    private interface FirebaseCallback{
        void onSuccess(DataSnapshot dataSnapshot);
        void onStart();
        void onFailure();
    }
}



