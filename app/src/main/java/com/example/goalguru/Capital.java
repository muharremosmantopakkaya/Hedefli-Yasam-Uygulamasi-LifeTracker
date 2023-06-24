package com.example.goalguru;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Capital extends AppCompatActivity implements CapitalListFragment.OnCapitalClickListener {

    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capital);

        fragmentContainer = findViewById(R.id.fragment_container);

        // Başlangıçta CapitalListFragment'ı aç
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, new CapitalListFragment())
                    .commit();
        }
    }

    @Override
    public void onCapitalClick(String capitalName) {
        // Başkente tıklandığında CapitalDetailFragment'ı aç
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, CapitalDetailFragment.newInstance(capitalName))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onItemClick(String capitalName) {
        // Başkentin ismini CapitalDetailFragment'e iletmek için bir intent oluşturabilirsiniz.
        Intent intent = new Intent(this, CapitalDetailFragment.class);
        intent.putExtra("capitalName", capitalName);
        startActivity(intent);
    }

}
