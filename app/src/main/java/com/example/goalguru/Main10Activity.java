package com.example.goalguru;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.googlecode.tesseract.android.TessBaseAPI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.googlecode.tesseract.android.TessBaseAPI;

public class Main10Activity extends AppCompatActivity {
    private TessBaseAPI tessBaseAPI;
    private static final int PICK_IMAGE = 100;
    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main10);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        Button selectImageButton = findViewById(R.id.choose_image_button);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
                new OCRTask().execute(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class OCRTask extends AsyncTask<Bitmap, Void, String> {
        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            TessBaseAPI tessBaseAPI = new TessBaseAPI();
            tessBaseAPI.init("C:\\Users\\canbe\\StudioProjects\\GoalGuru5\\app\\assets\\tessdata", "eng");

            tessBaseAPI.setImage(bitmaps[0]);
            return tessBaseAPI.getUTF8Text();
        }



    @Override
        protected void onPostExecute(String s) {
            textView.setText(s);
        }
    }
}
