package com.example.goalguru;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class tablayouteisen extends AppCompatActivity {

    private CustomViewPager mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Tam ekran ayarlarını burada yapın
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablayouteisen);
        init();
    }

    private void init() {
        ViewPager mViewPager = findViewById(R.id.main_activity_viewPager);
        TabLayout mTablayout = findViewById(R.id.main_activity_tableLayout);

        mAdapter = new CustomViewPager(getSupportFragmentManager());
        mAdapter.addFragment(new EisenhowerFragment(), "Eisenhower");
        mAdapter.addFragment(new MatrisFragment(), "Matris");

        mViewPager.setAdapter(mAdapter);
        mTablayout.setupWithViewPager(mViewPager);
    }
}
