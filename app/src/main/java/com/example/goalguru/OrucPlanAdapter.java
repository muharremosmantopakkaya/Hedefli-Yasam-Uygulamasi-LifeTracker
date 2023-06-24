package com.example.goalguru;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrucPlanAdapter extends RecyclerView.Adapter<OrucPlanAdapter.OrucPlanViewHolder> {
    private List<OrucPlan> orucPlanList;
    private Context context;

    public OrucPlanAdapter(List<OrucPlan> orucPlanList, Context context) {
        this.orucPlanList = orucPlanList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrucPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.oruc_plan_item, parent, false);
        return new OrucPlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrucPlanViewHolder holder, int position) {
        OrucPlan orucPlan = orucPlanList.get(position);
        holder.textViewOrucPlanAdi.setText(orucPlan.getOrucPlanAdi());
        holder.textViewOrucPlanDetay.setText(orucPlan.getOrucPlanDetay());
    }

    @Override
    public int getItemCount() {
        return orucPlanList.size();
    }

    public static class OrucPlanViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOrucPlanAdi;
        TextView textViewOrucPlanDetay;

        public OrucPlanViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrucPlanAdi = itemView.findViewById(R.id.textViewOrucPlanAdi);
            textViewOrucPlanDetay = itemView.findViewById(R.id.textViewOrucPlanDetay);
        }
    }
}
