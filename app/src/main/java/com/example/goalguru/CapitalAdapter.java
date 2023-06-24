package com.example.goalguru;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CapitalAdapter extends RecyclerView.Adapter<CapitalAdapter.ViewHolder> {

    private List<String> capitalList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String capitalName);
    }

    public CapitalAdapter(List<String> capitalList, OnItemClickListener listener) {
        this.capitalList = capitalList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_capital, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String capitalName = capitalList.get(position);
        holder.bind(capitalName);
    }

    @Override
    public int getItemCount() {
        return capitalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView capitalNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            capitalNameTextView = itemView.findViewById(R.id.capital_name_text_view);
            itemView.setOnClickListener(this);
        }

        public void bind(String capitalName) {
            capitalNameTextView.setText(capitalName);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                String capitalName = capitalList.get(position);
                listener.onItemClick(capitalName);
            }
        }
    }
}
