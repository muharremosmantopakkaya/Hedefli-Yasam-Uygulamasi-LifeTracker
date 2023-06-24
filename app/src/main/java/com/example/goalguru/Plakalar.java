package com.example.goalguru;

import android.os.Bundle;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;



import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;



import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;



import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;



import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Plakalar extends AppCompatActivity {
    private ListView listViewPlakalar;
    private List<PlakaModel> plakaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plakalar);

        listViewPlakalar = findViewById(R.id.listViewPlakalar);
        plakaList = new ArrayList<>();

        // Şehir ve plaka verilerini ekle
        plakaList.add(new PlakaModel("Adana", "01"));
        plakaList.add(new PlakaModel("Adıyaman", "02"));
        plakaList.add(new PlakaModel("Afyon", "03"));
        plakaList.add(new PlakaModel("Ağrı", "04"));
        plakaList.add(new PlakaModel("Amasya", "05"));
        plakaList.add(new PlakaModel("Ankara", "06"));
        plakaList.add(new PlakaModel("Antalya", "07"));
        plakaList.add(new PlakaModel("Artvin", "08"));
        plakaList.add(new PlakaModel("Aydın", "09"));
        plakaList.add(new PlakaModel("Balıkesir", "10"));
        plakaList.add(new PlakaModel("Bilecik", "11"));
        plakaList.add(new PlakaModel("Bingöl", "12"));
        plakaList.add(new PlakaModel("Bitlis", "13"));
        plakaList.add(new PlakaModel("Bolu", "14"));
        plakaList.add(new PlakaModel("Burdur", "15"));
        plakaList.add(new PlakaModel("Bursa", "16"));
        plakaList.add(new PlakaModel("Çanakkale", "17"));
        plakaList.add(new PlakaModel("Çankırı", "18"));
        plakaList.add(new PlakaModel("Çorum", "19"));
        plakaList.add(new PlakaModel("Denizli", "20"));
        plakaList.add(new PlakaModel("Diyarbakır", "21"));
        plakaList.add(new PlakaModel("Edirne", "22"));
        plakaList.add(new PlakaModel("Elazığ", "23"));
        plakaList.add(new PlakaModel("Erzincan", "24"));
        plakaList.add(new PlakaModel("Erzurum", "25"));
        plakaList.add(new PlakaModel("Eskişehir", "26"));
        plakaList.add(new PlakaModel("Gaziantep", "27"));
        plakaList.add(new PlakaModel("Giresun", "28"));
        plakaList.add(new PlakaModel("Gümüşhane", "29"));
        plakaList.add(new PlakaModel("Hakkari", "30"));
        plakaList.add(new PlakaModel("Hatay", "31"));
        plakaList.add(new PlakaModel("Isparta", "32"));
        plakaList.add(new PlakaModel("İçel", "33"));
        plakaList.add(new PlakaModel("İstanbul", "34"));
        plakaList.add(new PlakaModel("İzmir", "35"));
        plakaList.add(new PlakaModel("Kars", "36"));
        plakaList.add(new PlakaModel("Kastamonu", "37"));
        plakaList.add(new PlakaModel("Kayseri", "38"));
        plakaList.add(new PlakaModel("Kırklareli", "39"));
        plakaList.add(new PlakaModel("Kırşehir", "40"));
        plakaList.add(new PlakaModel("Kocaeli", "41"));
        plakaList.add(new PlakaModel("Konya", "42"));
        plakaList.add(new PlakaModel("Kütahya", "43"));
        plakaList.add(new PlakaModel("Malatya", "44"));
        plakaList.add(new PlakaModel("Manisa", "45"));
        plakaList.add(new PlakaModel("Kahramanmaraş", "46"));
        plakaList.add(new PlakaModel("Mardin", "47"));
        plakaList.add(new PlakaModel("Muğla", "48"));
        plakaList.add(new PlakaModel("Muş", "49"));
        plakaList.add(new PlakaModel("Nevşehir", "50"));
        plakaList.add(new PlakaModel("Niğde", "51"));
        plakaList.add(new PlakaModel("Ordu", "52"));
        plakaList.add(new PlakaModel("Rize", "53"));
        plakaList.add(new PlakaModel("Sakarya", "54"));
        plakaList.add(new PlakaModel("Samsun", "55"));
        plakaList.add(new PlakaModel("Siirt", "56"));
        plakaList.add(new PlakaModel("Sinop", "57"));
        plakaList.add(new PlakaModel("Sivas", "58"));
        plakaList.add(new PlakaModel("Tekirdağ", "59"));
        plakaList.add(new PlakaModel("Tokat", "60"));
        plakaList.add(new PlakaModel("Trabzon", "61"));
        plakaList.add(new PlakaModel("Tunceli", "62"));
        plakaList.add(new PlakaModel("Şanlıurfa", "63"));
        plakaList.add(new PlakaModel("Uşak", "64"));
        plakaList.add(new PlakaModel("Van", "65"));
        plakaList.add(new PlakaModel("Yozgat", "66"));
        plakaList.add(new PlakaModel("Zonguldak", "67"));
        plakaList.add(new PlakaModel("Aksaray", "68"));
        plakaList.add(new PlakaModel("Bayburt", "69"));
        plakaList.add(new PlakaModel("Karaman", "70"));
        plakaList.add(new PlakaModel("Kırıkkale", "71"));
        plakaList.add(new PlakaModel("Batman", "72"));
        plakaList.add(new PlakaModel("Şırnak", "73"));
        plakaList.add(new PlakaModel("Bartın", "74"));
        plakaList.add(new PlakaModel("Ardahan", "75"));
        plakaList.add(new PlakaModel("Iğdır", "76"));
        plakaList.add(new PlakaModel("Yalova", "77"));
        plakaList.add(new PlakaModel("Karabük", "78"));
        plakaList.add(new PlakaModel("Kilis", "79"));
        plakaList.add(new PlakaModel("Osmaniye", "80"));
        plakaList.add(new PlakaModel("Düzce", "81"));



// Diğer şehirleri ve plakaları buraya ekleyebilirsiniz...

        // Diğer şehirleri ve plakaları buraya ekleyin...

        PlakaAdapter adapter = new PlakaAdapter(this, plakaList);
        listViewPlakalar.setAdapter(adapter);

        // ListView'da bir şehir seçildiğinde işlem yapın
        listViewPlakalar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlakaModel selectedPlaka = plakaList.get(position);
                String selectedCity = selectedPlaka.getCity();
                String selectedPlate = selectedPlaka.getPlate();

                // Seçilen şehir ve plakayı kullanıcıya gösterin
                Toast.makeText(Plakalar.this, "Seçilen şehir: " + selectedCity +
                        ", Plaka: " + selectedPlate, Toast.LENGTH_SHORT).show();
            }
        });
        // Kaydırma hızını yavaşlat
        listViewPlakalar.setFriction(ViewConfiguration.getScrollFriction() * 100);
    }
}

