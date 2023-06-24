package com.example.goalguru;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HarcamaAdapter extends RecyclerView.Adapter<HarcamaAdapter.HarcamaViewHolder> {
    private List<Harcama> harcamalar;
    private OnHarcamaLongClickListener longClickListener;
    private Context context;

    public HarcamaAdapter(List<Harcama> harcamalar) {
        this.harcamalar = harcamalar;
    }

    public void setOnHarcamaLongClickListener(OnHarcamaLongClickListener listener) {
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public HarcamaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_harcama, parent, false);
        return new HarcamaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HarcamaViewHolder holder, int position) {
        Harcama harcama = harcamalar.get(position);
        holder.bind(harcama, position);
    }

    @Override
    public int getItemCount() {
        return harcamalar.size();
    }

    public void setHarcamalar(List<Harcama> harcamalar) {
        this.harcamalar = harcamalar;
        notifyDataSetChanged();
    }

    public interface OnHarcamaLongClickListener {
        void onHarcamaLongClick(Harcama harcama);
    }

    public class HarcamaViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView tutarTextView;
        private TextView aciklamaTextView;
        private TextView tarihTextView;
        private Harcama currentHarcama;
        private int currentPosition;

        public HarcamaViewHolder(@NonNull View itemView) {
            super(itemView);
            tutarTextView = itemView.findViewById(R.id.harcama_tutar_textview);
            aciklamaTextView = itemView.findViewById(R.id.harcama_aciklama_textview);
            tarihTextView = itemView.findViewById(R.id.harcama_tarih_textview);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Harcama harcama, int position) {
            currentHarcama = harcama;
            currentPosition = position;
            tutarTextView.setText(String.valueOf(harcama.getHarcamaTutar()));
            aciklamaTextView.setText(harcama.getHarcamaAciklama());

            if (harcama.getDate() != null) {
                Date date = harcama.getDate().toDate();
                String formattedDate = formatDate(date);
                tarihTextView.setText(formattedDate);
            } else {
                tarihTextView.setText("Tarih Belirtilmedi");
            }
        }

        private String formatDate(Date date) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            return sdf.format(date);
        }

        @Override
        public boolean onLongClick(View v) {
            showDeleteConfirmationDialog(getAdapterPosition());
            return true;
        }

        private static final String TAG = "HarcamaAdapter";

        private void showDeleteConfirmationDialog(final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Silme Onayı");
            builder.setMessage("Bu harcamayı silmek istediğinize emin misiniz?");
            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteHarcama(position); // Yöntemi çağırarak harcamayı sil
                }
            });
            builder.setNegativeButton("Hayır", null);
            AlertDialog dialog = builder.create();
            dialog.show();
            Log.d(TAG, "Silme onayı dialogu gösterildi. Position: " + position);
        }

        private void deleteHarcama(int position) {
            Harcama harcama = harcamalar.get(position);

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            CollectionReference harcamaRef = firestore.collection("harcamalar");

            harcamaRef.document(harcama.getId()).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            harcamalar.remove(position);
                            notifyItemRemoved(position);
                            Log.d(TAG, "Harcama başarıyla silindi. Position: " + position);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Harcama silinirken hata oluştu", e);
                        }
                    });
        }

    }
}
