package com.example.goalguru;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class HedefFragment extends Fragment {

    private static final String ARG_VERI_1 = "veri1";
    private static final String ARG_VERI_2 = "veri2";

    private String veri1;
    private String veri2;

    public HedefFragment() {
        // Boş public yapıcı metod gereklidir
    }

    public static AidatTakipFragment newInstance(String veri1, String veri2) {
        AidatTakipFragment fragment = new AidatTakipFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VERI_1, veri1);
        args.putString(ARG_VERI_2, veri2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            veri1 = getArguments().getString(ARG_VERI_1);
            veri2 = getArguments().getString(ARG_VERI_2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hedef, container, false);

        Button btnGecis = view.findViewById(R.id.btnGecis);
        btnGecis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Hedeflerim.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
