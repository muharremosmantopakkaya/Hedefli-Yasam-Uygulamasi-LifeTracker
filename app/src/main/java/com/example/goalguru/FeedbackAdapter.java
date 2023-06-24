package com.example.goalguru;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {
    private List<Feedback> feedbackList;

    public FeedbackAdapter(List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback feedback = feedbackList.get(position);
        holder.bind(feedback);
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public class FeedbackViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewFeedback;
        private TextView textViewRating;
        private CardView cardView;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFeedback = itemView.findViewById(R.id.editTextFeedback);
            textViewRating = itemView.findViewById(R.id.ratingBar);
            cardView = itemView.findViewById(R.id.cardViewFeedback);
        }

        public void bind(Feedback feedback) {
            textViewFeedback.setText(feedback.getFeedback());
            textViewRating.setText(String.valueOf(feedback.getRating()));
        }
    }
}
