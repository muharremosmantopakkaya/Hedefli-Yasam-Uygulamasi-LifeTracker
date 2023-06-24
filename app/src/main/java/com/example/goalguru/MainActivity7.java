package com.example.goalguru;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity7 extends AppCompatActivity {

    private TextView diyetBaslikTextView;
    private TextView diyetOneri1TextView;
    private TextView diyetOneri2TextView;
    private TextView diyetOneri3TextView;
    private TextView diyetOneri4TextView;
    private TextView diyetOneri5TextView;
    private TextView diyetOneri6TextView;
    private TextView diyetOneri7TextView;

    public MainActivity7() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        Intent intent = getIntent();
        String diyetTuru = intent.getStringExtra("diyet_turu");

        TextView diyetOneri1TextView = findViewById(R.id.diyet_oneri_1_textview);
        TextView diyetOneri2TextView = findViewById(R.id.diyet_oneri_2_textview);
        TextView diyetOneri3TextView = findViewById(R.id.diyet_oneri_3_textview);
        TextView diyetOneri4TextView = findViewById(R.id.diyet_oneri_4_textview);
         TextView diyetOneri5TextView=findViewById(R.id.diyet_oneri_5_textview);
         TextView diyetOneri6TextView=findViewById(R.id.diyet_oneri_6_textview);
         TextView diyetOneri7TextView=findViewById(R.id.diyet_oneri_7_textview);

        if (diyetTuru != null) {
            if (diyetTuru.equals("kiloverme")) {
                diyetOneri1TextView.setText("1. Sabah kahvaltınızda protein ağırlıklı yiyecekler tüketin (örneğin yulaf ezmesi, yoğurt, tam buğday ekmeği)");
                diyetOneri2TextView.setText("2. Öğle ve akşam yemeklerinde sebzeler öğünün yarısını oluştursun");
                diyetOneri3TextView.setText("3. Ara öğünlerde meyve, badem gibi sağlıklı atıştırmalıklar tüketin");
                diyetOneri4TextView.setText("4. Haftada en az 3 kez egzersiz yapın");
                diyetOneri5TextView.setText("5. Acı biber tüketin. ");
                diyetOneri6TextView.setText("6. Düzenli su için ve uykusuzluktan kaçının.");
                diyetOneri7TextView.setText("7. Yeşil çay için ve tuzu hayatınızdan çıkarın");

            } else if (diyetTuru.equals("kilokontrol")) {
                diyetOneri1TextView.setText("1. Sabah kahvaltınızı atlamayın ve dengeli bir şekilde beslenin");
                diyetOneri2TextView.setText("2. Porsiyonları kontrol altında tutun ve abur cubur tüketimini azaltın");
                diyetOneri3TextView.setText("3. Düzenli olarak egzersiz yapın ve aktif bir yaşam tarzı benimseyin");
                diyetOneri4TextView.setText("4. Su tüketimine özen gösterin ve sağlıklı içecekleri tercih edin");
                diyetOneri5TextView.setText("5. Her gün 1 adet yumurta yiyin.");
                diyetOneri6TextView.setText("6. Peynir, süt, yoğurt, ayran gibi proteinden zengin besinlerin tam yağlılarını tercih edin");
                diyetOneri7TextView.setText("7.Kahvaltıda kalori değeri yüksek tereyağı, kaymak gibi besinlere yer verin.");
            }
        }

    }

    public void geriii(View view){
        Intent intent = new Intent(MainActivity7.this, MainActivity3.class);
        startActivity(intent);
    }

}