package com.example.goalguru;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class NewVKEFragment extends Fragment {

    private static final String ARG_VERI_1 = "veri1";
    private static final String ARG_VERI_2 = "veri2";

    private String veri1;
    private String veri2;

    public NewVKEFragment() {
        // Boş public yapıcı metod gereklidir
    }

    public static NewVKEFragment newInstance(String veri1, String veri2) {
        NewVKEFragment fragment = new NewVKEFragment();
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
        View view = inflater.inflate(R.layout.fragment_new_v_k_e, container, false);

        Button btnGecis = view.findViewById(R.id.btnGecis);
        btnGecis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BmiActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}