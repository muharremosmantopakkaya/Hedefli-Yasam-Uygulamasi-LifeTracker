package com.example.goalguru;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

public class SonucActivity extends AppCompatActivity {

    private TextView textViewSonuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sonuc);

        textViewSonuc = findViewById(R.id.textViewSonuc);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String planId = extras.getString("planId");
            getOrucSuresiFromPlanId(planId);
        }
    }

    private void getOrucSuresiFromPlanId(String planId) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("orucPlanlari")
                .document(planId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            int orucSuresi = documentSnapshot.getLong("orucSuresi").intValue();

                            textViewSonuc.setText("Planlanan Oruç Süresi: " + orucSuresi + " saat" +
                                    "  ");
                        } else {
                            textViewSonuc.setText("Plan bulunamadı");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        textViewSonuc.setText("Hata: " + e.getMessage());
                    }
                });
    }

    public void geri(View view) {
        Intent intent = new Intent(SonucActivity.this, OrucTakibi.class);
        startActivity(intent);
    }
}
