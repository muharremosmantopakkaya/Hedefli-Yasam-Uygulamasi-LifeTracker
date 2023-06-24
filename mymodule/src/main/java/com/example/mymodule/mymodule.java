package com.example.mymodule;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

public class mymodule extends AppCompatActivity {
    private TessBaseAPI tessBaseAPI;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.imageView);
        TextView textView = findViewById(R.id.textView);

        // Görüntüyü yükle
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.example_image, options);
        imageView.setImageBitmap(bitmap);

        // Tesseract OCR API'yi başlat
        tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.init(getExternalFilesDir(null).getAbsolutePath(), "eng");
        tessBaseAPI.setImage(bitmap);

        // Metin çıktısını al
        String text = tessBaseAPI.getUTF8Text();
        textView.setText(text);

        // Tesseract OCR API'yi kapat
        tessBaseAPI.end();
    }
}
