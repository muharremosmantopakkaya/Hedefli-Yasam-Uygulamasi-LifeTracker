package com.example.goalguru;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class Bar extends AppCompatActivity {

    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);

        barChart = findViewById(R.id.bar_chart);

        // Verileri oluşturun ve ekleyin
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        // Verileri kullanarak BarDataSet oluşturun
        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        // BarDataSet'i BarData objesine ekleyin
        BarData data = new BarData(set);
        // BarChart'a BarData'yı atayın
        barChart.setData(data);
        // Grafiği görüntüleyin
        barChart.invalidate();
    }
}
