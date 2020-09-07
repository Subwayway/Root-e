package com.example.DIYSF;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GraphActivity extends Activity
{
    ArrayList<String> THarrayList = new ArrayList<>();
    LineChart lineChart;
    public TextView tvToday;
    public Button btnCalendar;
    public Button btnWeek;
    public static Context gcontext;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();  // DB 사용을 위한 인스턴스 생성

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);
        gcontext = this;

        lineChart = findViewById(R.id.chart);
        btnCalendar = findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Cal_intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(Cal_intent);
                lineChart.invalidate();
            }
        });
        btnWeek = findViewById(R.id.btnWeek);
        btnWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetWReadHT(ref);
            }
        });
        SetWReadHT(ref);
    }

    public void WeekendGraph(ArrayList new_arrayListWT, ArrayList new_arrayListWH)
    {
        lineChart.invalidate();
        List<Entry> entries_a = new ArrayList<>();
        List<Entry> entries_b = new ArrayList<>();
        if (new_arrayListWT.size() == 6)
        {
            entries_a.add(new Entry(0f, Float.parseFloat(new_arrayListWT.get(5).toString())));
            entries_a.add(new Entry(1f, Float.parseFloat(new_arrayListWT.get(4).toString())));
            entries_a.add(new Entry(2f, Float.parseFloat(new_arrayListWT.get(3).toString())));
            entries_a.add(new Entry(3f, Float.parseFloat(new_arrayListWT.get(2).toString())));
            entries_a.add(new Entry(4f, Float.parseFloat(new_arrayListWT.get(1).toString())));
            entries_a.add(new Entry(5f, Float.parseFloat(new_arrayListWT.get(0).toString())));
            entries_b.add(new Entry(0f, Float.parseFloat(new_arrayListWH.get(5).toString())));
            entries_b.add(new Entry(1f, Float.parseFloat(new_arrayListWH.get(4).toString())));
            entries_b.add(new Entry(2f, Float.parseFloat(new_arrayListWH.get(3).toString())));
            entries_b.add(new Entry(3f, Float.parseFloat(new_arrayListWH.get(2).toString())));
            entries_b.add(new Entry(4f, Float.parseFloat(new_arrayListWH.get(1).toString())));
            entries_b.add(new Entry(5f, Float.parseFloat(new_arrayListWH.get(0).toString())));
        }
        else if (new_arrayListWT.size() == 5)
        {
            entries_a.add(new Entry(0f, Float.parseFloat(new_arrayListWT.get(4).toString())));
            entries_a.add(new Entry(1f, Float.parseFloat(new_arrayListWT.get(3).toString())));
            entries_a.add(new Entry(2f, Float.parseFloat(new_arrayListWT.get(3).toString())));
            entries_a.add(new Entry(3f, Float.parseFloat(new_arrayListWT.get(1).toString())));
            entries_a.add(new Entry(4f, Float.parseFloat(new_arrayListWT.get(0).toString())));
            entries_b.add(new Entry(0f, Float.parseFloat(new_arrayListWH.get(4).toString())));
            entries_b.add(new Entry(1f, Float.parseFloat(new_arrayListWH.get(3).toString())));
            entries_b.add(new Entry(2f, Float.parseFloat(new_arrayListWH.get(2).toString())));
            entries_b.add(new Entry(3f, Float.parseFloat(new_arrayListWH.get(1).toString())));
            entries_b.add(new Entry(4f, Float.parseFloat(new_arrayListWH.get(0).toString())));
        }
        else if (new_arrayListWT.size() == 4)
        {
            entries_a.add(new Entry(0f, Float.parseFloat(new_arrayListWT.get(3).toString())));
            entries_a.add(new Entry(1f, Float.parseFloat(new_arrayListWT.get(2).toString())));
            entries_a.add(new Entry(2f, Float.parseFloat(new_arrayListWT.get(1).toString())));
            entries_a.add(new Entry(3f, Float.parseFloat(new_arrayListWT.get(0).toString())));
            entries_b.add(new Entry(0f, Float.parseFloat(new_arrayListWH.get(3).toString())));
            entries_b.add(new Entry(1f, Float.parseFloat(new_arrayListWH.get(2).toString())));
            entries_b.add(new Entry(2f, Float.parseFloat(new_arrayListWH.get(1).toString())));
            entries_b.add(new Entry(3f, Float.parseFloat(new_arrayListWH.get(0).toString())));
        }
        else if (new_arrayListWT.size() == 3)
        {
            entries_a.add(new Entry(0f, Float.parseFloat(new_arrayListWT.get(2).toString())));
            entries_a.add(new Entry(1f, Float.parseFloat(new_arrayListWT.get(1).toString())));
            entries_a.add(new Entry(2f, Float.parseFloat(new_arrayListWT.get(0).toString())));
            entries_b.add(new Entry(0f, Float.parseFloat(new_arrayListWH.get(2).toString())));
            entries_b.add(new Entry(1f, Float.parseFloat(new_arrayListWH.get(1).toString())));
            entries_b.add(new Entry(2f, Float.parseFloat(new_arrayListWH.get(0).toString())));
        }
        else if (new_arrayListWT.size() == 2)
        {
            entries_a.add(new Entry(0f, Float.parseFloat(new_arrayListWT.get(1).toString())));
            entries_a.add(new Entry(1f, Float.parseFloat(new_arrayListWT.get(0).toString())));
            entries_b.add(new Entry(0f, Float.parseFloat(new_arrayListWH.get(1).toString())));
            entries_b.add(new Entry(1f, Float.parseFloat(new_arrayListWH.get(0).toString())));
        }
        else if (new_arrayListWT.size() == 1)
        {
            entries_a.add(new Entry(0f, Float.parseFloat(new_arrayListWT.get(0).toString())));
            entries_b.add(new Entry(0f, Float.parseFloat(new_arrayListWH.get(0).toString())));
        }
        else {
            entries_a.add(new Entry(0f, Float.parseFloat(new_arrayListWT.get(6).toString())));
            entries_a.add(new Entry(1f, Float.parseFloat(new_arrayListWT.get(5).toString())));
            entries_a.add(new Entry(2f, Float.parseFloat(new_arrayListWT.get(4).toString())));
            entries_a.add(new Entry(3f, Float.parseFloat(new_arrayListWT.get(3).toString())));
            entries_a.add(new Entry(4f, Float.parseFloat(new_arrayListWT.get(2).toString())));
            entries_a.add(new Entry(5f, Float.parseFloat(new_arrayListWT.get(1).toString())));
            entries_a.add(new Entry(6f, Float.parseFloat(new_arrayListWT.get(0).toString())));
            entries_b.add(new Entry(0f, Float.parseFloat(new_arrayListWH.get(6).toString())));
            entries_b.add(new Entry(1f, Float.parseFloat(new_arrayListWH.get(5).toString())));
            entries_b.add(new Entry(2f, Float.parseFloat(new_arrayListWH.get(4).toString())));
            entries_b.add(new Entry(3f, Float.parseFloat(new_arrayListWH.get(3).toString())));
            entries_b.add(new Entry(4f, Float.parseFloat(new_arrayListWH.get(2).toString())));
            entries_b.add(new Entry(5f, Float.parseFloat(new_arrayListWH.get(1).toString())));
            entries_b.add(new Entry(6f, Float.parseFloat(new_arrayListWH.get(0).toString())));
        }

        LineDataSet lineDataSet_a = new LineDataSet(entries_a, "온도");
        LineDataSet lineDataSet_b = new LineDataSet(entries_b, "습도");

        lineDataSet_a.setLineWidth(2);
        lineDataSet_a.setCircleRadius(6);
        lineDataSet_a.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet_a.setCircleHoleColor(1);
        lineDataSet_a.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet_a.setDrawCircleHole(true);
        lineDataSet_a.setDrawCircles(true);
        lineDataSet_a.setDrawHorizontalHighlightIndicator(false);
        lineDataSet_a.setDrawHighlightIndicators(false);
        lineDataSet_a.setValueTextSize(20);

        lineDataSet_b.setLineWidth(2);
        lineDataSet_b.setCircleRadius(6);
        lineDataSet_b.setColor(Color.parseColor("#FF6633"));
        lineDataSet_b.setCircleHoleColor(1);
        lineDataSet_b.setCircleColor(Color.parseColor("#FF6633"));
        lineDataSet_b.setDrawCircleHole(true);
        lineDataSet_b.setDrawCircles(true);
        lineDataSet_a.setDrawHorizontalHighlightIndicator(false);
        lineDataSet_a.setDrawHighlightIndicators(false);
        lineDataSet_b.setValueTextSize(20);

        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet_a);
        lineData.addDataSet(lineDataSet_b);
        lineChart.setData(lineData);

        ArrayList<String> Array_date = new ArrayList<>();
        for(int i =0; i < 7; i++)
        {
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            date = new Date(date.getTime()+(1000*60*60*24*-1*i));
            SimpleDateFormat mFormat = new SimpleDateFormat("MM/dd");
            String time = mFormat.format(date);
            Array_date.add(time);
        }
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add(Array_date.get(0));
        xAxisLabel.add(Array_date.get(1));
        xAxisLabel.add(Array_date.get(2));
        xAxisLabel.add(Array_date.get(3));
        xAxisLabel.add(Array_date.get(4));
        xAxisLabel.add(Array_date.get(5));
        xAxisLabel.add(Array_date.get(6));

        tvToday = findViewById(R.id.tvToday);
        tvToday.setText(Array_date.get(0) + "~" + Array_date.get(6));

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel)
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                return xAxisLabel.get((int) value);
            }
        });
        xAxis.setTextSize(15);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setTextSize(15);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);
        yRAxis.setTextSize(15);
        Description description = new Description();
        description.setText("온습도 - 날짜");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EaseInCubic);

    }

    public void DailyGraph(ArrayList TarrayList, ArrayList HarrayList)
    {
        lineChart.invalidate();

        List<Entry> entries_a = new ArrayList<>();
        List<Entry> entries_b = new ArrayList<>();
        for(int i = 0; i<72; i++)
        {
            if(TarrayList.get(i).toString().equals(""))
            {

            }
            else {
                entries_a.add(new Entry(i/3, Float.parseFloat(TarrayList.get(i).toString())));
                entries_b.add(new Entry(i/3, Float.parseFloat(HarrayList.get(i).toString())));
            }
        }

        LineDataSet lineDataSet_a = new LineDataSet(entries_a, "온도");
        LineDataSet lineDataSet_b = new LineDataSet(entries_b, "습도");

        lineDataSet_a.setLineWidth(2);
        lineDataSet_a.setCircleRadius(1);
        lineDataSet_a.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet_a.setCircleHoleColor(1);
        lineDataSet_a.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet_a.setDrawCircleHole(true);
        lineDataSet_a.setDrawCircles(true);
        lineDataSet_a.setDrawHorizontalHighlightIndicator(false);
        lineDataSet_a.setDrawHighlightIndicators(false);
        lineDataSet_a.setDrawValues(false);

        lineDataSet_b.setLineWidth(2);
        lineDataSet_b.setCircleRadius(1);
        lineDataSet_b.setColor(Color.parseColor("#FF6633"));
        lineDataSet_b.setCircleHoleColor(1);
        lineDataSet_b.setCircleColor(Color.parseColor("#FF6633"));
        lineDataSet_b.setDrawCircleHole(true);
        lineDataSet_b.setDrawCircles(true);
        lineDataSet_b.setDrawHorizontalHighlightIndicator(false);
        lineDataSet_b.setDrawHighlightIndicators(false);
        lineDataSet_b.setDrawValues(false);

        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet_a);
        lineData.addDataSet(lineDataSet_b);
        lineChart.setData(lineData);


        final ArrayList<String> xAxisLabel = new ArrayList<>();
        for(int i = 0; i <24; i++)
        {
            xAxisLabel.add(i + "시");
        }

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel)
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                return xAxisLabel.get((int) value);
            }
        });
        xAxis.setTextSize(15);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setTextSize(15);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);
        yRAxis.setTextSize(15);

        Description description = new Description();
        description.setText("온습도 - 날짜");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EaseInCubic);

    }

    private void SetWReadHT(DatabaseReference ref)
    {
        WReadHT(ref, new FirebaseCallback() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                ArrayList<String> arrayList = new ArrayList<>();

                for(DataSnapshot messageData : dataSnapshot.getChildren())
                {
                    String value = messageData.getValue().toString();
                    arrayList.add(value);
                }

                ArrayList<String> arrayListWH = new ArrayList<>();
                ArrayList<String> arrayListWT = new ArrayList<>();

                if(arrayList.size()<504)
                {
                    for(int i = arrayList.size(); 0 < i; i--)
                    {
                        int middle_index = arrayList.get(i-1).indexOf(',');
                        int end_index = arrayList.get(i-1).indexOf('H');

                        arrayListWT.add(arrayList.get(i-1).substring(3,middle_index));
                        arrayListWH.add(arrayList.get(i-1).substring(middle_index+4,end_index+4));
                    }
                }
                else
                {
                    for(int i = arrayList.size(); arrayList.size() - 504 < i; i--)
                    {
                        int middle_index = arrayList.get(i-1).indexOf(',');
                        int end_index = arrayList.get(i-1).indexOf('H');

                        arrayListWT.add(arrayList.get(i-1).substring(3,middle_index));
                        arrayListWH.add(arrayList.get(i-1).substring(middle_index+4,end_index+4));
                    }
                }

                if(arrayList.size() > 72)
                {
                    WeekendCal(arrayListWT, arrayListWH);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"자료가 부족합니다.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onStart() {
            }
            @Override
            public void onFailure() {
            }
        });

    }

    private void WReadHT(DatabaseReference ref, final FirebaseCallback listener)
    {
        listener.onStart();

        ref.child("env").child("id 1").addValueEventListener(new ValueEventListener() {
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

    public void WeekendCal(ArrayList arrayListWT, ArrayList arrayListWH)
    {

        ArrayList<String> new_arrayListWT = new ArrayList<>();
        ArrayList<String> new_arrayListWH = new ArrayList<>();

        for(int i=0; i < arrayListWT.size()/72; i++)
        {
            double Tsum = 0;
            double Hsum = 0;

            for(int j=0; j < 72; j++)
            {
                Tsum += Double.parseDouble(arrayListWT.get(j + 72 * i).toString());
                Hsum += Double.parseDouble(arrayListWH.get(j + 72 * i).toString());
            }
            new_arrayListWT.add(Double.toString(Tsum/72));
            new_arrayListWH.add(Double.toString(Hsum/72));
        }

        WeekendGraph(new_arrayListWT,new_arrayListWH);
    }

    private void SetReadHT(DatabaseReference ref, String shot_day) {
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 3; j++) {
                ReadHT(ref, new FirebaseCallback() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot)
                    {
                        ArrayList<String> THarray = new ArrayList<>();
                        if (dataSnapshot.exists())
                        {
                            String value = dataSnapshot.getValue().toString();
                            THarray.add(value);
                        }
                        else
                        {
                            THarray.add("");
                        }
                        DailyCal(THarray);
                    }
                    @Override
                    public void onStart () {
                    }
                    @Override
                    public void onFailure () {
                    }

                }, shot_day,i,j);
            }
        }
    }

    private void ReadHT(DatabaseReference ref, final FirebaseCallback listener, String shot_Day,int i, int j)
    {
        int hour = 1;
        int minute = 20;
        listener.onStart();

        ref.child("env").child("id 1").child(shot_Day + "_" + String.format("%02d%02d",(hour * i),(minute*j))).addValueEventListener(new ValueEventListener() {
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

    public void DailyCal(ArrayList THarray)
    {

        THarrayList.add(THarray.toString());

        ArrayList<String> TarrayList = new ArrayList<>();
        ArrayList<String> HarrayList = new ArrayList<>();
        if(THarrayList.size() == 72)
        {
            for (int i = 0; i < 72; i++)
            {
                if(THarrayList.get(i).toString().equals("[]"))
                {
                    TarrayList.add("");
                    HarrayList.add("");
                }
                else
                {
                    int start_index = THarrayList.get(i).indexOf('T');
                    int middle_index = THarrayList.get(i).indexOf(',');
                    int end_index = THarrayList.get(i).indexOf('H');
                    TarrayList.add(THarrayList.get(i).substring(start_index + 2, middle_index));
                    HarrayList.add(THarrayList.get(i).substring(end_index + 2, end_index + 7));
                }
            }
            DailyGraph(TarrayList,HarrayList);
            THarrayList.clear();
        }
    }

    public void Daily(String shot_Day)
    {
        tvToday = findViewById(R.id.tvToday);
        tvToday.setText(shot_Day.substring(5,6)+"월"+shot_Day.substring(6,8)+"일");
        SetReadHT(ref,shot_Day);
    }

    private interface FirebaseCallback{
        void onSuccess(DataSnapshot dataSnapshot);
        void onStart();
        void onFailure();
    }
}
