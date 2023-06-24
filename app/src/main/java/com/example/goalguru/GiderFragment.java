package com.example.goalguru;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GiderFragment extends Fragment {

    private EditText etTarih, etTutar, etKategori, etHesap, etNot;
    private Button btnSave;
    private DatabaseReference databaseRef;
    private CardView cardView;
    private Calendar selectedDate;
    public GiderFragment() {
        // Boş kurucu metot
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gider, container, false);
        Button btnGeri = view.findViewById(R.id.btnGeri);
        btnGeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Geri butonuna tıklandığında yapılacak işlemleri buraya yazın
                Intent intent = new Intent(getActivity(), bottom.class);
                startActivity(intent);
            }
        });

        etTarih = view.findViewById(R.id.etTarih);
        etTutar = view.findViewById(R.id.etTutar);
        etKategori = view.findViewById(R.id.etKategori);
        etHesap = view.findViewById(R.id.etHesap);
        etNot = view.findViewById(R.id.etNot);
        btnSave = view.findViewById(R.id.btnSave);

        // Firebase veritabanı referansını alın
        databaseRef = FirebaseDatabase.getInstance().getReference().child("giderler");
        // Tarih seçme olayını başlatmak için tarih alanına tıklama olayını dinleyin
        etTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        // Kaydet butonunun tıklanma olayını dinleyin
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verileri alın
                String tarih = etTarih.getText().toString();
                String tutar = etTutar.getText().toString();
                String kategori = etKategori.getText().toString();
                String hesap = etHesap.getText().toString();
                String not = etNot.getText().toString();

                // Verileri bir Map nesnesine yerleştirin
                Map<String, Object> giderMap = new HashMap<>();
                giderMap.put("tarih", tarih);
                giderMap.put("tutar", tutar);
                giderMap.put("kategori", kategori);
                giderMap.put("hesap", hesap);
                giderMap.put("not", not);

                // Verileri Firebase veritabanına kaydedin
                databaseRef.push().setValue(giderMap);

                // Verilerin başarıyla kaydedildiğini bildiren bir mesaj gösterin veya istediğiniz işlemleri yapın
                Toast.makeText(getActivity(), "Gider başarıyla kaydedildi!", Toast.LENGTH_SHORT).show();

                // Verileri temizleyin
                clearFields();
            }
        });

        return view;


    }
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String formattedDate = dateFormat.format(selectedDate.getTime());
                        etTarih.setText(formattedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }
    private void clearFields() {
        etTarih.setText("");
        etTutar.setText("");
        etKategori.setText("");
        etHesap.setText("");
        etNot.setText("");
        Toast.makeText(getActivity(), "Veriler temizlendi!", Toast.LENGTH_SHORT).show();
    }



}
