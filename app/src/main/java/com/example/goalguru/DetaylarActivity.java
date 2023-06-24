package com.example.goalguru;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DetaylarActivity extends AppCompatActivity {
    // Gerekli değişkenleri tanımlayın
    private EditText maasEditText;
    private EditText faturaEditText;
    private EditText digerGiderEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaylar);

        // EditText bileşenlerini ilgili görünümlere bağlayın
        maasEditText = findViewById(R.id.maasEditText);
        faturaEditText = findViewById(R.id.faturaEditText);
        digerGiderEditText = findViewById(R.id.digerGiderEditText);
    }

    // İleri butonuna tıklandığında yapılacak işlemleri burada tanımlayın
    public void ileri(View view) {
        // Girdileri alın
        String maasText = maasEditText.getText().toString();
        String faturaText = faturaEditText.getText().toString();
        String digerGiderText = digerGiderEditText.getText().toString();

        // Alınan verileri kullanarak detayları hesaplamak veya başka bir işlem yapmak için kodu buraya ekleyin
    }

    public void hesapla(View view) {
        // Girdileri alın
        String maasText = maasEditText.getText().toString();
        String faturaText = faturaEditText.getText().toString();
        String digerGiderText = digerGiderEditText.getText().toString();

        // Girdilerin doğruluğunu kontrol edin
        if (!maasText.isEmpty() && !faturaText.isEmpty() && !digerGiderText.isEmpty()) {
            // Girdileri float veya double türüne dönüştürün
            float maas = Float.parseFloat(maasText);
            float faturaGideri = Float.parseFloat(faturaText);
            float digerGiderler = Float.parseFloat(digerGiderText);

            // Hesaplama işlemlerini yapın
            float kalanPara = maas - faturaGideri - digerGiderler;

            // Sonucu görüntülemek için TextView bileşenini bulun
            TextView kalanParaTextView = findViewById(R.id.kalanParaValueTextView);
            // Sonucu TextView'e atayın
            kalanParaTextView.setText(String.valueOf(kalanPara));
        } else {
            // Eksik girdi hatasını kullanıcıya bildirin
            Toast.makeText(this, "Lütfen tüm girdileri doldurun.", Toast.LENGTH_SHORT).show();
        }
    }

}
