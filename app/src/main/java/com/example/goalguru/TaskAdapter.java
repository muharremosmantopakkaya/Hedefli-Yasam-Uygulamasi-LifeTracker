package com.example.goalguru;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private OnItemClickListener itemClickListener;

    public TaskAdapter(List<Task> tasks, OnItemClickListener itemClickListener) {
        this.tasks = tasks;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task, itemClickListener);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }

        public void bind(Task task, OnItemClickListener listener) {
            titleTextView.setText(task.getTaskText());
            itemView.setBackgroundColor(getBackgroundColor(task));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(task, getAdapterPosition());
                }
            });

            titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTitleClick(task, getAdapterPosition());
                }
            });
        }

        private int getBackgroundColor(Task task) {
            if (task.isUrgent() && task.isImportant()) {
                return ContextCompat.getColor(itemView.getContext(), R.color.urgent_important_color);
            } else if (task.isUrgent()) {
                return ContextCompat.getColor(itemView.getContext(), R.color.urgent_color);
            } else if (task.isImportant()) {
                return ContextCompat.getColor(itemView.getContext(), R.color.important_color);
            } else {
                return ContextCompat.getColor(itemView.getContext(), R.color.default_color);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Task task, int position);
        void onTitleClick(Task task, int position);
    }
}
