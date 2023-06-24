package com.example.goalguru;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private CardView clothingCard;
    private CardView aidatCard;
    private CardView kiloCard;
    private CardView fastCard;
    private CardView hobiCard;
    private CardView sorumCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Kart görünümlerini bulma
        clothingCard = view.findViewById(R.id.clothingCard);
        aidatCard = view.findViewById(R.id.aidatCard);
        kiloCard = view.findViewById(R.id.kiloCard);
        fastCard = view.findViewById(R.id.fastCard);
        hobiCard = view.findViewById(R.id.hobiCard);
        sorumCard = view.findViewById(R.id.sorumCard);

        // Kartlara tıklama olaylarını ayarlama
        clothingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), tablayout.class);
                startActivity(intent);
            }
        });

        aidatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), aidattab.class);
                startActivity(intent);
            }
        });

        kiloCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), kilotab.class);
                startActivity(intent);
            }
        }); 

        fastCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), oructab.class);
                startActivity(intent);
            }
        });

        hobiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Hobiler.class);
                startActivity(intent);
            }
        });

        sorumCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), hedeftab.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
