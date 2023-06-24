package com.example.goalguru;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class kilotab extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTablayout;
    private CustomViewPagerAdapter mAdapter;

    private void init() {
        mViewPager = findViewById(R.id.main_activity_viewPager);
        mTablayout = findViewById(R.id.main_activity_tableLayout);

        mAdapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new KiloFragment(), "ChatGPT");
        mAdapter.addFragment(new VkeFragment(), "Vke");
        mAdapter.addFragment(new FavouriteFragment(), "Favori");
        mAdapter.addFragment(new EgzersizFragment(), "Egzersiz");
        mAdapter.addFragment(new NewVKEFragment(), "VKE DETAYLI");
        mViewPager.setAdapter(mAdapter);
        mTablayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Tam ekran ayarlarını burada yapın
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide(); // Bu satırı yorum satırı yapın veya kaldırın

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kilotab);
        init();
    }

    private static class CustomViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public CustomViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}
