package com.example.goalguru;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity {
    private EditText editTextFeedback;
    private Button buttonSubmit;
    private List<Feedback> feedbackList;
    private FirebaseFirestore firestore;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feedback);

        editTextFeedback = findViewById(R.id.editTextFeedback);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        ratingBar = findViewById(R.id.ratingBar);

        firestore = FirebaseFirestore.getInstance();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedback = editTextFeedback.getText().toString().trim();
                float rating = ratingBar.getRating();
                saveFeedbackToFirestore(feedback, rating);
            }
        });

        feedbackList = new ArrayList<>();

        loadFeedbacksFromFirestore();
    }

    private void saveFeedbackToFirestore(String feedback, float rating) {
        firestore.collection("feedbacks")
                .add(new HashMap<String, Object>() {{
                    put("feedback", feedback);
                    put("rating", rating);
                }})
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(FeedbackActivity.this, "Geri bildiriminiz gönderildi.", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(FeedbackActivity.this, "Geri bildirim gönderilirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadFeedbacksFromFirestore() {
        firestore.collection("feedbacks")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    feedbackList.clear();
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        String feedback = documentSnapshot.getString("feedback");
                        double rating = documentSnapshot.getDouble("rating");
                        Feedback feedbackObject = new Feedback(feedback, (float) rating);
                        feedbackList.add(feedbackObject);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(FeedbackActivity.this, "Geri bildirimleri yüklerken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                });
    }
}
