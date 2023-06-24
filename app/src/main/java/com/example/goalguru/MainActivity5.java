package com.example.goalguru;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
    }
    public void paraYonetimiEkraninaGit(View view)

    {
        Intent intent = new Intent(MainActivity5.this, MainActivity2.class);
        startActivity(intent);
    }
   // private void startMainActivity5() {
     //   Intent intent = new Intent(MainActivity.this, MainActivity5.class);
      //  startActivity(intent);
     //  finish(); // MainActivity'yi kapatmak için kullanabilirsiniz (isteğe bağlı)
   // }

    public void kiloEkraninaGit(View view)

    {
        Intent intent = new Intent(MainActivity5.this, MainActivity3.class);
        startActivity(intent);
    }
    public void vucutEkraninaGit(View view)

    {
        Intent intent = new Intent(MainActivity5.this, MainActivity6.class);
        startActivity(intent);
    }
    public void aidatEkranınagit(View view) {
        Intent intent = new Intent(MainActivity5.this, AidatTakipActivity.class);
        startActivity(intent);
    }

    public void sdkEkranınagit(View view) {
        Intent intent = new Intent(MainActivity5.this, Hedeflerim.class); //hedeflerim
        startActivity(intent);
    }
    public void sdksEkranınagit(View view) {
        Intent intent = new Intent(MainActivity5.this, Hobiler.class); //hedeflerim
        startActivity(intent);
    }




    public void oruc(View view) {
        Intent intent = new Intent(MainActivity5.this, OrucTakibi.class); //hedeflerim
        startActivity(intent);
    }
}