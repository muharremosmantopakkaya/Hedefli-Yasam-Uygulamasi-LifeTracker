package com.example.goalguru;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity4 extends AppCompatActivity implements HarcamaAdapter.OnHarcamaLongClickListener {

    private static final String TAG = "MainActivity4";
    private EditText harcamaTutarEditText;
    private EditText harcamaAciklamaEditText;
    private Button harcamaEkleButton;
    private RecyclerView harcamaRecyclerView;
    private FirebaseFirestore db;
    private Date selectedDate;

    private HarcamaAdapter harcamaAdapter;
    private List<Harcama> harcamalar;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        calendarView = findViewById(R.id.calendarView);
        RecyclerView harcamaRecyclerView = findViewById(R.id.harcama_recyclerview);
        harcamaRecyclerView.addOnItemTouchListener(new DisableItemClick());
        // Takvimden tarih seçildiğinde
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                // Seçilen tarihi selectedDate değişkenine atayın
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(Calendar.YEAR, year);
                selectedCalendar.set(Calendar.MONTH, month);
                selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                selectedCalendar.set(Calendar.HOUR_OF_DAY, 0);
                selectedCalendar.set(Calendar.MINUTE, 0);
                selectedCalendar.set(Calendar.SECOND, 0);
                selectedCalendar.set(Calendar.MILLISECOND, 0);
                selectedDate = selectedCalendar.getTime();

                // Harcamaları listeleme işlemini çağırın
                harcamalariListele();
            }
        });

        harcamalar = new ArrayList<>();

        harcamaRecyclerView = findViewById(R.id.harcama_recyclerview);
        harcamaRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        harcamaAdapter = new HarcamaAdapter(harcamalar);

        harcamaRecyclerView.setAdapter(harcamaAdapter);

        // Firebase Firestore bağlantısını kur
        db = FirebaseFirestore.getInstance();

        // View nesnelerini bağla
        harcamaTutarEditText = findViewById(R.id.harcama_tutar_edittext);
        harcamaAciklamaEditText = findViewById(R.id.harcama_aciklama_edittext);
        harcamaEkleButton = findViewById(R.id.harcama_ekle_button);

        // Harcama ekleme işlemi için listener ekle
        harcamaEkleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                harcamaEkle();
            }
        });

        // Harcamaları listele
        harcamalariListele();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_all) {
            // Doğrulama için bir iletişim kutusu göster
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Silme Onayı");
            builder.setMessage("Tüm harcamaları silmek istediğinize emin misiniz?");
            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Tüm verileri silme işlemini yap
                    CollectionReference harcamalarRef = db.collection("harcamalar");

                    harcamalarRef.get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    // Tüm harcamaları sil
                                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot document : documents) {
                                        document.getReference().delete();
                                    }

                                    // Verileri sildikten sonra harcamaları tekrar listele
                                    harcamalar.clear();
                                    harcamaAdapter.notifyDataSetChanged();

                                    Toast.makeText(MainActivity4.this, "Tüm harcamalar silindi.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Harcamalar silinirken hata oluştu: ", e);
                                    Toast.makeText(MainActivity4.this, "Harcamalar silinirken hata oluştu.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
            builder.setNegativeButton("Hayır", null);

            // İletişim kutusunu göster
            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }
        else if (id == R.id.action_listele_all) {
            Intent intent = new Intent(this, HarcamaListele.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void harcamalariListele() {
        if (selectedDate != null) {
            db.collection("harcamalar")
                    .whereEqualTo("date", new Timestamp(selectedDate))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                harcamalar.clear(); // Önceki verileri temizle
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Harcama harcama = document.toObject(Harcama.class);
                                    harcamalar.add(harcama);
                                }
                                harcamaAdapter.setHarcamalar(harcamalar); // Adapteri güncelle

                            } else {
                                Log.d(TAG, "Harcamalar çekilemedi: ", task.getException());
                            }
                        }
                    });
        }
    }

    public void gerii(View view) {
        Intent intent = new Intent(MainActivity4.this, bottom.class);
        startActivity(intent);
    }

    private void harcamaEkle() {
        String tutarStr = harcamaTutarEditText.getText().toString();
        String aciklama = harcamaAciklamaEditText.getText().toString();

        if (!tutarStr.isEmpty() && !aciklama.isEmpty()) {
            double tutar = Double.parseDouble(tutarStr);

            // Tarih ve saat bilgisini al
            Date date = new Date(); // Şu anki tarih ve saat

            // Timestamp oluştur
            Timestamp timestamp = new Timestamp(date);

            // Harcama nesnesini oluştur ve timestamp'i ayarla
            Harcama harcama = new Harcama(tutar, aciklama, selectedDate != null ? new Timestamp(selectedDate) : null);

            db.collection("harcamalar")
                    .add(harcama)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(MainActivity4.this, "Harcama başarıyla eklendi.", Toast.LENGTH_SHORT).show();
                            harcamaTutarEditText.setText("");
                            harcamaAciklamaEditText.setText("");

                            // Yeni harcama eklendikten sonra harcamaları tekrar listele
                            harcamalariListele();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Harcama eklenirken hata oluştu: ", e);
                        }
                    });
        } else {
            Toast.makeText(MainActivity4.this, "Lütfen tutar ve açıklama alanlarını doldurun.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onHarcamaLongClick(Harcama harcama) {
        CollectionReference harcamalarRef = db.collection("harcamalar");

        harcamalarRef
                .whereEqualTo("harcamaTutar", harcama.getHarcamaTutar())
                .whereEqualTo("harcamaAciklama", harcama.getHarcamaAciklama())
                .whereEqualTo("date", harcama.getDate())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            documentSnapshot.getReference().delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            // Harcama başarıyla silindiğinde
                                            harcamalar.remove(harcama);
                                            harcamaAdapter.notifyDataSetChanged();
                                            Toast.makeText(MainActivity4.this, "Harcama başarıyla silindi.", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "Harcama silinirken hata oluştu: ", e);
                                            Toast.makeText(MainActivity4.this, "Harcama silinirken hata oluştu.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(MainActivity4.this, "Silinmek istenen harcama bulunamadı.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Harcama alınırken hata oluştu: ", e);
                        Toast.makeText(MainActivity4.this, "Harcama silinirken hata oluştu.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public class DisableItemClick implements RecyclerView.OnItemTouchListener {
        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            return true; // Tıklama olayını engelle
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            // Boş bırak
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            // Boş bırak
        }
    }

}
