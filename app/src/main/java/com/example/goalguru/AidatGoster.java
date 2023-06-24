package com.example.goalguru;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AidatGoster extends AppCompatActivity implements AidatAdapter.AidatUpdateListener, AidatAdapter.AidatDeleteListener {
    private RecyclerView recyclerView;
    private AidatAdapter adapter;
    private List<Aidat> aidatList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidat_goster);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.rcview);
        aidatList = new ArrayList<>();
        adapter = new AidatAdapter(this, aidatList);
        adapter.setAidatUpdateListener(this);
        adapter.setAidatDeleteListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Firestore'dan verileri alın
        fetchAidatlarFromFirestore();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_aidat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_all) {
            showDeleteAllConfirmationDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void showDeleteAllConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tüm Aidatları Sil");
        builder.setMessage("Tüm aidatları silmek istediğinize emin misiniz?");
        builder.setPositiveButton("Evet", (dialog, which) -> {
            deleteAllAidatlar();
        });
        builder.setNegativeButton("Hayır", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
//addOnCompleteListener Task nesnesi, asenkron bir işlemi temsil eder ve bu işlemin tamamlanma durumunu takip etmek ve sonucunu almak için kullanılır. addOnCompleteListener yöntemi,
// Task nesnesinin tamamlandığı zaman çağrılan bir dinleyici eklemek için kullanılır.
    private void deleteAllAidatlar() {
        // Tüm aidatları silme işlemini gerçekleştir
        db.collection("Aidatlar")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> aidatIds = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String aidatId = document.getId();
                            aidatIds.add(aidatId);
                        }

                        deleteAidatlarBatch(aidatIds);
                    } else {
                        // Silme işlemi başarısız olduğunda yapılacak işlemler
                    }
                });
    }

    private void deleteAidatlarBatch(List<String> aidatIds) {
        if (aidatIds.isEmpty()) {
            // Silme işlemi tamamlandığında yapılacak işlemler
            Toast.makeText(this, "Tüm aidatlar silindi.", Toast.LENGTH_SHORT).show();
            // Firestore'dan verileri tekrar alarak güncel listeyi ve RecyclerView'u göster
            fetchAidatlarFromFirestore();
            return;
        }

        String aidatId = aidatIds.get(0);
        aidatIds.remove(0);

        db.collection("Aidatlar")
                .document(aidatId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Aidatı başarıyla sildikten sonra bir sonraki aidata geç
                    deleteAidatlarBatch(aidatIds);
                })
                .addOnFailureListener(e -> {
                    // Silme işlemi başarısız olduğunda yapılacak işlemler
                });
    }
    @Override
    public void onAidatUpdate(int position, Aidat updatedAidat) {
        // Güncelleme talebi alındığında çağrılan yöntem
        // position parametresi, güncellenen öğenin konumunu belirtir
        // updatedAidat parametresi, güncellenen Aidat nesnesini temsil eder
        // Bu yöntem, güncelleme işlemini gerçekleştirmek için updateAidat yöntemini çağırır
        updateAidat(updatedAidat);
    }

    @Override
    public void onAidatDelete(String aidatId) {
        // Silme talebi alındığında çağrılan yöntem
        // aidatId parametresi, silinecek öğenin benzersiz kimliğini belirtir
        // Bu yöntem, silme işlemini gerçekleştirmek için deleteAidat yöntemini çağırır
        deleteAidat(aidatId);
    }


    private void updateAidat(Aidat updatedAidat) {
        // Tarih formatını "dd/MM/yyyy" -> "yyyy-MM-dd" şeklinde dönüştür
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat firestoreDateFormat = new SimpleDateFormat("MMMM d, yyyy 'at' hh:mm:ss a zzz", Locale.getDefault());

        SimpleDateFormat displayDateFormat = new SimpleDateFormat("MMMM d, yyyy 'at' hh:mm:ss a zzz", Locale.getDefault());

        // Güncellenen verileri alın
        String updatedAidatTutari = updatedAidat.getAidatTutari();
        Timestamp updatedAidatTarihi = updatedAidat.getAidatTarihi();
        String updatedAidatAciklama = updatedAidat.getAidatAciklama();

        // Seçilen tarihi kontrol et
        if (updatedAidatTarihi != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(2020, Calendar.JANUARY, 1);
            Timestamp minimumTarih = new Timestamp(calendar.getTime());

            if (updatedAidatTarihi.compareTo(minimumTarih) < 0) {
                // Hata durumunda kullanıcıya uyarı göster
                Toast.makeText(this, "2020'den önceki bir tarih seçemezsiniz.", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            // Hata durumunda kullanıcıya uyarı göster
            Toast.makeText(this, "Geçersiz bir tarih seçildi.", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("Aidatlar")
                .whereEqualTo("aidatAciklama", updatedAidat.getAidatAciklama())
                .whereEqualTo("aidatTarihi", updatedAidat.getAidatTarihi())
                .whereEqualTo("aidatTutari", updatedAidat.getAidatTutari())
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Aidat existingAidat = document.toObject(Aidat.class);
                        if (existingAidat != null) {
                            Log.d("ExistingAidat", "Existing aidatAciklama değeri: " + existingAidat.getAidatAciklama());
                            Log.d("UpdatedAidatAciklama", "Güncellenen aidatAciklama değeri: " + updatedAidatAciklama);

                            existingAidat.setAidatTutari(updatedAidatTutari);
                            existingAidat.setAidatTarihi(updatedAidatTarihi);
                            existingAidat.setAidatAciklama(updatedAidatAciklama);

                            db.collection("Aidatlar")
                                    .document(document.getId())
                                    .set(existingAidat)
                                    .addOnSuccessListener(aVoid -> {
                                        // Güncelleme işlemi başarılı olduğunda yapılacak işlemler
                                        // Firestore'dan verileri tekrar alın
                                        fetchAidatlarFromFirestore();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Güncelleme işlemi başarısız olduğunda yapılacak işlemler
                                        Log.e("UpdateAidat", "Güncelleme işlemi başarısız oldu: " + e.getMessage());
                                    });
                            break; // Sadece belgeyi güncelleyin ve döngüyü sonlandırın
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Firestore sorgusu başarısız oldu
                    // Hata durumunda yapılacak işlemleri buraya ekleyebilirsiniz
                    Log.e("UpdateAidat", "Firestore sorgusu başarısız oldu: " + e.getMessage());
                });
    }




    private void deleteAidat(String aidatId) {
        // Silme işlemini gerçekleştir
        db.collection("Aidatlar")
                .document(aidatId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Silme işlemi başarılı olduğunda yapılacak işlemler
                    // Örneğin, listeden silinen öğeyi de kaldırabilirsiniz
                    int position = -1;
                    for (int i = 0; i < aidatList.size(); i++) {
                        Aidat aidat = aidatList.get(i);
                        if (aidat.getAidatId().equals(aidatId)) {
                            position = i;
                            break;
                        }
                    }
                    if (position != -1) {
                        aidatList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    // Silme işlemi başarısız olduğunda yapılacak işlemler
                });
    }


    // Firestore'dan aidatları almak için kullanılan metot
    private void fetchAidatlarFromFirestore() {
        db.collection("Aidatlar")
                .orderBy("aidatTarihi", Query.Direction.DESCENDING) // aidatTarihi'ne göre azalan sıralama
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        aidatList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Aidat aidat = document.toObject(Aidat.class);
                            aidatList.add(aidat);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        // Firestore'dan verileri alma işlemi başarısız oldu
                    }
                });
    }



}
