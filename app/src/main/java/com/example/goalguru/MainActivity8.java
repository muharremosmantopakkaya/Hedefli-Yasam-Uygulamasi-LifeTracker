package com.example.goalguru;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity8 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);

        // VKE değerini alıyoruz
        String vke = getIntent().getStringExtra("vke");

        // VKE değerini ve sonucu ekrana yazdırıyoruz
        TextView vkeTextView = findViewById(R.id.vke_textview);
        vkeTextView.setText(vke);

        // Fitness önerilerini ekrana yazdırıyoruz
        TextView fitnessTextView = findViewById(R.id.fitness_textview);
        String fitness = "1. Dumbell antrenmanları yapın\n2. Mekik çekin\n3. Şınav çekin\n4. Barfiks çekin";
        fitnessTextView.setText(fitness);
        String sonuc = vkeTextView.getText().toString();

        if(sonuc.contains("Zayıfsınız")) {
            fitness = "1. Mekik çekin\n2. Barfiks çekin\n3. Koşu bandı\n4. Yürüyüş\n5. Günlük protein alımını arttırın\n6. Daha sık ve az miktarlarda yemeğe özen gösterin";
        } else if (sonuc.contains("Normal kilolusunuz")) {
            fitness = "1. Dumbell antrenmanları yapın\n2. Mekik çekin\n3. Şınav çekin\n4. Barfiks çekin\n5. Kardiyo antrenmanları yapın (koşu, bisiklet, yüzme gibi)\n6. Pilates veya yoga derslerine katılın";
        } else if (sonuc.contains("Fazla kilolusunuz")) {
            fitness = "1. Kardiyo egzersizleri yapın\n2. Plank çalışmaları yapın\n3. Yoga yapın\n4. Pilates yapın\n5. Daha sağlıklı beslenin\n6. Günlük protein alımını arttırın";
        } else if (sonuc.contains("Aşırı kilolusunuz")) {
            fitness = "1. Kardiyo egzersizleri yapın\n2. Plank çalışmaları yapın\n3. Koşu bandı\n4.Yürüyüş\n5. Daha sağlıklı beslenin\n6. Günlük protein alımını arttırın";
        }

        fitnessTextView.setText(fitness);

    }
}
