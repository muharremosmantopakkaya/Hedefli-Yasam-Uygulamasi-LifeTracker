package com.example.goalguru;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * AidatTakipActivity, aidatların takip edildiği ana etkinlik sınıfıdır.
 * Aidatları görüntülemek, eklemek, güncellemek ve silmek için işlemleri gerçekleştirir.
 */
public class AidatTakipActivity extends AppCompatActivity implements AidatAdapter.AidatDeleteListener {
    // AidatTakipActivity sınıfı, AppCompatActivity sınıfını genişletir ve AidatAdapter.AidatDeleteListener arayüzünü uygular.

    ArrayList<Aidat> Aidatlar = new ArrayList<Aidat>();
    // Aidatlar adında bir ArrayList oluşturuldu ve Aidat sınıfının nesnelerini içerecektir.

    private RecyclerView mAidatRecyclerView;
    // Aidatları görüntülemek için RecyclerView.

    private EditText mAidatEditText;
    // Aidat miktarını girmek için EditText.

    private EditText mAidatTarihEditText;
    // Aidatın tarihini girmek için EditText.

    private EditText mAidatAciklamaEditText;
    // Aidata ilişkin açıklamayı girmek için EditText.

    private TextView mAidatlarTextView;
    // Aidatları görüntülemek için TextView.

    private Calendar selectedDate;
    // Seçili tarihi tutmak için Calendar nesnesi.

    private FirebaseFirestore db;
    // Firestore veritabanı referansını temsil eden FirebaseFirestore nesnesi.

    AidatAdapter aidatAdapter;
    // AidatAdapter sınıfı, Aidatları RecyclerView'da görüntülemek ve işlemek için kullanılır.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ebeveyn sınıfın onCreate() yöntemini çağırarak işlemleri devrediyoruz.

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Başlık çubuğunu gizlemek için pencere özelliği ayarlanıyor.

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Tam ekran modunu etkinleştirmek için pencere özellikleri ayarlanıyor.

        getSupportActionBar().hide();
        // Eylem çubuğunu gizlemek için kullanılır.

        setContentView(R.layout.activity_aidat_takip);
        // activity_aidat_takip layout dosyasını etkinleştirir ve içeriğini görüntüler.

        db = FirebaseFirestore.getInstance();
        // Firebase Firestore referansını alıyoruz.

        mAidatTarihEditText = findViewById(R.id.aidat_tarihi_edittext);
        // activity_aidat_takip içerisindeki aidat_tarihi_edittext öğesini buluyoruz ve referansını alıyoruz.

        mAidatAciklamaEditText = findViewById(R.id.aidat_aciklama_edittext);
        // activity_aidat_takip içerisindeki aidat_aciklama_edittext öğesini buluyoruz ve referansını alıyoruz.

        mAidatEditText = findViewById(R.id.aidat_tutari_edittext);
        // activity_aidat_takip içerisindeki aidat_tutari_edittext öğesini buluyoruz ve referansını alıyoruz.

        mAidatlarTextView = findViewById(R.id.aidatlar_textview);
        // activity_aidat_takip içerisindeki aidatlar_textview öğesini buluyoruz ve referansını alıyoruz.

        mAidatRecyclerView = findViewById(R.id.rcview);
        // activity_aidat_takip içerisindeki rcview RecyclerView öğesini buluyoruz ve referansını alıyoruz.

        mAidatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // RecyclerView için düzenleyici ayarlanıyor. Bu durumda, dikey olarak lineer bir düzen kullanılıyor.

        mAidatRecyclerView.setAdapter(aidatAdapter);
        // RecyclerView'a AidatAdapter'ı ayarlıyoruz.

        mAidatTarihEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePicker();
                    return true;
                }
                return false;
            }
        });
        // mAidatTarihEditText öğesine dokunulduğunda bir tarih seçiciyi göstermek için bir dokunma dinleyici ekliyoruz.

        mAidatRecyclerView.setAdapter(aidatAdapter);
        // RecyclerView'a AidatAdapter'ı ayarlıyoruz.
    }


    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        // Bir Calendar örneği alınır ve mevcut tarihi ve saatini içerir.

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // Calendar nesnesinden yıl, ay ve gün bilgileri alınır.
//Bu satır, DatePickerDialog sınıfının bir nesnesini oluşturur ve bu nesneyi datePickerDialog adıyla tanımlar.
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Kullanıcı bir tarih seçtiğinde çağrılacak olan onDateSet() yöntemi tanımlanır.
                        // Seçilen tarih, DatePickerDialog tarafından sağlanır.

                        calendar.set(year, month, dayOfMonth);
                        // Calendar nesnesinin tarihini seçilen tarihe ayarlar.

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        // Tarih biçimlendirme için SimpleDateFormat kullanılır.
                        // "dd/MM/yyyy" formatında bir tarih biçimi belirlenir.

                        String formattedDate = dateFormat.format(calendar.getTime());
                        // Calendar nesnesinden biçimlendirilmiş tarih elde edilir.

                        mAidatTarihEditText.setText(formattedDate);
                        // Elde edilen tarih, mAidatTarihEditText öğesine atanır ve görüntülenir.
                    }
                },
                year, month, day);
        // DatePickerDialog örneği oluşturulur ve onDateSetListener ile birlikte ayarlanır.
        // Varsayılan olarak, DatePickerDialog, mevcut tarihi kullanarak açılır.

        datePickerDialog.show();
        // DatePickerDialog'u gösterir ve kullanıcıdan bir tarih seçmesini bekler.
    }


    private String formatDate(Date date) {
        // SimpleDateFormat nesnesi oluşturuluyor ve "dd/MM/yyyy" formatı belirtiliyor.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        // Belirtilen formata göre date nesnesini tarih dizesine dönüştürüyor.
        return sdf.format(date);
    }

    public void aidatEkle(View view) {
        EditText aidatTarihiEditText = findViewById(R.id.aidat_tarihi_edittext); // Aidat tarihi EditText'i
        EditText aidatAciklamaEditText = findViewById(R.id.aidat_aciklama_edittext); // Aidat açıklama EditText'i
        EditText aidatTutariEditText = findViewById(R.id.aidat_tutari_edittext); // Aidat tutarı EditText'i

        String aidatTarihiString = aidatTarihiEditText.getText().toString(); // Aidat tarihini String olarak alır
        String aidatAciklama = aidatAciklamaEditText.getText().toString(); // Aidat açıklamasını String olarak alır
        String aidatTutari = aidatTutariEditText.getText().toString(); // Aidat tutarını String olarak alır

        if (TextUtils.isEmpty(aidatTutari) || TextUtils.isEmpty(aidatTarihiString) || TextUtils.isEmpty(aidatAciklama)) {
            // Eğer bir bilgi eksikse, kullanıcıya uyarı verir ve işlemi durdurur
            Toast.makeText(AidatTakipActivity.this, "Lütfen bilgilerinizi tam olarak doldurunuz!", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); // Tarih formatı belirlenir
        Date date;
        try {
            date = inputDateFormat.parse(aidatTarihiString); // Kullanıcının girdiği tarihi verilen formata çevirir
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(AidatTakipActivity.this, "Geçersiz tarih formatı!", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance(); // Takvim oluşturulur
        calendar.set(2020, Calendar.JANUARY, 1); // 2020'den önceki tarihleri temsil eder
        Date minimumDate = calendar.getTime(); // Minimum tarih elde edilir

        if (date.before(minimumDate)) {
            // Seçilen tarih 2020'den önceyse, kullanıcıya uyarı verir ve işlemi durdurur
            Toast.makeText(AidatTakipActivity.this, "2020'den önceki bir tarih seçemezsiniz!", Toast.LENGTH_SHORT).show();
            return;
        }

        Timestamp aidatTarihi = new Timestamp(date); // Tarih bilgisi Timestamp formatına dönüştürülür

        Map<String, Object> aidat = new HashMap<>(); // Firestore'a eklenecek verileri içeren bir Map oluşturulur  HashMap, projelerimizdeki anahtar ve değer çiftlerinden oluşan verilerimizi depolamak için kullanabileceğimiz, collection yapıları içerisinde yer alan, harita tabanlı, Map arayüzünün altında yer alan bir sınıftır
        aidat.put("aidatTarihi", aidatTarihi); // Aidat tarihi eklenir
        aidat.put("aidatAciklama", aidatAciklama); // Aidat açıklaması eklenir
        aidat.put("aidatTutari", aidatTutari); // Aidat tutarı eklenir

        db.collection("Aidatlar") // "Aidatlar" koleksiyonuna erişilir
                .add(aidat) // Veriler Firestore'a eklenir
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {  //Belli bir dosya üzerinde işlemler yapılacağı zaman DocumentReferance kullanılır.
                        // Başarı durumunda kullanıcıya geri bildirim verilir ve EditText alanları temizlenir
                        Toast.makeText(AidatTakipActivity.this, "Aidat başarıyla eklendi!", Toast.LENGTH_SHORT).show();
                        aidatTutariEditText.setText("");
                        aidatTarihiEditText.setText("");
                        aidatAciklamaEditText.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Hata durumunda kullanıcıya geri bildirim verilir
                        Toast.makeText(AidatTakipActivity.this, "Aidat eklenirken hata oluştu!", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    public void aidatlistele(View view) {
        // Metodun içeriği

        Intent intent = new Intent(AidatTakipActivity.this, AidatGoster.class);
 //bir metodun içinde o metodun ait olduğu sınıftan yaratılacak nesneyi veya o nesnenin bir alt değişkenini tanımlamamız gerektiğinde kullandığımız deyime this diyoruz.
        // Yeni bir Intent oluşturulur. Intent, bir aktiviteden diğerine geçişi temsil eder.
        // AidatGoster sınıfına geçiş yapılacak.

        startActivity(intent);
        // Oluşturulan Intent kullanılarak AidatGoster aktivitesi başlatılır ve geçiş yapılır.
    }



    public void aidatGoster(View view) {
        // Metodun içeriği

        db.collection("Aidatlar")
                .orderBy("aidatTarihi", Query.Direction.DESCENDING) // Tarihe göre sıralama (azalan sırada)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments(); // DocumentSnapshot Firebase tarafından size döndürülen veriyi taşıyan bir objedir.
                        // Firebase'dan dönen belgeleri içeren bir liste alınır

                        if (documents.isEmpty()) {
                            Toast.makeText(AidatTakipActivity.this, "Aidat bulunamadı.", Toast.LENGTH_SHORT).show();
                            // Belge yoksa kullanıcıya bilgilendirme mesajı gösterilir
                            return;
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(AidatTakipActivity.this);
                        builder.setTitle("Aidat Bilgileri");
                        // Bildirim kutusu başlığı ayarlanır

                        String[] aidatlar = new String[documents.size()];
                        // Aidat bilgilerini tutmak için bir dizi oluşturulur

                        for (int i = 0; i < documents.size(); i++) {
                            DocumentSnapshot document = documents.get(i);
                            // Belgeye erişim sağlanır

                            String aidatTarihi = "";
                            Object aidatTarihiObj = document.get("aidatTarihi");
                            // Belgeden aidat tarihine erişilir

                            if (aidatTarihiObj instanceof Timestamp) {
                                Timestamp timestamp = (Timestamp) aidatTarihiObj;
                                Date date = timestamp.toDate();
                                aidatTarihi = formatDate(date);
                                // Timestamp verisini tarihe dönüştürmek için formatlama yapılır
                            }

                            String aidatAciklama = document.getString("aidatAciklama");
                            // Belgeden aidat açıklamasına erişilir

                            String aidatTutari = document.getString("aidatTutari");
                            // Belgeden aidat tutarına erişilir

                            String aidatBilgisi = "Aidat Tarihi: " + aidatTarihi + "\n"
                                    + "Aidat Açıklama: " + aidatAciklama + "\n"
                                    + "Aidat Tutarı: " + aidatTutari + "\n\n";
                            // Belgedeki aidat bilgileri birleştirilerek bir metin oluşturulur

                            aidatlar[i] = aidatBilgisi;
                            // Metin, aidatlar dizisine eklenir
                        }

                        builder.setItems(aidatlar, (dialog, which) -> showAidatOptions(documents.get(which).getId()));
                        // Bildirim kutusunda aidatlar dizisi görüntülenir ve seçildiğinde ilgili seçenekler gösterilir

                        builder.setPositiveButton("Tamam", (dialog, which) -> dialog.dismiss());
                        // Bildirim kutusu düğmesi ayarlanır

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        // Bildirim kutusu gösterilir
                    } else {
                        Toast.makeText(AidatTakipActivity.this, "Aidatları getirirken hata oluştu.", Toast.LENGTH_SHORT).show();
                        // Veriler alınırken hata oluşursa kullanıcıya bilgilendirme mesajı gösterilir
                    }
                });
    }


 //Java'da nesne üretmek için kullanılan sözdizimi : ClassName object = new ClassName(); ClassName : Nesne oluşturmak istediğimiz sınıfı belirtiyoruz.
 private void showAidatOptions(String aidatId) {
     // Metodun içeriği

     AlertDialog.Builder builder = new AlertDialog.Builder(AidatTakipActivity.this);
     builder.setTitle("Aidat Seçenekleri");
     // Bildirim kutusu başlığı ayarlanır

     builder.setPositiveButton("Güncelle", (dialog, which) -> {
         // "Güncelle" düğmesine tıklanınca yapılacak işlemler
         // Güncelleme işlemi için bir form veya giriş ekranı açabilirsiniz
         showAidatUpdateDialog(aidatId);
         dialog.dismiss();
         // Bildirim kutusu kapatılır
     });

     builder.setNegativeButton("Sil", (dialog, which) -> {
         // "Sil" düğmesine tıklanınca yapılacak işlemler
         showDeleteConfirmation(aidatId);
         dialog.dismiss();
         // Bildirim kutusu kapatılır
     });

     builder.setNeutralButton("İptal", (dialog, which) -> dialog.dismiss());
     // "İptal" düğmesine tıklanınca bildirim kutusu kapatılır

     AlertDialog alertDialog = builder.create();
     alertDialog.show();
     // Bildirim kutusu gösterilir
 }


    private void showAidatUpdateDialog(String aidatId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AidatTakipActivity.this);
        builder.setTitle("Aidat Güncelleme");

        // Özel layout dosyasını kullanarak bir form veya giriş ekranı tasarlayın
        // XML dosyasından View nesnesi oluşturulur
        View dialogView = getLayoutInflater().inflate(R.layout.activity_aidat_takip_update, null);
        //Layoutinflater sınıfı temel olarak XML objeleri Java sınıflarına çevirir.
        builder.setView(dialogView);

        EditText updatedAidatTarihiEditText = dialogView.findViewById(R.id.updated_aidat_tarihi_edittext);

        //EditText nesnesi, dialogView içindeki "updated_aidat_tarihi_edittext" kimliğine sahip EditText öğesini temsil eder.

        EditText updatedAidatAciklamaEditText = dialogView.findViewById(R.id.updated_aidat_aciklama_edittext);
        EditText updatedAidatTutariEditText = dialogView.findViewById(R.id.updated_aidat_tutari_edittext);

        // Mevcut aidat verilerini alın ve form alanlarına yerleştirin
        //Veriler document adı verilen yapılarda saklanır. Document topluluğuna, collections adı veriliyor.
        db.collection("Aidatlar")
                .document(aidatId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Timestamp aidatTarihi = documentSnapshot.getTimestamp("aidatTarihi");  //DocumentSnapshot Firebase tarafından size döndürülen veriyi taşıyan bir objedir
                        String aidatAciklama = documentSnapshot.getString("aidatAciklama");
                        String aidatTutari = documentSnapshot.getString("aidatTutari");

                        if (aidatTarihi != null) {
                            Date aidatDate = aidatTarihi.toDate();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            //, SimpleDateFormat Java'da tarihleri biçimlendirmek ve ayrıştırmak için kullanılan bir sınıftır
                            String aidatTarihiStr = dateFormat.format(aidatDate);
                            updatedAidatTarihiEditText.setText(aidatTarihiStr);
                        }
                        updatedAidatAciklamaEditText.setText(aidatAciklama);
                        updatedAidatTutariEditText.setText(aidatTutari);

                        // Güncelleme işlemi için onay mesajı göster
                        builder.setPositiveButton("Güncelle", (dialog, which) -> {
                            // Kullanıcı "Evet" seçeneğini seçtiğinde yapılacak işlemler

                            String yeniAidatTarihi = updatedAidatTarihiEditText.getText().toString();
                            String yeniAidatAciklama = updatedAidatAciklamaEditText.getText().toString();
                            String yeniAidatTutari = updatedAidatTutariEditText.getText().toString();

                            // Veriyi güncelle
                            updateAidat(aidatId, yeniAidatTarihi, yeniAidatAciklama, yeniAidatTutari);

                            dialog.dismiss();
                        });

                        // Geri alma işlemi için iptal mesajı göster
                        builder.setNegativeButton("Hayır", (dialog, which) -> {   //Builder pattern, karmaşık nesneleri adım adım oluşturmanıza olanak tanıyan creational bir tasarım desenidir.
                            // Kullanıcı "Hayır" seçeneğini seçtiğinde yapılacak işlemler
                            dialog.dismiss();
                        });

                        // AlertDialog'ı oluştur
                        AlertDialog dialog = builder.create();

                        //builder nesnesi, bir AlertDialog oluşturmak için kullanılan AlertDialog.Builder sınıfının bir örneğidir. AlertDialog.Builder, kullanıcıya iletişim kutuları (dialog) oluşturmak için kullanılan bir yardımcı sınıftır.

                        // Tarih seçiciyi ayarla
                        updatedAidatTarihiEditText.setOnClickListener(v -> showDatePicker(updatedAidatTarihiEditText));



                        dialog.show();

                    }
                })
                .addOnFailureListener(e -> {  //addOnFailureListener bir Firebase Firestore işlemi başarısız olduğunda gerçekleştirilecek eylemleri belirlemek için kullanılan bir yöntemdir.
                    Toast.makeText(AidatTakipActivity.this, "Aidat verisi alınırken hata oluştu.", Toast.LENGTH_SHORT).show();
                    // Hata durumunda yapılacak işlemleri buraya ekleyebilirsiniz
                });
    }

    private void showDatePicker(EditText editText) {
        // Şu anki tarihi al
        Calendar calendar = Calendar.getInstance(); // Şu anki tarihi almak için bir Calendar nesnesi oluşturulur.
        int year = calendar.get(Calendar.YEAR); // Yılı al
        int month = calendar.get(Calendar.MONTH); // Ayı al
        int day = calendar.get(Calendar.DAY_OF_MONTH); // Günü al

        // Tarih seçiciyi aç
        DatePickerDialog datePickerDialog = new DatePickerDialog(AidatTakipActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Seçilen tarihi al
                    calendar.set(selectedYear, selectedMonth, selectedDay); // Seçilen tarihi Calendar nesnesine ayarla

                    // Tarih formatını "dd/MM/yyyy" şeklinde ayarla
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String selectedDate = dateFormat.format(calendar.getTime()); // Biçimlendirilmiş tarihi al

                    // Seçilen tarihi EditText alanına yerleştir
                    editText.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show(); // Tarih seçiciyi görüntüler, kullanıcıya tarih seçme imkanı sağlar.
    }



    private void showDeleteConfirmation(String aidatId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AidatTakipActivity.this); // AlertDialog oluşturmak için bir Builder nesnesi oluşturulur
        builder.setTitle("Aidat Silme Onayı") // İletişim kutusu başlığını ayarlar
                .setMessage("Seçili aidatı silmek istediğinize emin misiniz?") // İletişim kutusu içeriğini ayarlar
                .setPositiveButton("Evet", (dialog, which) -> {
                    deleteAidat(aidatId); // Evet düğmesine tıklandığında yapılacak işlem: Seçili aidatı sil
                    dialog.dismiss(); // İletişim kutusunu kapat
                })
                .setNegativeButton("Hayır", (dialog, which) -> dialog.dismiss()) // Hayır düğmesine tıklandığında yapılacak işlem: İletişim kutusunu kapat
                .show(); // İletişim kutusunu göster
    }


    private void updateAidat(String aidatId, String yeniAidatTarihi, String yeniAidatAciklama, String yeniAidatTutari) {
        // Tarih değerini kontrol etmek için SimpleDateFormat ve Calendar kullanın
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date tarih = dateFormat.parse(yeniAidatTarihi);
            Calendar calendar = Calendar.getInstance();
            calendar.set(2020, Calendar.JANUARY, 1); // 2020'den önceki tarihleri temsil eder
            Date minimumDate = calendar.getTime();

            // Eğer tarih 2020'den önceyse uyarı göster
            if (tarih.before(minimumDate)) {
                Toast.makeText(AidatTakipActivity.this, "2020'den önceki bir tarih seçemezsiniz!", Toast.LENGTH_SHORT).show();
                return; // Uyarıyı gösterdikten sonra işlemi sonlandır
            }

            // Tarih değeri geçerli ise güncelleme işlemini devam ettirin

            // Yeni tarih değerini Timestamp formatına dönüştürün
            Timestamp yeniTimestamp = new Timestamp(tarih);

            // Güncellenecek verileri bir harita (Map) nesnesine yerleştirin
            Map<String, Object> updatedAidat = new HashMap<>();
            updatedAidat.put("aidatTarihi", yeniTimestamp);
            updatedAidat.put("aidatAciklama", yeniAidatAciklama);
            updatedAidat.put("aidatTutari", yeniAidatTutari);

            // Firestore veritabanında güncelleme işlemini gerçekleştirin
            db.collection("Aidatlar")
                    .document(aidatId)
                    .update(updatedAidat)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AidatTakipActivity.this, "Aidat başarıyla güncellendi!", Toast.LENGTH_SHORT).show();
                        aidatGoster(null); // Aidatları tekrar göstermek için çağırabilirsiniz
                        // Güncelleme işlemi tamamlandığında yapılacak işlemleri buraya ekleyebilirsiniz
                        // Örneğin, dialog penceresini kapatmak veya listedeki verileri yenilemek gibi işlemler yapılabilir
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AidatTakipActivity.this, "Aidat güncellenirken hata oluştu.", Toast.LENGTH_SHORT).show();
                        // Hata durumunda yapılacak işlemleri buraya ekleyebilirsiniz
                    });
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(AidatTakipActivity.this, "Tarih formatı geçersiz.", Toast.LENGTH_SHORT).show();
        }
    }





    public void deleteAidat(String aidatId) {
        db.collection("Aidatlar")
                .document(aidatId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AidatTakipActivity.this, "Aidat başarıyla silindi!", Toast.LENGTH_SHORT).show();
                    aidatGoster(null); // Aidatları tekrar göstermek için çağırabilirsiniz
                })
                .addOnFailureListener(e -> Toast.makeText(AidatTakipActivity.this, "Aidatları silerken hata oluştu.", Toast.LENGTH_SHORT).show());
    }


    public void aidatSil(View view) {
        db.collection("Aidatlar")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        if (documents.isEmpty()) {
                            Toast.makeText(AidatTakipActivity.this, "Silinecek aidat bulunamadı.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(AidatTakipActivity.this); // AlertDialog oluşturmak için bir Builder nesnesi oluşturulur
                        builder.setTitle("Aidat Silme") // İletişim kutusu başlığını ayarlar
                                .setMessage("Tüm aidatları silmek istediğinize emin misiniz?") // İletişim kutusu içeriğini ayarlar
                                .setPositiveButton("Evet", (dialog, which) -> {
                                    // Tüm aidatları sil
                                    for (DocumentSnapshot document : documents) {
                                        String aidatId = document.getId();
                                        db.collection("Aidatlar")
                                                .document(aidatId)
                                                .delete();
                                    }
                                    Toast.makeText(AidatTakipActivity.this, "Tüm aidatlar başarıyla silindi!", Toast.LENGTH_SHORT).show();
                                    mAidatlarTextView.setVisibility(View.GONE);
                                })
                                .setNegativeButton("Hayır", (dialog, which) -> dialog.dismiss()) // Hayır düğmesine tıklandığında yapılacak işlem: İletişim kutusunu kapat
                                .show(); // İletişim kutusunu göster
                    } else {
                        Toast.makeText(AidatTakipActivity.this, "Aidatları getirirken hata oluştu.", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    public void aidatGuncelle(View view) {
        EditText aidatTarihiEditText = findViewById(R.id.aidat_tarihi_edittext);
        EditText aidatAciklamaEditText = findViewById(R.id.aidat_aciklama_edittext);
        EditText aidatTutariEditText = findViewById(R.id.aidat_tutari_edittext);

        String aidatTarihi = aidatTarihiEditText.getText().toString();
        String aidatAciklama = aidatAciklamaEditText.getText().toString();
        String aidatTutari = aidatTutariEditText.getText().toString();

        if (TextUtils.isEmpty(aidatTutari) || TextUtils.isEmpty(aidatTarihi) || TextUtils.isEmpty(aidatAciklama)) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tarih formatını "dd/MM/yyyy" -> "yyyy-MM-dd" şeklinde dönüştür
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat firestoreDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date;
        try {
            date = inputDateFormat.parse(aidatTarihi);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Geçersiz tarih formatı!", Toast.LENGTH_SHORT).show();
            return;
        }

        String firestoreFormattedDate = firestoreDateFormat.format(date);
        Timestamp timestamp = new Timestamp(date);

        // Tüm aidat belgelerini alarak en son tarihi bulma
        db.collection("Aidatlar")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult(); //QuerySnapshot, Firestore sorgusu sonucunda dönen bir sonuç kümesini temsil eden bir Firestore sınıfıdır.
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            List<DocumentSnapshot> aidatBelgeleri = querySnapshot.getDocuments(); // DocumentSnapshot Firebase tarafından size döndürülen veriyi taşıyan bir objedir
                            DocumentSnapshot enSonAidat = null;
                            Date enSonTarih = null;

                            // En son tarihi bulma
                            for (DocumentSnapshot belge : aidatBelgeleri) {
                                Timestamp belgeTarihi = belge.getTimestamp("aidatTarihi");
                                if (belgeTarihi != null) {
                                    Date belgeDate = belgeTarihi.toDate();

                                    if (enSonTarih == null || (belgeDate != null && belgeDate.after(enSonTarih))) {
                                        enSonTarih = belgeDate;
                                        enSonAidat = belge;
                                    }
                                }
                            }

                            if (enSonAidat != null) {
                                String selectedAidatId = enSonAidat.getId();

                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setTitle("Aidat Güncelleme")
                                        .setMessage("Son tarihli aidatı güncellemek istediğinize emin misiniz?")
                                        .setPositiveButton("Evet", (dialog, which) -> {
                                            // Aidatı güncelle
                                            Map<String, Object> updatedAidat = new HashMap<>();
                                            //updatedAidat adında bir HashMap nesnesi oluşturuluyor. updatedAidat, güncellenecek aidatın yeni verilerini içeren bir koleksiyon olarak kullanılacak.
                                            //HashMap veri yapısı, anahtar-değer çiftlerini depolamak için kullanılır. Burada anahtarlar String türünde olacak ve değerler Object türünde olacak. Yani, her anahtarın bir değeri vardır. updatedAidat nesnesi, güncellenen aidatın yeni aidatTarihi, aidatAciklama ve aidatTutari değerlerini içerir.
                                            updatedAidat.put("aidatTarihi", timestamp);
                                            updatedAidat.put("aidatAciklama", aidatAciklama);
                                            updatedAidat.put("aidatTutari", aidatTutari);

                                            db.collection("Aidatlar")
                                                    .document(selectedAidatId)
                                                    .update(updatedAidat)
                                                    .addOnSuccessListener(aVoid -> {
                                                        aidatGoster(null); // Aidatları tekrar göstermek için çağırabilirsiniz
                                                        Toast.makeText(this, "Aidat başarıyla güncellendi!", Toast.LENGTH_SHORT).show();

                                                        // Aşağıdaki satırlar, güncelleme işlemi başarılı olduğunda EditText alanlarındaki verileri temizlemek için kullanılıyor.
                                                        // Eğer güncelleme işleminden sonra EditText alanlarını temizlemek istiyorsanız bu satırları kullanabilirsiniz.
                                                        aidatTarihiEditText.setText("");
                                                        aidatAciklamaEditText.setText("");
                                                        aidatTutariEditText.setText("");

                                                    })
                                                    .addOnFailureListener(e -> Toast.makeText(this, "Aidat güncellenirken hata oluştu.", Toast.LENGTH_SHORT).show());
                                        })
                                        .setNegativeButton("Hayır", (dialog, which) -> dialog.dismiss())
                                        .show();
                            } else {
                                Toast.makeText(this, "Güncellenecek aidat bulunamadı.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Güncellenecek aidat bulunamadı.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                        Toast.makeText(this, "Aidat güncellenirken hata oluştu.", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    public void aidatgeri(View view) {
        Intent intent = new Intent(AidatTakipActivity.this, bottom.class);
        //bir metodun içinde o metodun ait olduğu sınıftan yaratılacak nesneyi veya o nesnenin bir alt değişkenini tanımlamamız gerektiğinde kullandığımız deyime this diyoruz.
        // Yeni bir Intent oluşturulur. Intent, bir aktiviteden diğerine geçişi temsil eder.
        // AidatGoster sınıfına geçiş yapılacak.
        startActivity(intent);
        // Oluşturulan Intent kullanılarak AidatGoster aktivitesi başlatılır ve geçiş yapılır.
    }

    @Override
    public void onAidatDelete(String aidatId) {
   deleteAidat(aidatId);
    }

    //  onAidatDelete metodu, bir arayüzün (AidatDeleteListener) bir parçası olarak tanımlanmış bir geri çağırma yöntemidir. Bu yöntem, bir aidatın silinmesi gerektiğinde çağrılır.
    //Metot içinde deleteAidat(aidatId) çağrısı yapılarak deleteAidat metodu çağrılır ve aidatın silinmesi işlemi gerçekleştirilir. aidatId parametresi, silinecek aidatın benzersiz kimliğini temsil eder.
}