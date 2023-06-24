package com.example.goalguru;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goalguru.R;

public class MainActivity6 extends AppCompatActivity {

    private EditText boyEditText;
    private EditText kiloEditText;
    private TextView sonucTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        boyEditText = findViewById(R.id.boy_edittext);
        kiloEditText = findViewById(R.id.kilo_edittext);
        sonucTextView = findViewById(R.id.sonuc_textview);

        findViewById(R.id.hesapla_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hesapla();
            }
        });
    }

    private void hesapla() {
        String boyStr = boyEditText.getText().toString();
        String kiloStr = kiloEditText.getText().toString();

        if (boyStr.isEmpty() || kiloStr.isEmpty()) {
            Toast.makeText(this, "Lütfen boy ve kilo değerlerini giriniz", Toast.LENGTH_SHORT).show();
            return;
        }

        double boy = Double.parseDouble(boyStr);
        double kilo = Double.parseDouble(kiloStr);

        double bmi = kilo / (boy * boy);
      //  bmi = bmi * 0.024;
        bmi = bmi*10000;
        String sonuc = "Vücut kitle endeksiniz (BMI): " + bmi + " kg/metrekare";
        if(bmi<18.5) sonuc += "\nSonuç: Zayıfsınız";
        else if(bmi<25) sonuc += "\nSonuç: Normal kilolusunuz";
        else if(bmi<30) sonuc += "\nSonuç: Fazla kilolusunuz";
        else sonuc += "\nSonuç: Aşırı kilolusunuz";
        sonucTextView.setText(sonuc);

         sonuc = "Vücut kitle endeksiniz (BMI): " + String.format("%.1f", bmi) + " kg/metrekare";
        if(bmi<18.5) sonuc += "\nSonuç: Zayıfsınız";
        else if(bmi<25) sonuc += "\nSonuç: Normal kilolusunuz";
        else if(bmi<30) sonuc += "\nSonuç: Fazla kilolusunuz";
        else sonuc += "\nSonuç: Aşırı kilolusunuz";
        sonucTextView.setText(sonuc);
        sonucTextView.setText(sonuc);
    }
    public void gerii(View view){

        Intent intent = new Intent(MainActivity6.this, MainActivity5.class);
        startActivity(intent);
    }

    public void fitness(View view) {
        // VKE değerini alıyoruz
        String vke = sonucTextView.getText().toString();
        if(vke.isEmpty()) {
            Toast.makeText(MainActivity6.this, "Lütfen boy ve kilonuzu giriniz.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity6.this, MainActivity8.class);
        intent.putExtra("vke", vke);
        startActivity(intent);
    }


}



