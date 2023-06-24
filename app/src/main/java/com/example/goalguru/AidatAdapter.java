package com.example.goalguru;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AidatAdapter extends RecyclerView.Adapter<AidatAdapter.AidatViewHolder> {
    private List<Aidat> aidatList;
    private Context mContext;
    private FirebaseFirestore db;
    private AidatDeleteListener deleteListener;
    private AidatUpdateListener updateListener;

    // Aidat silme işlemlerini dinlemek için kullanılan arayüz
    public interface AidatDeleteListener {
        void onAidatDelete(String aidatId);
    }

    // Aidat güncelleme işlemlerini dinlemek için kullanılan arayüz
    public interface AidatUpdateListener {
        void onAidatUpdate(int position, Aidat updatedAidat);
    }

    // Aktivite veya Fragment'ta tanımlanan güncelleme dinleyici örneği set etmek için kullanılan metot

    // AidatAdapter sınıfının yapıcı metodu
    public AidatAdapter(Context context, List<Aidat> aidatList) {
        this.mContext = context;
        this.aidatList = aidatList;
        this.db = FirebaseFirestore.getInstance();
    }

    // Aidat silme dinleyicisini set etmek için kullanılan metot
    public void setAidatDeleteListener(AidatDeleteListener listener) {
        this.deleteListener = listener;
    }

    // Aidat güncelleme dinleyicisini set etmek için kullanılan metot
    public void setAidatUpdateListener(AidatUpdateListener listener) {
        this.updateListener = listener;
    }

    // ViewHolder sınıfı, RecyclerView'da her bir öğeyi temsil eder
    public static class AidatViewHolder extends RecyclerView.ViewHolder {
        public TextView aidatAciklamaTextView;
        public TextView aidatTarihiTextView;
        public TextView aidatTutariTextView;

        // ViewHolder'ın yapıcı metodu
        public AidatViewHolder(View itemView) {
            super(itemView);
            aidatAciklamaTextView = itemView.findViewById(R.id.aidat_aciklama_edittext);
            aidatTarihiTextView = itemView.findViewById(R.id.aidat_tarihi_edittext);
            aidatTutariTextView = itemView.findViewById(R.id.aidat_tutari_edittext);
        }
    }

    // ViewHolder oluşturulduğunda çağrılan metot
    @NonNull
    @Override
    public AidatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ViewHolder'ı oluşturmak için layout dosyasını şişirir
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.aidat_item, parent, false);
        AidatViewHolder viewHolder = new AidatViewHolder(view);
        setupLongClickListener(viewHolder); // Uzun tıklama dinleyicisini ayarlar
        return viewHolder;
    }

    // ViewHolder'ın içeriğini doldurmak için çağrılan metot
    @Override
    public void onBindViewHolder(@NonNull AidatViewHolder holder, int position) {
        // Belirli bir pozisyondaki aidatı alır
        Aidat currentAidat = aidatList.get(position);
        String aidatAciklama = currentAidat.getAidatAciklama();
        String aidatTarihi = formatDate(currentAidat.getAidatTarihi());
        String aidatTutari = currentAidat.getAidatTutari();

        // Holder'ın içindeki TextView'lere verileri yerleştirir
        holder.aidatTutariTextView.setText("Aidat Tutarı: " + aidatTutari);
        holder.aidatTarihiTextView.setText("Aidat Tarihi: " + aidatTarihi);
        holder.aidatAciklamaTextView.setText("Aidat Açıklama: " + aidatAciklama);

        // Öğeye tıklandığında herhangi bir işlem yapmaz, tıklamayı engeller
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Buraya herhangi bir işlem ekleyebilirsiniz, ancak şu an için boş bıraktım
            }
        });
    }

    // RecyclerView'da gösterilecek öğe sayısını döndüren metot
    @Override
    public int getItemCount() {
        return aidatList != null ? aidatList.size() : 0;
    }


    public void deleteItem(int position) {
        if (position >= 0 && position < aidatList.size()) {
            Aidat deletedAidat = aidatList.get(position);
            if (deletedAidat != null) {
                String aidatAciklama = deletedAidat.getAidatAciklama();
                String aidatTutari = deletedAidat.getAidatTutari();
                Timestamp aidatTarihi = deletedAidat.getAidatTarihi();
                if (aidatAciklama != null) {
                    // Firestore veritabanında "Aidatlar" koleksiyonunda aidatAciklama alanına göre eşleşen belgeleri al ve sil
                    db.collection("Aidatlar")
                            .whereEqualTo("aidatAciklama", aidatAciklama)
                            .whereEqualTo("aidatTutari", aidatTutari)
                            .whereEqualTo("aidatTarihi", aidatTarihi)
                            .limit(1) // Sadece 1 belgeyi getir
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        documentSnapshot.getReference().delete();
                                    }
                                    aidatList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, aidatList.size());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Silme işlemi başarısız olduğunda yapılacak işlemler
                                }
                            });
                } else {
                    Log.d("AidatAdapter", "AidatAciklama null!");
                }
            } else {
                Log.d("AidatAdapter", "DeletedAidat null!");
            }
        } else {
            Log.d("AidatAdapter", "Invalid position! Pozisyon geçerli değil: " + position);
        }
    }

    private String formatDate(Timestamp timestamp) {
        // Verilen bir zaman damgasını belirtilen formatta biçimlendirir
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = timestamp.toDate();
        return dateFormat.format(date);
    }

    public void updateItem(int position, Aidat updatedAidat) {
        // Belirli bir pozisyondaki aidatı al
        Aidat aidat = aidatList.get(position);
        String aidatId = aidat.getAidatId();

        // Güncellenen verileri alıp yeni Aidat nesnesini oluştur
        Aidat updatedAidatWithId = new Aidat();
        updatedAidatWithId.setAidatId(aidatId);
        updatedAidatWithId.setAidatAciklama(updatedAidat.getAidatAciklama());
        updatedAidatWithId.setAidatTutari(updatedAidat.getAidatTutari());
        updatedAidatWithId.setAidatTarihi(updatedAidat.getAidatTarihi());

        // Aidat listesinde belirtilen pozisyondaki öğeyi güncelle
        aidatList.set(position, updatedAidatWithId);
        notifyItemChanged(position);
    }

    private void setupLongClickListener(AidatViewHolder viewHolder) {
        // Uzun tıklama dinleyicisi ayarla
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Pozisyon geçerliyse, pozisyona göre aidatı al
                    Aidat aidat = aidatList.get(position);

                    // Uyarı dialogu oluştur ve düğmelerin tıklama işlemlerini belirle
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext)
                            .setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Sil düğmesine tıklanınca yapılacak işlemler
                                    String aidatId = aidat.getAidatId();
                                    Log.d("AidatAdapter", "Aidat silindi: " + aidatId);

                                    deleteItem(position);
                                }
                            })
                            .setNegativeButton("Güncelle", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Güncelle düğmesine tıklanınca yapılacak işlemler
                                    showUpdateDialog(position);
                                }
                            })
                            .setNeutralButton("Vazgeç", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Vazgeç düğmesine tıklanınca yapılacak işlemler
                                    Log.d("AidatAdapter", "Vazgeç işlemi tıklanan pozisyon: " + position);
                                }
                            });

                    alertDialogBuilder.setTitle("Aidat Bilgileri İçin İşlem Seçiniz");
                    alertDialogBuilder.show();
                    return true;
                }
                return false;
            }
        });
    }


    private void showUpdateDialog(int position) {
        // Belirli bir pozisyondaki aidatı al
        Aidat aidat = aidatList.get(position);
        String aidatAciklama = aidat.getAidatAciklama();
        String aidatTutari = aidat.getAidatTutari();
        Timestamp aidatTarihi = aidat.getAidatTarihi();

        // Takvim ve tarih seçici için gerekli ayarlamalar
        Calendar calendar = Calendar.getInstance();
        Date aidatTarihiDate = aidatTarihi.toDate();
        calendar.setTime(aidatTarihiDate);

        // AlertDialog oluştur
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Aidat Güncelle");

        // Güncelleme için kullanılacak layout dosyasını yükle
        View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.activity_aidat_takip_update, null);

        // EditText bileşenlerini al
        EditText etAidatAciklama = viewInflated.findViewById(R.id.updated_aidat_aciklama_edittext);
        EditText etAidatTutari = viewInflated.findViewById(R.id.updated_aidat_tutari_edittext);
        EditText etAidatTarihi = viewInflated.findViewById(R.id.updated_aidat_tarihi_edittext);

        // EditText bileşenlerine mevcut aidat değerlerini yerleştir
        etAidatAciklama.setText(aidatAciklama);
        etAidatTutari.setText(aidatTutari);
        etAidatTarihi.setText(formatDate(aidatTarihi));

        // Tarih EditText bileşenine tıklandığında tarih seçiciyi göster
        etAidatTarihi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(etAidatTarihi, calendar);
            }
        });

        builder.setView(viewInflated);

        // Onay düğmesi tıklamasında yapılacak işlemleri belirle
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // EditText bileşenlerinden güncellenmiş aidat değerlerini al
                String updatedAidatAciklama = etAidatAciklama.getText().toString();
                String updatedAidatTutari = etAidatTutari.getText().toString();
                String updatedAidatTarihi = etAidatTarihi.getText().toString();

                // Güncellenecek belgeyi bulmak için sorgu yap
                Query query = db.collection("Aidatlar")
                        .whereEqualTo("aidatAciklama", aidatAciklama)
                        .whereEqualTo("aidatTarihi", aidatTarihi)
                        .whereEqualTo("aidatTutari", aidatTutari);

                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Güncellenecek belge bulundu
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String documentId = documentSnapshot.getId();

                            // Güncellenen verileri alıp belgeyi güncelle
                            Map<String, Object> updatedData = new HashMap<>();
                            updatedData.put("aidatAciklama", updatedAidatAciklama);
                            updatedData.put("aidatTutari", updatedAidatTutari);

                            // AidatTarihi'nin güncellenmesi gerekiyorsa, metin alanından alınan veriyi Timestamp'e dönüştür
                            if (!TextUtils.isEmpty(updatedAidatTarihi)) {
                                updatedData.put("aidatTarihi", dateToTimestamp(parseDate(updatedAidatTarihi)));
                            }

                            // Belgeyi güncelle
                            db.collection("Aidatlar").document(documentId)
                                    .update(updatedData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Güncelleme başarılı
                                            Aidat updatedAidat = new Aidat();
                                            updatedAidat.setAidatAciklama(updatedAidatAciklama);
                                            updatedAidat.setAidatTutari(updatedAidatTutari);
                                            if (!TextUtils.isEmpty(updatedAidatTarihi)) {
                                                updatedAidat.setAidatTarihi(dateToTimestamp(parseDate(updatedAidatTarihi)));
                                            }
                                            updateListener.onAidatUpdate(position, updatedAidat);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Güncelleme başarısız
                                            Log.e("UpdateAidat", "Aidat güncelleme hatası", e);
                                        }
                                    });
                        } else {
                            Log.e("UpdateAidat", "Güncellenecek belge bulunamadı. aidatAciklama: " + updatedAidatAciklama
                                    + ", aidatTarihi: " + updatedAidatTarihi + ", aidatTutari: " + updatedAidatTutari);
                        }
                    }
                });

            }
        });

        // İptal düğmesi tıklamasında dialogu kapat
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // AlertDialog'u oluştur ve göster
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // Firebase Timestamp'i Java Date'e dönüştüren metod


    // Java Date'i Firebase Timestamp'e dönüştüren metod
    private Timestamp dateToTimestamp(Date date) {
        if (date != null) {
            return new Timestamp(date);
        }
        return null;
    }

    // Metin formatındaki tarihi Timestamp'e dönüştüren bir yardımcı metot
    public static Timestamp convertToTimestamp(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = dateFormat.parse(dateString);
            return new Timestamp(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Date timestampToDate(Timestamp timestamp) {
        // Verilen bir Timestamp'i Date'e dönüştürür
        if (timestamp != null) {
            return timestamp.toDate();
        }
        return null;
    }


    private void showDatePicker(final EditText editText, final Calendar calendar) {
        // DatePickerDialog ile tarih seçme işlemini gerçekleştirir
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                mContext,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // Ay seçici 0-11 aralığında ay döndürür, bu yüzden 1 artırarak düzeltiyoruz
                        month += 1;

                        // Seçilen tarihi editText'e ayarlayın
                        String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", day, month, year);
                        editText.setText(selectedDate);

                        // Calendar nesnesini güncelleyin
                        calendar.set(year, month - 1, day);

                        // Tarih kontrolü yapın
                        if (calendar.getTimeInMillis() < getMinimumDate()) {
                            Toast.makeText(mContext, "2020'den önceki bir tarih seçemezsiniz.", Toast.LENGTH_SHORT).show();
                            editText.setText("");
                        }
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private long getMinimumDate() {
        // Minimum tarih değerini döndürür
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, Calendar.JANUARY, 1);
        return calendar.getTimeInMillis();
    }


    private Date parseDate(String dateString) {
        // Verilen bir tarih dizesini "dd/MM/yyyy" formatından Date nesnesine dönüştürür
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateAidat(String belgeKimligi, String aidatAciklama, String aidatTarihi, String aidatTutari) {
        // Tarih formatını "dd/MM/yyyy" -> "yyyy-MM-dd" şeklinde dönüştür
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat firestoreDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date;
        try {
            date = inputDateFormat.parse(aidatTarihi);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Geçersiz tarih formatı!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Dönüştürülen tarihi Timestamp'e dönüştür
        Timestamp timestamp = new Timestamp(date);

        // Firestore veritabanına kaydetmek için verileri oluştur
        Map<String, Object> data = new HashMap<>();
        data.put("aidatAciklama", aidatAciklama);
        data.put("aidatTarihi", timestamp);
        data.put("aidatTutari", aidatTutari);

        // Belgeyi güncelle
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Aidatlar").document(belgeKimligi)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("UpdateAidat", "Belge başarıyla güncellendi.");
                        // Güncelleme işlemi başarılıysa, UI'ı güncelleyebilirsiniz (isteğe bağlı)
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("UpdateAidat", "Belge güncelleme hatası: " + e.getMessage());
                        // Güncelleme işlemi başarısız olduğunda hata mesajını gösterebilirsiniz (isteğe bağlı)
                    }
                });
    }
}