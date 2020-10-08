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

public class GraphActivity extends Activity                 // MPAndroidChart 라이브러리를 이용한 그래프 액티비티
{                 // 일주일간의 평균 옵습도를 나타내는 그래프 = 그래프 A
                  // 달력에서 선택한 날짜의 온습도를 나타내는 그래프 = 그래프 B
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

        lineChart = findViewById(R.id.chart);           // 하나의 뷰에 두개의 그래프를 가지는 하나의 차트를 띄움
        btnCalendar = findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {          // 달력 창을 띄우는 이벤트 (CalendarActivity.class 로 전환)
                Intent Cal_intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(Cal_intent);
                lineChart.invalidate();
            }
        });
        btnWeek = findViewById(R.id.btnWeek);
        btnWeek.setOnClickListener(new View.OnClickListener() {     // 그래프 A를 띄우는 이벤트
            @Override
            public void onClick(View v) {
                SetWReadHT(ref);
            }
        });
        SetWReadHT(ref);                                             // 액티비티를 처음 시작하면 그래프 A를 띄우도록 함
    }

    public void WeekendGraph(ArrayList new_arrayListWT, ArrayList new_arrayListWH)      // 그래프 A의 설정 메소드
    {
        lineChart.invalidate();
        List<Entry> entries_a = new ArrayList<>();
        List<Entry> entries_b = new ArrayList<>();
        if (new_arrayListWT.size() == 6)                // 현재 DB에 데이터가 7일 미만일 경우를 위한 if문
                                                        // 7일을 넘어갈경우 현재로부터 일주일 동안의 데이터를 사용
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
        for(int i =0; i < 7; i++)                        // 현재부터 7일 전까지의 날짜를 담을 리스트
        {
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            date = new Date(date.getTime()+(1000*60*60*24*-1*i));
            SimpleDateFormat mFormat = new SimpleDateFormat("MM/dd");
            String time = mFormat.format(date);
            Array_date.add(time);
        }
        final ArrayList<String> xAxisLabel = new ArrayList<>();     // 가로축에 위의 리스트 값을 표시
        xAxisLabel.add(Array_date.get(0));
        xAxisLabel.add(Array_date.get(1));
        xAxisLabel.add(Array_date.get(2));
        xAxisLabel.add(Array_date.get(3));
        xAxisLabel.add(Array_date.get(4));
        xAxisLabel.add(Array_date.get(5));
        xAxisLabel.add(Array_date.get(6));

        tvToday = findViewById(R.id.tvToday);                       // 현재부터 7일전 까지의 날짜 표시
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

        YAxis yLAxis = lineChart.getAxisLeft();             // 세로축의 경우 자동으로 세팅
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

    public void DailyGraph(ArrayList TarrayList, ArrayList HarrayList)    // 그래프 B의 설정 메소드
    {
        lineChart.invalidate();

        List<Entry> entries_a = new ArrayList<>();
        List<Entry> entries_b = new ArrayList<>();
        for(int i = 0; i<72; i++)       // 20분마다 측정을 하기 때문에 하루에 72개의 값을 측정해 리스트로 받아옴
                                        // 중간에 오류나 아직 db에 값이 없는 경우를 걸러 내기 위한 if문
        {
            if(TarrayList.get(i).toString().equals(""))     // 리스트에 값이 없으면 스킾
            {

            }
            else                                            // 가로축 단위가 24개 이므로 3으로 나눔
            {
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
        for(int i = 0; i <24; i++)  // 가로축 값은 한시간마다 표시
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

        YAxis yLAxis = lineChart.getAxisLeft();         // 세로축값은 자동으로 설정
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

    private void SetWReadHT(DatabaseReference ref)      // DB에서 일주일동안의 데이터를 읽어오는 메소드
    {
        WReadHT(ref, new FirebaseCallback() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                ArrayList<String> arrayList = new ArrayList<>();

                for(DataSnapshot messageData : dataSnapshot.getChildren())      // 모든 온습도 데이터를 받아와 리스트에 저장
                {
                    String value = messageData.getValue().toString();
                    arrayList.add(value);
                }

                ArrayList<String> arrayListWH = new ArrayList<>();
                ArrayList<String> arrayListWT = new ArrayList<>();

                if(arrayList.size()<504)                // 리스트의 크기가 7*24*3개 보다 작을 경우 모든 리스트 값의 온도와 습도값을 사용
                {
                    for(int i = arrayList.size(); 0 < i; i--)
                    {
                        int middle_index = arrayList.get(i-1).indexOf(',');
                        int end_index = arrayList.get(i-1).indexOf('H');

                        arrayListWT.add(arrayList.get(i-1).substring(3,middle_index));
                        arrayListWH.add(arrayList.get(i-1).substring(middle_index+4,end_index+4));
                    }
                }
                else                                    // 리스트의 크기가 7*24*3개 보다 클 경우 가장 최근 온도와 습도값을 사용
                {
                    for(int i = arrayList.size(); arrayList.size() - 504 < i; i--)
                    {
                        int middle_index = arrayList.get(i-1).indexOf(',');
                        int end_index = arrayList.get(i-1).indexOf('H');

                        arrayListWT.add(arrayList.get(i-1).substring(3,middle_index));
                        arrayListWH.add(arrayList.get(i-1).substring(middle_index+4,end_index+4));
                    }
                }

                if(arrayList.size() > 72)               // 리스트의 크기가 하루의 평균을 산출하지 못할경우 그래프를 표시하지 않음
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

    private void WReadHT(DatabaseReference ref, final FirebaseCallback listener)   // DB에서 읽어올 데이터의 위치를 정하고 datasnapshot에서 읽어오는 메서드
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

    public void WeekendCal(ArrayList arrayListWT, ArrayList arrayListWH)        // 온도와 습도를 저장한 리스트를 이용해 평균을 산출)
    {

        ArrayList<String> new_arrayListWT = new ArrayList<>();
        ArrayList<String> new_arrayListWH = new ArrayList<>();

        for(int i=0; i < arrayListWT.size()/72; i++)                // 504개의 데이터를 72개씩 나누어 7개의 평균값을 리스트에 저장
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

    private void SetReadHT(DatabaseReference ref, String shot_day) {        // DB에서 선택한 날짜의 하루동안의 데이터를 읽어오는 메소드
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 3; j++) {
                ReadHT(ref, new FirebaseCallback() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot)
                    {
                        ArrayList<String> THarray = new ArrayList<>();
                        if (dataSnapshot.exists())          // 리스트에 DB 데이터의 최근 24*3개의 값을 저장
                        {
                            String value = dataSnapshot.getValue().toString();
                            THarray.add(value);
                        }
                        else    // 값이 없는 경우 공백을 리스트에 추가
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

    private void ReadHT(DatabaseReference ref, final FirebaseCallback listener, String shot_Day,int i, int j)    // DB에서 읽어올 데이터의 위치를 정하고 datasnapshot에서 읽어오는 메서드
    {                                                                                                            // 날짜와 시간값을 받아와 child의 포맷을 맞춤
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

    public void DailyCal(ArrayList THarray)         // 값을 저장한 리스트를 편집하는 메소드
    {
        THarrayList.add(THarray.toString());

        ArrayList<String> TarrayList = new ArrayList<>();
        ArrayList<String> HarrayList = new ArrayList<>();
        if(THarrayList.size() == 72)
        {
            for (int i = 0; i < 72; i++)
            {
                if(THarrayList.get(i).equals("[]"))  // 공백인경우 그대로 공백을 넣음
                {
                    TarrayList.add("");
                    HarrayList.add("");
                }
                else                                 // 온습도 값을 추출
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

    public void Daily(String shot_Day)                  // 받아온 날짜를 텍스트뷰에 표시 + DB를 읽어오는 메소드에 전달
    {
        tvToday = findViewById(R.id.tvToday);
        tvToday.setText(shot_Day.substring(5,6)+"월"+shot_Day.substring(6,8)+"일");
        SetReadHT(ref,shot_Day);
    }

    private interface FirebaseCallback{       // onDatachange()의 비동기적 특성을 해결하기위한 콜백 매커니즘 인터페이스
        void onSuccess(DataSnapshot dataSnapshot);
        void onStart();
        void onFailure();
    }
}
