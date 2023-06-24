package com.example.goalguru;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    private TextView questionTextView;
    private Button option1Button;
    private Button option2Button;
    private Button option3Button;
    private Button noneButton;

    private String capitalName;
    private String correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = findViewById(R.id.question_text_view);
        option1Button = findViewById(R.id.option1_button);
        option2Button = findViewById(R.id.option2_button);
        option3Button = findViewById(R.id.option3_button);
        noneButton = findViewById(R.id.none_button);

        // Intent'ten gelen verileri al
        capitalName = getIntent().getStringExtra("capitalName");
        correctAnswer = getIntent().getStringExtra("correctAnswer");

        // Soruyu ve seçenekleri ekrana göster
        questionTextView.setText("Hangi ülkenin başkenti " + capitalName + "?");

        // Doğru cevap olmadığında "Hiçbiri" seçeneğini göster
        option1Button.setText("Türkiye");
        option2Button.setText("Almanya");
        option3Button.setText("Fransa");
        noneButton.setText("Hiçbiri");
    }

    public void checkAnswer(View view) {
        Button selectedOptionButton = (Button) view;
        String selectedOption = selectedOptionButton.getText().toString();

        if (selectedOption.equals(correctAnswer)) {
            // Cevap doğru ise Toast mesajı göster
            Toast.makeText(this, "Doğru cevap!", Toast.LENGTH_SHORT).show();
        } else {
            // Cevap yanlış ise Toast mesajı göster
            Toast.makeText(this, "Yanlış cevap!", Toast.LENGTH_SHORT).show();
        }

        // Quiz aktivitesini kapat
        finish();
    }
}
