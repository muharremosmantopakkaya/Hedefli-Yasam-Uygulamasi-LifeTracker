package com.example.goalguru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GelirDetailsFragment extends Fragment {

    public GelirDetailsFragment() {
        // Boş kurucu metot
    }

    public static GelirDetailsFragment newInstance(String gelir) {
        return new GelirDetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gelir_details, container, false);

        // İstenilen görüntüleme işlemlerini burada gerçekleştirin

        return view;
    }
}
