package com.example.goalguru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FeedbackFragment extends Fragment {
    private EditText editTextFeedback;
    private Button buttonSubmit;
    private Button backButton;
    private List<Feedback> feedbackList;
    private FirebaseFirestore firestore;
    private RatingBar ratingBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
       // backButton = view.findViewById(R.id.button7);
        editTextFeedback = view.findViewById(R.id.editTextFeedback);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        ratingBar = view.findViewById(R.id.ratingBar);

        firestore = FirebaseFirestore.getInstance();
       // backButton.setOnClickListener(v -> geri());

        buttonSubmit.setOnClickListener(v -> {
            String feedback = editTextFeedback.getText().toString().trim();
            float rating = ratingBar.getRating();
            saveFeedbackToFirestore(feedback, rating);
        });

        feedbackList = new ArrayList<>();

        loadFeedbacksFromFirestore();

        return view;
    }

    private void saveFeedbackToFirestore(String feedback, float rating) {
        firestore.collection("feedbacks")
                .add(new HashMap<String, Object>() {{
                    put("feedback", feedback);
                    put("rating", rating);
                }})
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity(), "Geri bildiriminiz gönderildi.", Toast.LENGTH_SHORT).show();
                  //  getActivity().finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Geri bildirim gönderilirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "Geri bildirimleri yüklerken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                });
    }

  //  public void geri() {
   //     Intent intent = new Intent(getActivity(), moderndash.class);
   //     startActivity(intent);
    // }
}
