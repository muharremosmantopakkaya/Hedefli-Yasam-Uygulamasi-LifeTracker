package com.example.goalguru;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.goalguru.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.checkerframework.checker.nullness.qual.NonNull;

public class bottom extends AppCompatActivity implements TaskListener {
  ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();

    AyarlarFragment ayarlarFragment = new AyarlarFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_bottom);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment).commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        if (bottomNavigationView != null) {
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.home:
                            selectedFragment = homeFragment;
                            break;
                        case R.id.profile:
                            selectedFragment = profileFragment;
                            break;
                        case R.id.settings:
                            selectedFragment = settingsFragment;
                            break;

                        case R.id.ayarlar:
                            selectedFragment = ayarlarFragment;
                            break;
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment).commit();
                    }

                    return true;
                }
            });
        }




    }
    @Override
    public void onTaskAdded() {
        // Bir görev eklendiğinde yapılması gereken işlemleri uygulayın
    }

    @Override
    public void onTaskDeleted() {
        // Bir görev silindiğinde yapılması gereken işlemleri uygulayın
    }

    @Override
    public void synchronizeData() {

    }

    @Override
    public void switchToMatrisTab() {

    }

    @Override
    public void clearAllTasks() {

    }

}
