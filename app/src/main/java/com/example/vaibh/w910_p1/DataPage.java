package com.example.vaibh.w910_p1;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class DataPage extends Fragment {

    private View fragData;
    private int pastHistoryCount = 5;
    private String uid;
    private Button btnAddData, btnSubData, btnMulData, btnDivData;
    public DataPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragData =  inflater.inflate(R.layout.fragment_data_page, container, false);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        btnAddData = fragData.findViewById(R.id.btnAddData);
        btnSubData = fragData.findViewById(R.id.btnSubData);
        btnMulData = fragData.findViewById(R.id.btnMulData);
        btnDivData = fragData.findViewById(R.id.btnDivData);
        getOverallTop("Addition");


        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "Addition";
                getOverallTop(type);
            }
        });

        btnSubData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "Subtraction";
                getOverallTop(type);
            }
        });

        btnMulData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "Multiplication";
                getOverallTop(type);
            }
        });


        btnDivData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "Division";
                getOverallTop(type);
            }
        });

        return fragData;
    }

    public void setAddedData(BarChart barChart, String type, BarData data) {

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setData(data);
        barChart.setFitBars(true);

        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }
    /*
        Now supports use of real data for the day of
     */
    // using code from MPAndroidCharts

    BarData generateData(String type, List<String> Scores) {
        Log.i("Aya", "Finally in generateData with mathType "+ type);
        ArrayList<BarEntry> dataEntries = new ArrayList<BarEntry>(pastHistoryCount);
        for (int j = 0; j < pastHistoryCount; j++) {
            dataEntries.add(new BarEntry(j+1, Integer.valueOf(Scores.get(j))));
        }

        BarDataSet d = new BarDataSet(dataEntries, type + " DataSet");

        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setBarShadowColor(Color.rgb(203, 203, 203));

        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();
        sets.add(d);

        BarData cd = new BarData(sets);
        cd.setBarWidth(0.9f);
        return cd;
    }

    void getOverallTop(final String type) {

        final DatabaseReference currentUser_read = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child(type).child("ScoreHistory");
        currentUser_read.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BarChart barChart = fragData.findViewById(R.id.barChart);

                if (dataSnapshot.child(Integer.toString(1)).exists()) {
                    List<String> Scores = new ArrayList<>(pastHistoryCount);

                    for (int num = 0; num < pastHistoryCount; num ++) {
                    //for (DataSnapshot postpostSnapshot : postSnapshot.getChildren()) {
                        String score = dataSnapshot.child(Integer.toString(num)).getValue().toString();

                        Scores.add(score);
                        Log.i("Aya","My num is " + Integer.toString(num));
                        Log.i("Aya", "Okay here with value " + score);
                        //    }
                    }

                    BarData data;
                    data = generateData(type, Scores);
                    setAddedData(barChart, type, data);

                } else {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(type).child("ScoreHistory").child("0").setValue(0);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(type).child("ScoreHistory").child("1").setValue(0);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(type).child("ScoreHistory").child("2").setValue(0);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(type).child("ScoreHistory").child("3").setValue(0);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(type).child("ScoreHistory").child("4").setValue(0);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
