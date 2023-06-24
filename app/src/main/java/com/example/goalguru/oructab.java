package com.example.goalguru;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class oructab extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTablayout;
    private CustomViewPager mAdapter;

    private void init() {
        mViewPager = findViewById(R.id.main_activity_viewPager);
        mTablayout = findViewById(R.id.main_activity_tableLayout);

        mAdapter = new CustomViewPager(getSupportFragmentManager());
        mAdapter.addFragment(new OrucFragment(), "Oruc");
        mAdapter.addFragment(new OrucPlanFragment(), "Oruc Planı");
        mAdapter.addFragment(new SuFragment(), "Günlük Su Hedefi");
   //     mAdapter.addFragment(new HarcamalarFragment(), "Ağırlık");
        mViewPager.setAdapter(mAdapter);
        mTablayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Tam ekran ayarlarını burada yapın
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout);
        init();
    }
}
