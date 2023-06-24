package com.example.goalguru;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainActivity3 extends AppCompatActivity {

    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // Create the DecimalFormat with the appropriate Locale
        decimalFormat = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.US));
    }

    public void hesapla(View view) {
        EditText hedefEditText = findViewById(R.id.hedef_edittext);
        EditText mevcutEditText = findViewById(R.id.mevcut_edittext);
        TextView sonucTextView = findViewById(R.id.sonuc_textview);

        String hedefText = hedefEditText.getText().toString();
        String mevcutText = mevcutEditText.getText().toString();

        if (hedefText.isEmpty() || mevcutText.isEmpty()) {
            // Uyarı verin veya Toast gösterin
            Toast.makeText(MainActivity3.this, "Lütfen hedef ve mevcut kilonuzu giriniz.", Toast.LENGTH_SHORT).show();
            return;
        }

        double hedefDeger = Double.parseDouble(hedefText.replace(",", "."));
        double mevcutDeger = Double.parseDouble(mevcutText.replace(",", "."));

        double sonuc = mevcutDeger - hedefDeger;

        String sonucText = "Fark: " + decimalFormat.format(sonuc);

        if (hedefDeger > mevcutDeger)
            sonucText += "\nHedef kilonuza ulaşmak için kilo almanız gerekmektedir";
        else if (hedefDeger < mevcutDeger)
            sonucText += "\nHedef kilonuza ulaşmak için kilo vermeniz gerekmektedir";
        else
            sonucText += "\nHedef kilonuza ulaştınız";

        sonucTextView.setText(sonucText);
    }

    public void diyetAnaliziYap(View view) {
        EditText hedefEditText = findViewById(R.id.hedef_edittext);
        EditText mevcutEditText = findViewById(R.id.mevcut_edittext);
        String hedefText = hedefEditText.getText().toString();
        String mevcutText = mevcutEditText.getText().toString();

        if (hedefText.isEmpty() || mevcutText.isEmpty()) {
            // Uyarı verin veya Toast gösterin
            Toast.makeText(MainActivity3.this, "Lütfen hedef ve mevcut kilonuzu giriniz.", Toast.LENGTH_SHORT).show();
            return;
        }

        double hedefDeger = Double.parseDouble(hedefText.replace(",", "."));
        double mevcutDeger = Double.parseDouble(mevcutText.replace(",", "."));

        String diyet;
        if (hedefDeger < mevcutDeger) {
            diyet = "kiloverme";
        } else {
            diyet = "kilokontrol";
        }

        Intent intent = new Intent(MainActivity3.this, MainActivity7.class);
        intent.putExtra("diyet_turu", diyet);
        startActivity(intent);
    }
    public void ilerigit(View view) {
        EditText hedefEditText = findViewById(R.id.hedef_edittext);
        EditText mevcutEditText = findViewById(R.id.mevcut_edittext);
        String hedefText = hedefEditText.getText().toString();
        String mevcutText = mevcutEditText.getText().toString();

        if (hedefText.isEmpty() || mevcutText.isEmpty()) {
            // Uyarı verin veya Toast gösterin
            Toast.makeText(MainActivity3.this, "Lütfen hedef ve mevcut kilonuzu giriniz.", Toast.LENGTH_SHORT).show();
            return;
        }

        double hedefDeger = Double.parseDouble(hedefText.replace(",", "."));
        double mevcutDeger = Double.parseDouble(mevcutText.replace(",", "."));

        String diyet;
        if (hedefDeger < mevcutDeger) {
            diyet = "kiloverme";
        } else {
            diyet = "kilokontrol";
        }

        Intent intent = new Intent(MainActivity3.this, MainActivity7.class);
        intent.putExtra("diyet_turu", diyet);
        startActivity(intent);
    }


    public void gerigit(View view) {
        Intent intent = new Intent(MainActivity3.this, MainActivity5.class);
        startActivity(intent);
    }
    // Rest of the code...
}
