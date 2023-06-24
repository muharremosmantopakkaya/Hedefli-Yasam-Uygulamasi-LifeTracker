package com.example.goalguru;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AidatGuncelleActivity extends AppCompatActivity {
    private EditText updatedAidatTarihiEditText;
    private EditText updatedAidatAciklamaEditText;
    private EditText updatedAidatTutariEditText;
    private FirebaseFirestore db;
    private String aidatAciklama;
    private Button guncelleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aidatupdateyeni);

        updatedAidatTarihiEditText = findViewById(R.id.updated_aidat_tarihi_edittext);
        updatedAidatAciklamaEditText = findViewById(R.id.updated_aidat_aciklama_edittext);
        updatedAidatTutariEditText = findViewById(R.id.updated_aidat_tutari_edittext);
        guncelleButton = findViewById(R.id.guncelle_button);

        updatedAidatTarihiEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        db = FirebaseFirestore.getInstance(); // Firestore nesnesini başlatma

        aidatAciklama = getIntent().getStringExtra("aidatId");

        if (aidatAciklama != null && !aidatAciklama.isEmpty()) {
            db.collection("Aidatlar")
                    .whereEqualTo("aidatAciklama", aidatAciklama)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String aidatTarihi = documentSnapshot.getString("aidatTarihi");
                            String aidatAciklama = documentSnapshot.getString("aidatAciklama");
                            String aidatTutari = documentSnapshot.getString("aidatTutari");

                            updatedAidatTarihiEditText.setText(aidatTarihi);
                            updatedAidatAciklamaEditText.setText(aidatAciklama);
                            updatedAidatTutariEditText.setText(aidatTutari);
                        } else {
                            showAlertDialog("Aidat verisi bulunamadı.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        showAlertDialog("Aidat verisi alınırken hata oluştu.");
                    });
        } else {
            showAlertDialog("Aidat ID bulunamadı.");
        }
    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateAidat(String yeniAidatTarihi, String yeniAidatAciklama, String yeniAidatTutari, String aidatAciklama) {
        db.collection("Aidatlar")
                .whereEqualTo("aidatAciklama", aidatAciklama)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        documentSnapshot.getReference().update(
                                        "aidatTarihi", yeniAidatTarihi,
                                        "aidatAciklama", yeniAidatAciklama,
                                        "aidatTutari", yeniAidatTutari
                                )
                                .addOnSuccessListener(aVoid -> {
                                    showAlertDialog("Aidat güncellendi.");
                                })
                                .addOnFailureListener(e -> {
                                    showAlertDialog("Aidat güncellenirken hata oluştu.");
                                });
                    } else {
                        showAlertDialog("Aidat verisi bulunamadı.");
                    }
                })
                .addOnFailureListener(e -> {
                    showAlertDialog("Aidat verisi alınırken hata oluştu.");
                });
    }


    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                calendar.set(selectedYear, selectedMonth, selectedDayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String selectedDate = dateFormat.format(calendar.getTime());

                updatedAidatTarihiEditText.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    public void guncelleAidat(View view) {
        String yeniAidatTarihi = updatedAidatTarihiEditText.getText().toString();
        String yeniAidatAciklama = updatedAidatAciklamaEditText.getText().toString();
        String yeniAidatTutari = updatedAidatTutariEditText.getText().toString();

        updateAidat(yeniAidatTarihi, yeniAidatAciklama, yeniAidatTutari, aidatAciklama);
    }
}
