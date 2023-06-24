package com.example.goalguru;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class FutbolQuizActivity extends AppCompatActivity implements View.OnClickListener {

    TextView totalQuestionsTextView;
    TextView questionTextView;
    Button ansA, ansB, ansC, ansD;
    Button nextBtn;
    Button answerBtn;

    int score = 0;
    int totalQuestion = Soru.question.length;
    int currentQuestionIndex = 0;
    String selectedAnswer = "";

    private TextView tvTimer;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futbol_quiz);

        totalQuestionsTextView = findViewById(R.id.total_question);
        questionTextView = findViewById(R.id.question);
        ansA = findViewById(R.id.ans_A);
        ansB = findViewById(R.id.ans_B);
        ansC = findViewById(R.id.ans_C);
        ansD = findViewById(R.id.ans_D);
        nextBtn = findViewById(R.id.next_btn);
        answerBtn = findViewById(R.id.answer_btn);
        tvTimer = findViewById(R.id.tvTimer);

        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        answerBtn.setOnClickListener(this);

        totalQuestionsTextView.setText("Total questions: " + totalQuestion);

        loadNewQuestion();
    }

    @Override
    public void onClick(View view) {
        resetButtons();

        Button clickedButton = (Button) view;

        if (clickedButton.getId() == R.id.next_btn) {
            currentQuestionIndex++;
            loadNewQuestion();
        } else if (clickedButton.getId() == R.id.answer_btn) {
            showAnswers();
        } else {
            if (selectedAnswer.equals("")) {
                selectedAnswer = clickedButton.getText().toString();

                if (selectedAnswer.equals(Soru.correctAnswers[currentQuestionIndex])) {
                    // Correct answer selected
                    clickedButton.setBackgroundColor(Color.GREEN);
                    score++;
                } else {
                    // Wrong answer selected
                    clickedButton.setBackgroundColor(Color.RED);
                }

                // Disable buttons after selection
                ansA.setEnabled(false);
                ansB.setEnabled(false);
                ansC.setEnabled(false);
                ansD.setEnabled(false);

                // Delay to show the result
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int correctAnswerIndex = Arrays.asList(Soru.choices[currentQuestionIndex]).indexOf(Soru.correctAnswers[currentQuestionIndex]);

                        switch (correctAnswerIndex) {
                            case 0:
                                ansA.setBackgroundColor(Color.GREEN);
                                break;
                            case 1:
                                ansB.setBackgroundColor(Color.GREEN);
                                break;
                            case 2:
                                ansC.setBackgroundColor(Color.GREEN);
                                break;
                            case 3:
                                ansD.setBackgroundColor(Color.GREEN);
                                break;
                        }
                    }
                }, 1000); // Delay duration in milliseconds (adjust as needed)
            }
        }
    }



    void loadNewQuestion() {
        if (currentQuestionIndex == totalQuestion) {
            finishQuiz();
            return;
        }

        questionTextView.setText(Soru.question[currentQuestionIndex]);
        ansA.setText(Soru.choices[currentQuestionIndex][0]);
        ansB.setText(Soru.choices[currentQuestionIndex][1]);
        ansC.setText(Soru.choices[currentQuestionIndex][2]);
        ansD.setText(Soru.choices[currentQuestionIndex][3]);

        resetButtons();
        selectedAnswer = "";
        isAnswered = false; // Yeni soru için cevap verilmedi
        isCorrect = false; // Yeni soru için doğru cevap kontrolü sıfırlandı

        startTimer();
    }

    void finishQuiz() {
        String passStatus;
        if (score > totalQuestion * 0.50) {
            passStatus = "Başarılı";
        } else {
            passStatus = "Başarısız";
        }

        new AlertDialog.Builder(this)
                .setTitle(passStatus)
                .setMessage("Skorunuz " + score + " / " + totalQuestion)
                .setPositiveButton("Tekrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        restartQuiz();
                    }
                })
                .setCancelable(false)
                .show();
    }

    void restartQuiz() {
        score = 0;
        currentQuestionIndex = 0;
        loadNewQuestion();
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        timeLeftInMillis = 10000;
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                currentQuestionIndex++;
                loadNewQuestion();
            }
        }.start();
    }

    private void updateTimer() {
        int seconds = (int) (timeLeftInMillis / 1000);
        String timeLeftFormatted = String.format("%02d", seconds);
        tvTimer.setText(timeLeftFormatted + "s");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void showAnswers() {
        String[] choices = Soru.choices[currentQuestionIndex];
        String correctAnswer = Soru.correctAnswers[currentQuestionIndex];

        String answerText = "Doğru Cevap: " + correctAnswer;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < choices.length; i++) {
            sb.append(i + 1).append(") ").append(choices[i]).append("\n");
        }

        new AlertDialog.Builder(this)
                .setTitle("Cevaplar")
                .setMessage(sb.toString() + answerText)
                .setPositiveButton("Tamam", null)
                .show();
    }
    private boolean isAnswered = false; // Cevap verildi mi?
    private boolean isCorrect = false; // Doğru cevap mı?

    private void resetButtons() {

            ansA.setBackgroundResource(android.R.drawable.btn_default);
            ansB.setBackgroundResource(android.R.drawable.btn_default);
            ansC.setBackgroundResource(android.R.drawable.btn_default);
            ansD.setBackgroundResource(android.R.drawable.btn_default);



        // Enable buttons for the next question
        ansA.setEnabled(true);
        ansB.setEnabled(true);
        ansC.setEnabled(true);
        ansD.setEnabled(true);
    }

    public void geriiii(View view) {
        Intent intent = new Intent(FutbolQuizActivity.this, bottom.class);
        startActivity(intent);
    }
}
