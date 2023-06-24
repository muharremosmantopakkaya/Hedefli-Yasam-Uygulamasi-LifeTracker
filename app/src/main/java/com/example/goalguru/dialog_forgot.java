package com.example.goalguru;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class dialog_forgot extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.dialog__forgot);

        firebaseAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailBox);
        resetButton = findViewById(R.id.btnReset);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(dialog_forgot.this, "Lütfen e-posta adresinizi girin", Toast.LENGTH_SHORT).show();
                } else {
                    resetPassword(email);
                }
            }
        });
    }

    private void resetPassword(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Şifre sıfırlama e-postası gönderildi
                        Toast.makeText(dialog_forgot.this, "Şifre sıfırlama e-postası gönderildi", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // Şifre sıfırlama e-postası gönderilemedi
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException invalidEmail) {
                            Toast.makeText(dialog_forgot.this, "Geçersiz e-posta adresi", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(dialog_forgot.this, "Şifre sıfırlama e-postası gönderilemedi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void cancel(View view) {
        Intent intent = new Intent(dialog_forgot.this, MainActivity.class);
        startActivity(intent);
    }
}
