package com.example.goalguru;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PlakaAdapter extends ArrayAdapter<PlakaModel> {
    private Context context;
    private List<PlakaModel> plakaList;

    public PlakaAdapter(Context context, List<PlakaModel> plakaList) {
        super(context, 0, plakaList);
        this.context = context;
        this.plakaList = plakaList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.activity_plakalar, parent, false);
        }

        PlakaModel plaka = plakaList.get(position);

        TextView textViewCity = listItemView.findViewById(R.id.textViewCity);
        TextView textViewPlate = listItemView.findViewById(R.id.textViewPlate);

        textViewCity.setText(plaka.getCity());
        textViewPlate.setText(plaka.getPlate());

        return listItemView;
    }
}

