package com.example.goalguru;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class moderndash extends AppCompatActivity {

    CardView clothingCard;
    CardView aidatCard;
    CardView kiloCard;
    CardView fastCard;
    CardView hobiCard;
    CardView sorumCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderndash);

        clothingCard = findViewById(R.id.clothingCard);
        aidatCard = findViewById(R.id.aidatCard);
        kiloCard = findViewById(R.id.kiloCard);
        fastCard = findViewById(R.id.fastCard);
        hobiCard = findViewById(R.id.hobiCard);
        sorumCard = findViewById(R.id.sorumCard);

        clothingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(moderndash.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        aidatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(moderndash.this, AidatTakipActivity.class);
                startActivity(intent);
            }
        });
        kiloCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(moderndash.this, MainActivity3.class);
                startActivity(intent);
            }
        });
        fastCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(moderndash.this, OrucTakibi.class);
                startActivity(intent);
            }
        });
        hobiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(moderndash.this, Hobiler.class);
                startActivity(intent);
            }
        });
        sorumCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(moderndash.this, Hedeflerim.class);
                startActivity(intent);
            }
        });
    }
}
