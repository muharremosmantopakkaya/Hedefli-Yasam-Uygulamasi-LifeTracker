package com.example.goalguru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VkeFragment extends Fragment {
    private EditText weightField;
    private EditText heightField;
    private Button calculateButton;
    private TextView resultText;
    private DatabaseReference databaseReference;
    private TextView sonucTextView;
    private Button goToExerciseButton; // Yeni buton tanımı

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Firebase veritabanı referansını al
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vke, container, false);

        weightField = view.findViewById(R.id.weight_field);
        heightField = view.findViewById(R.id.height_field);
        calculateButton = view.findViewById(R.id.calculate_button);
        resultText = view.findViewById(R.id.result_text);
        sonucTextView = view.findViewById(R.id.sonuc_textview);
 // Butonu tanımla

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateVKE();
            }
        });


        return view;
    }

    private void calculateVKE() {
        // Girilen kilo ve boy değerlerini al
        String weight = weightField.getText().toString();
        String height = heightField.getText().toString();

        // Veritabanına kaydetmek için bir VKE nesnesi oluştur
        VKE vke = new VKE(weight, height);

        // Vücut Kitle Endeksi hesapla
        double vkeResult = calculateBMI(weight, height);

        // Sonucu TextView'e göster
        String result = "Vücut kitle endeksiniz (BMI): " + String.format("%.1f", vkeResult) + " kg/m²";
        if(vkeResult < 18.5)
            result += "\nSonuç: Zayıfsınız";
        else if(vkeResult < 25)
            result += "\nSonuç: Normal kilolusunuz";
        else if(vkeResult < 30)
            result += "\nSonuç: Fazla kilolusunuz";
        else
            result += "\nSonuç: Aşırı kilolusunuz";

        sonucTextView.setText(result);

        // Veriyi Firebase veritabanına kaydet
        databaseReference.child("vke").push().setValue(vke);
    }

    private double calculateBMI(String weight, String height) {
        // Vücut Kitle Endeksi hesaplama işlemini gerçekleştir
        double weightValue = Double.parseDouble(weight);
        double heightValue = Double.parseDouble(height);
        double heightInMeters = heightValue / 100.0; // Boyu metreye çevir

        return weightValue / (heightInMeters * heightInMeters);
    }

    private void goToExerciseFragment() {
        // VKE değerini egzersiz fragmentına gönder
        String vkeString = sonucTextView.getText().toString();
        if (vkeString.isEmpty()) {
            Toast.makeText(getActivity(), "Lütfen kilo ve boy değerlerini giriniz.", Toast.LENGTH_SHORT).show();
            return;
        }

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


    }
}
