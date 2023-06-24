package com.example.goalguru;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.firebase.firestore.FirebaseFirestore;
public class Hedeflerim extends AppCompatActivity {
    private FirebaseFirestore mFirestore;
    private CheckBox checkboxSigara;
    private EditText edittextSigaraNotes;
    private CheckBox checkboxDis;
    private EditText edittextDisNotes;
    private CheckBox checkboxKopek;
    private EditText edittextKopekNotes;
    private Button buttonSave;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Map<String, Object> goalsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hedeflerim);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        checkboxSigara = findViewById(R.id.checkbox_sigara);
        edittextSigaraNotes = findViewById(R.id.edittext_sigara_notes);
        checkboxDis = findViewById(R.id.checkbox_dis);
        edittextDisNotes = findViewById(R.id.edittext_dis_notes);
        checkboxKopek = findViewById(R.id.checkbox_kopek);
        edittextKopekNotes = findViewById(R.id.edittext_kopek_notes);
        buttonSave = findViewById(R.id.button_save);
        mFirestore = FirebaseFirestore.getInstance();
        checkboxDis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edittextDisNotes.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        checkboxKopek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edittextKopekNotes.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sigaraNotes = edittextSigaraNotes.getText().toString().trim();
                String disNotes = edittextDisNotes.getText().toString().trim();
                String kopekNotes = edittextKopekNotes.getText().toString().trim();

                Map<String, Object> goalsMap = new HashMap<>();
                goalsMap.put("sigara", checkboxSigara.isChecked());
                goalsMap.put("sigara_notes", sigaraNotes);
                goalsMap.put("dis", checkboxDis.isChecked());
                goalsMap.put("dis_notes", disNotes);
                goalsMap.put("kopek", checkboxKopek.isChecked());
                goalsMap.put("kopek_notes", kopekNotes);

                mFirestore.collection("hedefler").add(goalsMap);
            }
        });


    }

    public void grafik(View view) {
        Intent intent = new Intent(Hedeflerim.this, Bar.class);
        startActivity(intent);
    }
}