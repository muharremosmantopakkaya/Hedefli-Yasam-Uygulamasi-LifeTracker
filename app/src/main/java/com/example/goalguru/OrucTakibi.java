package com.example.goalguru;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalguru.OrucPlan;
import com.example.goalguru.OrucPlanAdapter;
import com.example.goalguru.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrucTakibi extends AppCompatActivity {

    private ScrollView scrollView;
    private int calculateOrucSuresi(String hedef, String aktivite, String egzersiz, String uyku, String cinsiyet, int yas, int boy, int mevcutKilo, int hedefKilo) {
        int orucSuresi = 0;

        // Hedef, aktivite, egzersiz, uyku, cinsiyet vb. parametrelere göre oruç süresini hesaplayın
        if (aktivite.equals("Hareketsiz")) {
            orucSuresi = 12; // Hareketsizlik durumu için 12 saatlik oruç süresi
        } else if (aktivite.equals("Hafiften Aktif")) {
            orucSuresi = 14; // Hafiften aktif durumu için 14 saatlik oruç süresi
        } else if (aktivite.equals("Kısmen Aktif")) {
            orucSuresi = 16; // Kısmen aktif durumu için 16 saatlik oruç süresi
        } else if (aktivite.equals("Çok Aktif")) {
            orucSuresi = 18; // Çok aktif durumu için 18 saatlik oruç süresi
        }

        // Uyku süresi ve mevcut kiloya göre oruç süresine eklemeler yapın
        String[] uykuSuresiParts = uyku.split(" "); // Uyku süresini boşluk karakterine göre ayrıştırın
        int uykuSuresi = Integer.parseInt(uykuSuresiParts[0]); // Uyku süresinin ilk parçasını integer'a dönüştürün

        // Uyku süresi 8 saatten az ise oruç süresine 1 saat ekleyin
        if (uykuSuresi > 6) {
            orucSuresi += 1;
        }

        // Mevcut kiloya göre oruç süresine eklemeler yapın
        int kiloFarki = mevcutKilo - hedefKilo;

        if (kiloFarki >= 10) {
            orucSuresi += 2;
        } else if (kiloFarki >= 5) {
            orucSuresi += 1;
        }
// Mevcut kilo, hedef kilodan 10 kilo veya daha fazla ise oruç süresine 2 saat ekleyin
        return orucSuresi;
    }

    private FirebaseFirestore firestore;
    private RecyclerView recyclerView;
    private OrucPlanAdapter adapter;
    private List<OrucPlan> orucPlanList;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        scrollView = findViewById(R.id.scrollView);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oruc_takibi);

        firestore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orucPlanList = new ArrayList<>();
        adapter = new OrucPlanAdapter(orucPlanList, this);
        recyclerView.setAdapter(adapter);

        Button buttonOrucPlanOlustur = findViewById(R.id.buttonOrucPlanOlustur);
        buttonOrucPlanOlustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrucPlan();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Çıkmak için tekrar geri tuşuna basın", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000); // Geri tuşuna çift tıklama zaman aralığı (ms)
    }

    private void createOrucPlan() {
        scrollView = findViewById(R.id.scrollView);
        // Sayfanın en altına otomatik olarak kaydırma işlemi

        // Kullanıcının seçtiği değerleri al
        RadioGroup radioGroupHedef = findViewById(R.id.radioGroupHedef);
        int selectedHedefId = radioGroupHedef.getCheckedRadioButtonId();
        RadioButton selectedHedefRadioButton = findViewById(selectedHedefId);
        String selectedHedef = "";
        if (selectedHedefRadioButton != null) {
            selectedHedef = selectedHedefRadioButton.getText().toString();
        }

        RadioGroup radioGroupAktivite = findViewById(R.id.radioGroupAktivite);
        int selectedAktiviteId = radioGroupAktivite.getCheckedRadioButtonId();
        RadioButton selectedAktiviteRadioButton = findViewById(selectedAktiviteId);
        String selectedAktivite = "";
        if (selectedAktiviteRadioButton != null) {
            selectedAktivite = selectedAktiviteRadioButton.getText().toString();
        }

        RadioGroup radioGroupEgzersiz = findViewById(R.id.radioGroupEgzersiz);
        int selectedEgzersizId = radioGroupEgzersiz.getCheckedRadioButtonId();
        RadioButton selectedEgzersizRadioButton = findViewById(selectedEgzersizId);
        String selectedEgzersiz = "";
        if (selectedEgzersizRadioButton != null) {
            selectedEgzersiz = selectedEgzersizRadioButton.getText().toString();
        }

        RadioGroup radioGroupUyku = findViewById(R.id.radioGroupUyku);
        int selectedUykuId = radioGroupUyku.getCheckedRadioButtonId();
        RadioButton selectedUykuRadioButton = findViewById(selectedUykuId);
        String selectedUyku = "";
        if (selectedUykuRadioButton != null) {
            selectedUyku = selectedUykuRadioButton.getText().toString();
        }

        RadioGroup radioGroupCinsiyet = findViewById(R.id.radioGroupCinsiyet);
        int selectedCinsiyetId = radioGroupCinsiyet.getCheckedRadioButtonId();
        RadioButton selectedCinsiyetRadioButton = findViewById(selectedCinsiyetId);
        String selectedCinsiyet = "";
        if (selectedCinsiyetRadioButton != null) {
            selectedCinsiyet = selectedCinsiyetRadioButton.getText().toString();
        }

        EditText editTextYas = findViewById(R.id.editTextYas);
        int yas = 0;
        if (!TextUtils.isEmpty(editTextYas.getText().toString())) {
            yas = Integer.parseInt(editTextYas.getText().toString());
        }

        EditText editTextBoy = findViewById(R.id.editTextBoy);
        int boy = 0;
        if (!TextUtils.isEmpty(editTextBoy.getText().toString())) {
            boy = Integer.parseInt(editTextBoy.getText().toString());
        }

        EditText editTextMevcutKilo = findViewById(R.id.editTextMevcutKilo);
        int mevcutKilo = 0;
        if (!TextUtils.isEmpty(editTextMevcutKilo.getText().toString())) {
            mevcutKilo = Integer.parseInt(editTextMevcutKilo.getText().toString());
        }

        EditText editTextHedefKilo = findViewById(R.id.editTextHedefKilo);

        String hedefKiloText = editTextHedefKilo.getText().toString().trim();
        if (TextUtils.isEmpty(hedefKiloText)) {
            Toast.makeText(OrucTakibi.this, "Lütfen bilgileri tam olarak doldurunuz.", Toast.LENGTH_SHORT).show();
            return; // Metodu burada sonlandırarak ilerlemeyi durduruyoruz
        }

        int hedefKilo = Integer.parseInt(hedefKiloText);
        hedefKilo = 0;
        if (!TextUtils.isEmpty(editTextHedefKilo.getText().toString())) {
            hedefKilo = Integer.parseInt(editTextHedefKilo.getText().toString());
        }

        // Hesaplamayı yapmak için calculateOrucSuresi() metodunu çağırın
        int orucSuresi = calculateOrucSuresi(selectedHedef, selectedAktivite, selectedEgzersiz, selectedUyku, selectedCinsiyet, yas, boy, mevcutKilo, hedefKilo);
        // Hesaplanan oruc süresini kullanabilirsiniz
        Toast.makeText(OrucTakibi.this, "Oruç Süresi: " + orucSuresi + " saat", Toast.LENGTH_SHORT).show();
        // Oluşturulan planı Firestore veritabanına kaydet
        Map<String, Object> plan = new HashMap<>();
        plan.put("hedef", selectedHedef);
        plan.put("aktivite", selectedAktivite);
        plan.put("egzersiz", selectedEgzersiz);
        plan.put("uyku", selectedUyku);
        plan.put("cinsiyet", selectedCinsiyet);
        plan.put("yas", yas);
        plan.put("boy", boy);
        plan.put("mevcutKilo", mevcutKilo);
        plan.put("hedefKilo", hedefKilo);
        plan.put("orucSuresi", orucSuresi); // Hesaplanan oruc süresini plana ekleyin

        firestore.collection("orucPlanlari")
                .add(plan)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // İstenilen işlemleri burada gerçekleştirin
                        // Örneğin, bir Toast mesajı göstermek:
                        String planId = documentReference.getId();
                        Toast.makeText(OrucTakibi.this, "Oruç planı oluşturuldu. " + planId, Toast.LENGTH_SHORT);

                        // İşlemler tamamlandıktan sonra yapılacakları buraya ekleyebilirsiniz
                        // Örneğin, yeni bir aktiviteye geçiş yapmak:
                        Intent intent = new Intent(OrucTakibi.this, SonucActivity.class);
                        intent.putExtra("planId", planId);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Plan oluşturulurken bir hata oluştuğunda yapılacak işlemler burada gerçekleştirilir
                        Toast.makeText(OrucTakibi.this, "Oruç planı oluşturulurken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                    }
                });


    }


            public void geri(View view) {
        Intent intent = new Intent(OrucTakibi.this, MainActivity5.class);
        startActivity(intent);
    }


}
