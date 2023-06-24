package com.example.goalguru;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TakvimFragment extends Fragment {

    private CalendarView calendarView;
    private DatabaseReference databaseRef;
    private SimpleDateFormat dateFormat;

    public TakvimFragment() {
        // Boş kurucu metot
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_takvim, container, false);
        Button btnGeri = view.findViewById(R.id.btnGeri);
        btnGeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Geri butonuna tıklandığında yapılacak işlemleri buraya yazın
                Intent intent = new Intent(getActivity(), bottom.class);
                startActivity(intent);
            }
        });
        calendarView = view.findViewById(R.id.calendarView);
        databaseRef = FirebaseDatabase.getInstance().getReference();

        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Takvime tıklanma olayını dinleyin
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Seçilen tarihi alın
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                Date selectedDate = calendar.getTime();

                // Veritabanından seçilen tarihe ait gelir ve gider verilerini alın
                showIncomeAndExpenseForDate(selectedDate);
            }
        });

        return view;
    }

    private void showIncomeAndExpenseForDate(Date selectedDate) {
        // Firebase veritabanından gelirler ve giderler düğümüne erişin ve ilgili güne ait verileri alın
        DatabaseReference incomeRef = FirebaseDatabase.getInstance().getReference().child("gelirler");
        DatabaseReference expenseRef = FirebaseDatabase.getInstance().getReference().child("giderler");

        // Seçilen tarihi kullanarak sorgu yapın
        String selectedDateString = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate);
        Query incomeQuery = incomeRef.orderByChild("tarih").equalTo(selectedDateString);
        Query expenseQuery = expenseRef.orderByChild("tarih").equalTo(selectedDateString);

        incomeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Gelir verilerini işleyin ve alert olarak gösterin
                    StringBuilder incomeData = new StringBuilder();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Gelir verisini alın
                        String tarih = snapshot.child("tarih").getValue(String.class);
                        String tutar = snapshot.child("tutar").getValue(String.class);
                        String kategori = snapshot.child("kategori").getValue(String.class);
                        String hesap = snapshot.child("hesap").getValue(String.class);
                        String not = snapshot.child("not").getValue(String.class);

                        // Gelir verisini alert mesajına ekleyin
                        incomeData.append("Tarih: ").append(tarih).append("\n");
                        incomeData.append("Tutar: ").append(tutar).append("\n");
                        incomeData.append("Kategori: ").append(kategori).append("\n");
                        incomeData.append("Hesap: ").append(hesap).append("\n");
                        incomeData.append("Not: ").append(not).append("\n\n");


                    }

                    // Gelir verilerini alert olarak gösterin
                    isLastAlert = false;
                    showAlert("Gelirler", incomeData.toString());
                } else {
                    // Veri bulunamazsa uygun mesajı gösterin
                    showAlert("Gelirler", "Seçilen tarihe ait gelir bulunamadı.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Veritabanı hatası durumunda uygun mesajı gösterin
                showAlert("Hata", "Veritabanı hatası: " + databaseError.getMessage());
            }
        });

        expenseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Gider verilerini işleyin ve alert olarak gösterin
                    StringBuilder expenseData = new StringBuilder();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Gider verisini alın
                        String tarih = snapshot.child("tarih").getValue(String.class);
                        String tutar = snapshot.child("tutar").getValue(String.class);
                        String kategori = snapshot.child("kategori").getValue(String.class);
                        String hesap = snapshot.child("hesap").getValue(String.class);
                        String not = snapshot.child("not").getValue(String.class);

                        // Gider verisini alert mesajına ekleyin
                        expenseData.append("Tarih: ").append(tarih).append("\n");
                        expenseData.append("Tutar: ").append(tutar).append("\n");
                        expenseData.append("Kategori: ").append(kategori).append("\n");
                        expenseData.append("Hesap: ").append(hesap).append("\n");
                        expenseData.append("Not: ").append(not).append("\n\n");
                    }

                    // Gider verilerini alert olarak gösterin
                    isLastAlert = true;
                    showAlert("Giderler", expenseData.toString());
                } else {
                    // Veri bulunamazsa uygun mesajı gösterin
                    showAlert("Giderler", "Seçilen tarihe ait gider bulunamadı.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Veritabanı hatası durumunda uygun mesajı gösterin
                showAlert("Hata", "Veritabanı hatası: " + databaseError.getMessage());
            }
        });
    }
    private boolean isLastAlert = false;

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (isLastAlert) {
                            // Son alert ise, takvim ekranını göster
                            showCalendarScreen();
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showCalendarScreen() {

    }


}
