package com.example.goalguru;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

public class MainActivity2 extends AppCompatActivity {


// ...
private EditText maasEditText;

    public void maasAyarla(View view) {
        String maasString = maasEditText.getText().toString();

        if (!maasString.isEmpty()) {
            double maas = Double.parseDouble(maasString);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> data = new HashMap<>();
            data.put("maas", maas);

            db.collection("kullanici_verileri").document("kullanici1")
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity2.this, "Maaş başarıyla kaydedildi.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity2.this, "Maaş kaydedilirken hata oluştu.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private EditText gelirEditText, giderEditText;
    private TextView sonucTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        maasEditText = findViewById(R.id.maasEditText);

        gelirEditText = findViewById(R.id.girdi_edittext);
        giderEditText = findViewById(R.id.gider_edittext);
        sonucTextView = findViewById(R.id.sonuc_textview);
    }
    public void ileri(View view) {
        // İleri butonuna tıklandığında yapılacak işlemler buraya gelecek
        Intent intent = new Intent(MainActivity2.this, DetaylarActivity.class);
        startActivity(intent);
    }

    public void hesapla(View view) {
        String gelirString = gelirEditText.getText().toString();
        String giderString = giderEditText.getText().toString();

        if(!gelirString.isEmpty() && !giderString.isEmpty()) {
            double gelir = Double.parseDouble(gelirString);
            double gider = Double.parseDouble(giderString);
            double sonuc = gelir - gider;

            sonucTextView.setText("Sonuç: " + sonuc);
        } else {
            sonucTextView.setText("Lütfen gelir ve gider değerlerini giriniz.");
        }
    }

    public void geri(View view) {
        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(intent);
    }

    public void detaylar(View view) {
        Intent intent = new Intent(MainActivity2.this, MainActivity4.class);
        startActivity(intent);
    }

    public void ilerii(View view) {
        Intent intent = new Intent(MainActivity2.this, DetaylarActivity.class);
        startActivity(intent);
    }
}
