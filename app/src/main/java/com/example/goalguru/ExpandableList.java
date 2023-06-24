package com.example.goalguru;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ExpandableList extends AppCompatActivity {
    ExpandableListView expandableListView;
    CustomExpandableListAdapter expandableListAdapter;
    List<PricePackedModel> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandablelistview);
        setExpandableListView();
    }

    void setExpandableListView() {
        expandableListDataPump();
        expandableListView = findViewById(R.id.expandableListView);
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListDetail.get(groupPosition).getPackedName() + " List Expanded.",
                        Toast.LENGTH_SHORT);
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListDetail.get(groupPosition).getPackedName() + " List Collapsed.",
                        Toast.LENGTH_SHORT);

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        expandableListDetail.get(groupPosition).getPackedName()
                                + " -> "
                                + expandableListDetail.get(groupPosition).getId().get(
                                childPosition), Toast.LENGTH_SHORT
                );
                return false;
            }
        });
    }

    public void expandableListDataPump() {
        expandableListDetail = new ArrayList<>();
        List<String> aylık = new ArrayList<>();
        aylık.add("29.90");
        List<String> yıllık = new ArrayList<>();
        yıllık.add("17.90");
        List<String> promo = new ArrayList<>();
        promo.add("Promosyon");
        List<String> deneme = new ArrayList<>();
        deneme.add("Ücretsiz Denemeye Başla"); // Düzeltme

        expandableListDetail.add(new PricePackedModel("Promosyon Kodu Gir", "", promo));
        expandableListDetail.add(new PricePackedModel("AYLIK PAKET", "17.99", aylık));
        expandableListDetail.add(new PricePackedModel("YILLIK PAKET", "13.99", yıllık));
        expandableListDetail.add(new PricePackedModel("DENEME PAKETİ", "ücretsiz", deneme));
    }


}
