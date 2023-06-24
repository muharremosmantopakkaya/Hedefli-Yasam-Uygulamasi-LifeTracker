package com.example.goalguru;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private EditText editEmail, editSifre;
    private String txtEmail, txtSifre;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        editEmail = findViewById(R.id.login_email);
        editSifre = findViewById(R.id.login_password);
        mAuth = FirebaseAuth.getInstance();

        // GoogleSignInOptions'ı yapılandırın
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // GoogleSignInClient örneğini oluşturun
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        editSifre.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_UP) {
                    int drawableRightWidth = editSifre.getCompoundDrawables()[2].getBounds().width();
                    if (event.getRawX() >= (editSifre.getRight() - drawableRightWidth - editSifre.getPaddingRight())) {
                        if (editSifre.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            editSifre.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            editSifre.setSelection(editSifre.length());
                            editSifre.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.baseline_visibility_off_24), null);
                        } else {
                            editSifre.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            editSifre.setSelection(editSifre.length());
                            editSifre.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.baseline_visibility_24), null);
                        }
                        return true;
                    } else if (event.getRawX() <= (editSifre.getLeft() + editSifre.getPaddingLeft())) {
                        // Sol taraftaki drawable'a tıklandığında yapılacak işlemleri buraya ekleyebilirsiniz.
                        // Şu an için boş bıraktım, isteğinize göre düzenleyebilirsiniz.
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Firebase ile Google hesabı ile giriş başarılı
                            Toast.makeText(MainActivity.this, "Google ile giriş başarılı", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, MainActivity5.class);
                            startActivity(intent);
                        } else {
                            // Firebase ile Google hesabı ile giriş başarısız
                            Toast.makeText(MainActivity.this, "Google ile giriş başarısız", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void kayitol(View view) {
        txtEmail = editEmail.getText().toString();
        txtSifre = editSifre.getText().toString();

        if (!TextUtils.isEmpty(txtEmail) && !TextUtils.isEmpty(txtSifre)) {
            mAuth.createUserWithEmailAndPassword(txtEmail, txtSifre)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Kayıt İşlemi Başarılı", Toast.LENGTH_SHORT).show();
                                //Intent intent = new Intent(MainActivity.this, MainActivity5.class);
                                //startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            editEmail.setError("E-posta zorunlu alandır.");
            editSifre.setError("Şifre girmek zorunlu alandır");
        }
    }

    public void giris(View view) {
        txtEmail = editEmail.getText().toString();
        txtSifre = editSifre.getText().toString();

        if (!TextUtils.isEmpty(txtEmail) && !TextUtils.isEmpty(txtSifre)) {
            if (txtSifre.length() < 6) {
                editSifre.setError("Şifrenin en az 6 karakterden oluşması gerekmektedir");
                editSifre.requestFocus();
            } else {
                mAuth.signInWithEmailAndPassword(txtEmail, txtSifre)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, bottom.class);
                                    startActivity(intent);
                                } else {
                                    // yanlış mail girişi veya hatalı şifre
                                    Toast.makeText(MainActivity.this, "Giriş Başarısız", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        } else {
            if (txtEmail.isEmpty()) {
                editEmail.setError("E-posta Boş Olamaz");
                editEmail.requestFocus();
            }
            if (txtSifre.isEmpty()) {
                editSifre.setError("Şifre Boş Olamaz");
                editSifre.requestFocus();
            }
        }
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google hesabı alındı
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Firebase kimlik doğrulama yöntemiyle Google ile giriş yap
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google girişi başarısız oldu
                Toast.makeText(this, "Google girişi başarısız oldu", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void ileri(View view) {
        Intent intent = new Intent(MainActivity.this, MainActivity5.class);
        startActivity(intent);
    }

    public void kayitoll(View view) {
        Intent intent = new Intent(MainActivity.this, giris.class);
        startActivity(intent);
    }

    public void google(View view) {
        Intent intent = new Intent(MainActivity.this, bottom.class);
        startActivity(intent);
    }

    public void forgot(View view) {
        Intent intent = new Intent(MainActivity.this, dialog_forgot.class);
        startActivity(intent);
    }
}
