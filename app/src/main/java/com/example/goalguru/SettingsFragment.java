package com.example.goalguru;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SettingsFragment extends Fragment {

    private MaterialCalendarView calendarView;
    private RecyclerView historyList;
    private HistoryAdapter historyAdapter;
    private List<HistoryItem> historyItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        historyList = view.findViewById(R.id.history_list);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                showExpensesForDay(date);
            }
        });
        // Takvimi yapılandırma
        // ...

        // Geçmiş verileri için RecyclerView yapılandırması
        historyItems = new ArrayList<>();
        historyAdapter = new HistoryAdapter(historyItems);
        historyList.setLayoutManager(new LinearLayoutManager(getContext()));
        historyList.setAdapter(historyAdapter);

        // Veritabanından verileri alıp takvime ve geçmiş listesine ekleyin
        loadHistoryData();

        return view;
    }

    private void loadHistoryData() {
        // Firestore'dan verileri almak için sorgu oluşturma
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference historyRef = db.collection("harcamalar");

        historyRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!isAdded() || getContext() == null) {
                    // Fragment bağlı değil, işlem yapmayın veya hata işleme yapın
                    return;
                }

                historyItems.clear();

                // Firestore'dan gelen verileri döngüyle işleme
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    // Firestore belgesini HistoryItem nesnesine dönüştürme
                    HistoryItem historyItem = document.toObject(HistoryItem.class);
                    if (historyItem != null) {
                        historyItems.add(historyItem);

                        // Takvimde harcamaları işaretleme
                        CalendarDay eventDate = CalendarDay.from(historyItem.getDate());
                        Drawable backgroundDrawable = requireContext().getResources().getDrawable(R.drawable.baseline_check_circle_outline_24);
                        calendarView.addDecorator(new EventDecorator(eventDate, backgroundDrawable));
                    }
                }

                // Geçmiş listesini güncelleme
                historyAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Firestore hatası durumunda işlemler
                Toast.makeText(getContext(), "Verileri getirirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showExpensesForDay(CalendarDay calendarDay) {
        // Seçilen günün tarihini alın
        Date selectedDate = calendarDay.getDate();

        // Firestore'dan seçilen güne ait harcamaları sorgulayın
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference expensesRef = db.collection("harcamalar");
        Query query = expensesRef.whereEqualTo("date", selectedDate);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Harcama> expenses = new ArrayList<>();

                // Firestore'dan gelen verileri döngüyle işleme
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    // Firestore belgesini Harcama nesnesine dönüştürme
                    Harcama harcama = document.toObject(Harcama.class);
                    if (harcama != null) {
                        // harcamaAciklama ve harcamaTutar'ı alın
                        String harcamaAciklama = harcama.getHarcamaAciklama();
                        double harcamaTutar = harcama.getHarcamaTutar();

                        // Gerekli işlemleri yapın (örneğin, metin oluşturup listeye ekleyin)
                        String expenseMessage = "- " + harcama.getHarcamaAciklama() + ": " + harcama.getHarcamaTutar() + "\n";

                        // ...

                        expenses.add(harcama);
                    }
                }

                // Günün harcamalarını göstermek için bir dialog oluşturun veya başka bir işlem yapın
                if (expenses.isEmpty()) {
                    Toast.makeText(getContext(), "Seçilen tarih için herhangi bir harcama bulunmamaktadır.", Toast.LENGTH_SHORT).show();
                } else {
                    showExpensesDialog(expenses, selectedDate);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Firestore hatası durumunda işlemler
                Toast.makeText(getContext(), "Harcamaları getirirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showExpensesDialog(List<Harcama> expenses, Date selectedDate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Harcamalar");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(selectedDate);
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Seçilen tarih: ").append(formattedDate).append("\n\n");

        for (Harcama harcama : expenses) {
            String expenseMessage = "- " + harcama.getHarcamaAciklama() + ": " + harcama.getHarcamaTutar() + "\n";
            messageBuilder.append(expenseMessage);
        }

        builder.setMessage(messageBuilder.toString());
        builder.setPositiveButton("Tamam", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private AlertDialog showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
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
        return dialog;
    }


}
