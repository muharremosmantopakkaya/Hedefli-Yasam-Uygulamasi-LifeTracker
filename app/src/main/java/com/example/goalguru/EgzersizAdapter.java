package com.example.goalguru;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EgzersizAdapter extends RecyclerView.Adapter<EgzersizAdapter.EgzersizViewHolder> {
    private List<Egzersiz> egzersizListesi;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Egzersiz egzersiz);
    }

    public EgzersizAdapter(List<Egzersiz> egzersizListesi, OnItemClickListener listener) {
        this.egzersizListesi = egzersizListesi;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EgzersizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_egzersiz, parent, false);
        return new EgzersizViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EgzersizViewHolder holder, int position) {
        Egzersiz egzersiz = egzersizListesi.get(position);
        holder.bind(egzersiz, listener);
    }

    @Override
    public int getItemCount() {
        return egzersizListesi.size();
    }

    public static class EgzersizViewHolder extends RecyclerView.ViewHolder {
        private ImageView egzersizImage;
        private TextView egzersizName;

        public EgzersizViewHolder(@NonNull View itemView) {
            super(itemView);
            egzersizImage = itemView.findViewById(R.id.egzersizImage);
            egzersizName = itemView.findViewById(R.id.egzersizName);
        }

        public void bind(final Egzersiz egzersiz, final OnItemClickListener listener) {
            egzersizImage.setImageResource(egzersiz.getResimId());
            egzersizName.setText(egzersiz.getAd());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(egzersiz);
                }
            });
        }
    }
}
