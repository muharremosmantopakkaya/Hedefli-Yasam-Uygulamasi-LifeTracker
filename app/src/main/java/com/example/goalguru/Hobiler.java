package com.example.goalguru;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Hobiler extends AppCompatActivity {

    private Button buttonCapital;
    private Button buttonPlakalar;

    private Button buttonFutbol;

    private Button buttonTarih;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_hobiler);

        buttonCapital = findViewById(R.id.buttonCapital);
        buttonPlakalar = findViewById(R.id.buttonPlakalar);
        buttonFutbol = findViewById(R.id.buttonFutbolQuiz);
     //   buttonTarih=findViewById(R.id.buttonTarih);
        buttonCapital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Capital aktivitesine geçiş yap
                Intent intent = new Intent(Hobiler.this, Capital.class);
                startActivity(intent);
            }
        });

        buttonPlakalar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // PlakalarActivity'yi başlat
                Intent intent = new Intent(Hobiler.this, Plakalar.class);
                startActivity(intent);
            }
        });
        buttonFutbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Capital aktivitesine geçiş yap
                Intent intent = new Intent(Hobiler.this, FutbolQuizActivity.class);
                startActivity(intent);
            }
        });
    }

    public void geriii(View view) {
        Intent intent = new Intent(Hobiler.this, bottom.class);
        startActivity(intent);
    }

    public void plakalarr(View view) {
        Intent intent = new Intent(Hobiler.this, Plakalar.class);
        startActivity(intent);
    }

    public void futbols(View view) {
        Intent intent = new Intent(Hobiler.this, FutbolQuizActivity.class);
        startActivity(intent);
    }

    public void quiz(View view) {
        Intent intent = new Intent(Hobiler.this, HistoryQuiz.class);
        startActivity(intent);

    }

    public void geriiis(View view) {
        Intent intent = new Intent(Hobiler.this, bottom.class);
        startActivity(intent);
    }

    public void multimedia_start(View view) {
        Intent intent = new Intent(Hobiler.this, Mainst.class);
        startActivity(intent);
    }
}
