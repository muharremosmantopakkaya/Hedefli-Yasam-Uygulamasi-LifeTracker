package com.example.goalguru;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class GraphHedef extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private PieChart pieChart;
    private List<Hedef> hedefList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_hedef);
        pieChart = findViewById(R.id.pie_chart);
        // Firebase veritabanı referansını başlat
        mDatabase = FirebaseDatabase.getInstance().getReference();
        hedefList = new ArrayList<>();
        Button showGraphButton = findViewById(R.id.show_graph_button);
        showGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGraph();
            }
        });

        // Hedefleri oku ve listeye ekle
        mDatabase.child("hedefler").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Hedef hedef = postSnapshot.getValue(Hedef.class);
                    hedefList.add(hedef);
                }

                // PieChart nesnesine referans al
                PieChart pieChart = findViewById(R.id.pie_chart);

                // PieEntry listesi oluştur
                List<PieEntry> entries = new ArrayList<>();

                // Hedefleri gez ve grafiğe ekle
                for (Hedef h : hedefList) {
                    if (h.isCompleted()){
                        entries.add(new PieEntry(1, h.getAd()));
                    }
                }

                // PieDataSet oluştur
                PieDataSet dataSet = new PieDataSet(entries, "Hedefler");
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                // PieData oluştur
                PieData data = new PieData(dataSet);
                Description description = new Description();
                description.setText("Description of the chart");
                pieChart.setDescription(description);

                // Grafiğe verileri ata
                pieChart.setData(data);
                pieChart.setCenterText("Hedefler");
                pieChart.setCenterTextSize(20f);
                pieChart.setCenterTextColor(Color.BLACK);
                pieChart.setHoleRadius(30f);
                pieChart.setTransparentCircleRadius(40f);
                pieChart.getLegend().setEnabled(false);
                pieChart.animateXY(1000, 1000);
                pieChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Throwable databaseError = new Throwable();
                Toast.makeText(GraphHedef.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        ;}
    private void showGraph() {
        // Grafiği görüntüleme işlemleri burada yapılacak
        List<PieEntry> entries = new ArrayList<>();
        for (Hedef h : hedefList) {
            if (h.isCompleted()){
                entries.add(new PieEntry(1, h.getAd()));
            }
        }
        PieDataSet dataSet = new PieDataSet(entries, "Goals");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.animateY(1000);
        pieChart.invalidate();
    };}