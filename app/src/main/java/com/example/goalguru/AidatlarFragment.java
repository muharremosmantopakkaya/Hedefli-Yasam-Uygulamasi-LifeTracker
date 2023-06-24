package com.example.goalguru;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.Date;

public class AidatlarFragment extends Fragment {

    private MaterialCalendarView calendarView;
    private FirebaseFirestore firestore;
    private CollectionReference aidatlarCollection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aidatlar, container, false);

        firestore = FirebaseFirestore.getInstance();
        aidatlarCollection = firestore.collection("Aidatlar");
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
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                // Seçilen tarihi al
                Calendar selectedCalendar = date.getCalendar();
                selectedCalendar.set(Calendar.HOUR_OF_DAY, 0);
                selectedCalendar.set(Calendar.MINUTE, 0);
                selectedCalendar.set(Calendar.SECOND, 0);
                selectedCalendar.set(Calendar.MILLISECOND, 0);

                Date selectedDate = selectedCalendar.getTime();
                Timestamp timestamp = new Timestamp(selectedDate);

                // Aidatları Firestore'dan kontrol et
                checkAidat(timestamp);
            }
        });

        return view;
    }

    private void checkAidat(Timestamp selectedDate) {
        Query query = aidatlarCollection.whereEqualTo("aidatTarihi", selectedDate);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        // Belge varsa işlemleri gerçekleştir
                        StringBuilder messageBuilder = new StringBuilder();
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            String aidatAciklama = document.getString("aidatAciklama");
                            String aidatTutari = document.getString("aidatTutari");

                            if (aidatAciklama != null && aidatTutari != null) {
                                messageBuilder.append("Aidat Açıklama: ").append(aidatAciklama).append("\n")
                                        .append("Aidat Tutarı: ").append(aidatTutari).append("\n\n");

                                // Aidat tarihlerini işaretlemek için gerekli kodu buraya ekleyin
                                CalendarDay eventDate = CalendarDay.from(selectedDate.toDate());
                                Drawable backgroundDrawable = getResources().getDrawable(R.drawable.baseline_check_circle_outline_24);
                                calendarView.addDecorator(new EventDecorator(eventDate, backgroundDrawable));
                            }
                        }

                        if (messageBuilder.length() > 0) {
                            String message = messageBuilder.toString().trim();
                            showAlertDialog("Aidat Detayları", message);
                        } else {
                            showAlertDialog("Uyarı", "Seçilen tarihe ait aidat verileri eksiktir.");
                            Log.d("AidatlarFragment", "Aidat verileri eksik");
                        }
                    } else {
                        showAlertDialog("Uyarı", "Seçilen tarihe ait aidat bulunmamaktadır.");
                        Log.d("AidatlarFragment", "Belge bulunamadı");
                    }
                } else {
                    if (task.getException() != null) {
                        showAlertDialog("Hata", "Aidat kontrolü başarısız oldu. Hata: " + task.getException().getMessage());
                        Log.e("AidatlarFragment", "Aidat kontrolü başarısız oldu", task.getException());
                    } else {
                        showAlertDialog("Hata", "Aidat kontrolü başarısız oldu. Bilinmeyen bir hata oluştu.");
                        Log.e("AidatlarFragment", "Aidat kontrolü başarısız oldu. Bilinmeyen hata");
                    }
                }
            }
        });
    }


    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
