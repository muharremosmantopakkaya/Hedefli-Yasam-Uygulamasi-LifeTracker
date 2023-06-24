package com.example.goalguru;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HarcamaListele extends AppCompatActivity implements HarcamaAdapter.OnHarcamaLongClickListener {

    private RecyclerView recyclerView;
    private HarcamaAdapter harcamaAdapter;
    private List<Harcama> harcamaList;
    private FirebaseFirestore firestore;
    private CollectionReference harcamaRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harcama_listele);

        recyclerView = findViewById(R.id.harcama_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        harcamaList = new ArrayList<>();
        harcamaAdapter = new HarcamaAdapter(harcamaList);
        harcamaAdapter.setOnHarcamaLongClickListener(this);
        recyclerView.setAdapter(harcamaAdapter);

        firestore = FirebaseFirestore.getInstance();
        harcamaRef = firestore.collection("harcamalar");

        fetchHarcamalar();
    }

    private void fetchHarcamalar() {
        harcamaRef.orderBy("date").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                harcamaList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Harcama harcama = document.toObject(Harcama.class);
                    harcama.setId(document.getId()); // Document ID'sini ayarla
                    harcamaList.add(harcama);
                }
                harcamaAdapter.notifyDataSetChanged();
            } else {
                // Hata durumunda işlemler
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_all) {
            showDeleteAllConfirmationDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAllConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tüm Harcamaları Sil");
        builder.setMessage("Tüm harcamaları silmek istediğinize emin misiniz?");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAllHarcamalar();
            }
        });
        builder.setNegativeButton("Hayır", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteAllHarcamalar() {
        harcamaRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    document.getReference().delete();
                }
                harcamaList.clear();
                harcamaAdapter.notifyDataSetChanged();
            } else {
                // Hata durumunda işlemler
            }
        });
    }

    @Override
    public void onHarcamaLongClick(Harcama harcama) {
        showDeleteConfirmationDialog(harcama);
    }

    private void showDeleteConfirmationDialog(final Harcama harcama) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Silme Onayı");
        builder.setMessage("Bu harcamayı silmek istediğinize emin misiniz?");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteHarcama(harcama);
            }
        });
        builder.setNegativeButton("Hayır", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteHarcama(Harcama harcama) {
        harcamaRef.document(harcama.getId()).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                harcamaList.remove(harcama);
                harcamaAdapter.notifyDataSetChanged();
            } else {
                // Hata durumunda işlemler
            }
        });
    }
}
