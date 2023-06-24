package com.example.goalguru;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OrucSayaciActivity extends AppCompatActivity {

    private TextView countdownTextView;
    private Button stopButton;
    private Button duraklatButton;
    private CountDownTimer countDownTimer;
    private long remainingTime;
    private boolean isPaused = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oruc_sayaci);

        countdownTextView = findViewById(R.id.countdownTextView);
        stopButton = findViewById(R.id.stopButton);
        duraklatButton = findViewById(R.id.duraklatButton);

        int orucSure = getIntent().getIntExtra("orucSure", 0);
        remainingTime = orucSure * 60 * 60 * 1000;

        countDownTimer = new CountDownTimer(remainingTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isPaused) {
                    remainingTime = millisUntilFinished;

                    long hours = remainingTime / (60 * 60 * 1000);
                    long minutes = (remainingTime % (60 * 60 * 1000)) / (60 * 1000);
                    long seconds = (remainingTime % (60 * 1000)) / 1000;

                    String timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                    countdownTextView.setText(timeLeftFormatted);
                }
            }

            @Override
            public void onFinish() {
                // Sayaç tamamlandığında yapılacak işlemler
            }
        };

        countDownTimer.start();

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                Intent intent = new Intent(OrucSayaciActivity.this, OrucPlanActivity.class);
                startActivity(intent);
                finish();
            }
        });

        duraklatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = !isPaused;

                if (isPaused) {
                    countDownTimer.cancel();
                    duraklatButton.setText("Devam Et");
                } else {
                    countDownTimer = new CountDownTimer(remainingTime, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            remainingTime = millisUntilFinished;

                            long hours = remainingTime / (60 * 60 * 1000);
                            long minutes = (remainingTime % (60 * 60 * 1000)) / (60 * 1000);
                            long seconds = (remainingTime % (60 * 1000)) / 1000;

                            String timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                            countdownTextView.setText(timeLeftFormatted);
                        }

                        @Override
                        public void onFinish() {
                            // Sayaç tamamlandığında yapılacak işlemler
                        }
                    };
                    countDownTimer.start();
                    duraklatButton.setText("Sayacı Duraklat");
                }
            }
        });
    }
}