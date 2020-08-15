package com.example.DIYSF;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlantListActivity extends Activity {

    int number;
    ArrayList<String> Array = new ArrayList<>();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    ArrayAdapter<String> adapter;
    ListView plantList;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_list);

        ArrayList<String> items =  new ArrayList<>();
        plantList = findViewById(R.id.Plant_ListView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        plantList.setAdapter(adapter);

        setUser(ref);

        plantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                number = position + 1 ;

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
                        finish();
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFailure() {

                    }
                }, number);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setUser(DatabaseReference ref)
    {
        adapter.clear();
        readData(ref, new FirebaseCallback() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot)
            {
                String value = dataSnapshot.getValue().toString();
                Array.add(value);
                adapter.add(value);
            }

            @Override
            public void onStart() {
                Log.d("Onstart", "start");
            }

            @Override
            public void onFailure() {
            }
        });
    }

    private void readData(DatabaseReference ref, final FirebaseCallback listener)
    {
        int i=1;
        listener.onStart();

        while (true)
        {
            try {

                ref.child("setting").child("id 1").child("setting" + i).child("plant").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listener.onSuccess(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onFailure();
                    }
                });
                i++;
                if(i==4)
                    break;
            }catch (Exception e){
                break;
            }

        }
        adapter.notifyDataSetChanged();
    }

    private void showData(DatabaseReference ref, final FirebaseCallback listener, int number)
    {
        ref.child("setting").child("id 1").child("setting" + number).addValueEventListener(new ValueEventListener() {
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



