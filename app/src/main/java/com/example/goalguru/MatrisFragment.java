package com.example.goalguru;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

public class MatrisFragment extends Fragment implements TaskAdapter.OnItemClickListener {

    private List<Task> taskList;
    private TaskAdapter taskAdapter;
    private FirebaseFirestore db;
    private CollectionReference tasksCollection;

    private static final int URGENT_IMPORTANT_COLOR = R.color.urgent_important_color;
    private static final int URGENT_COLOR = R.color.urgent_color;
    private static final int IMPORTANT_COLOR = R.color.important_color;
    private static final int DEFAULT_COLOR = R.color.default_color;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matris, container, false);

        // Firestore başlatma
        db = FirebaseFirestore.getInstance();
        tasksCollection = db.collection("tasks");

        // RecyclerView ve adapteri ayarla
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewMatrix);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList, this);
        recyclerView.setAdapter(taskAdapter);

        // Firestore'dan verileri al ve listeyi güncelle
        fetchTasksFromFirestore();

        return view;
    }

    private void fetchTasksFromFirestore() {
        tasksCollection.get(Source.CACHE)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    taskList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Task task = document.toObject(Task.class);
                        task.setId(document.getId());
                        taskList.add(task);
                    }
                    taskAdapter.notifyDataSetChanged();

                    if (taskList.isEmpty()) {
                        // Tablo boş olduğunda uyarı mesajını göster
                        Toast.makeText(getActivity(), "Tablo boş", Toast.LENGTH_SHORT);
                    }
                })
                .addOnFailureListener(e -> {
                    // Hata durumunda yapılacak işlemler
                    Toast.makeText(getActivity(), "Görevleri alırken hata oluştu", Toast.LENGTH_SHORT).show();
                });
    }

    public void refreshTasks() {
        fetchTasksFromFirestore();
    }

    private void updateTaskTextInFirestore(String taskId, String newTaskText) {
        tasksCollection.document(taskId)
                .update("taskText", newTaskText)
                .addOnSuccessListener(aVoid -> {
                    // Başarılı güncelleme durumunda yapılacak işlemler
                    Toast.makeText(getActivity(), "Görev metni güncellendi", Toast.LENGTH_SHORT).show();
                    fetchTasksFromFirestore(); // Görevleri yeniden yükle
                })
                .addOnFailureListener(e -> {
                    // Güncelleme hatası durumunda yapılacak işlemler
                    Toast.makeText(getActivity(), "Görev metnini güncellerken hata oluştu", Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteTaskFromFirestore(String taskId) {
        tasksCollection.document(taskId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Başarılı silme durumunda yapılacak işlemler
                    Toast.makeText(getActivity(), "Görev silindi", Toast.LENGTH_SHORT).show();
                    fetchTasksFromFirestore(); // Görevleri yeniden yükle
                })
                .addOnFailureListener(e -> {
                    // Silme hatası durumunda yapılacak işlemler
                    Toast.makeText(getActivity(), "Görevi silerken hata oluştu", Toast.LENGTH_SHORT).show();
                });
    }

    public void updateOrDeleteTask(String taskId, boolean isImportant, String taskText, boolean isUrgent) {
        if (isImportant) {
            // Görevi sil
            deleteTaskFromFirestore(taskId);
        } else {
            // Görevi güncelle
            String newTaskText = taskText + " güncellendi";
            updateTaskTextInFirestore(taskId, newTaskText);
        }
    }

    private boolean isDeleteDialogShown = false;

    @Override
    public void onItemClick(Task task, int position) {
        if (!isDeleteDialogShown) {
            showDeleteConfirmationDialog(task.getId());
        }
    }

    private void showDeleteConfirmationDialog(String taskId) {
        isDeleteDialogShown = true;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Silmek İstediğinize Emin Misiniz?")
                .setMessage("Bu işlem geri alınamaz.")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTaskFromFirestore(taskId);
                    }
                })
                .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isDeleteDialogShown = false;
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isDeleteDialogShown = false;
                    }
                })
                .show();
    }


    @Override
    public void onTitleClick(Task task, int position) {
        // Görev metnine tıklama işlemi burada gerçekleştirilebilir
    }
    public void clearAllTasksAndSwitchToMatrisFragment() {
        tasksCollection.get(Source.CACHE)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot document : documents) {
                        document.getReference().delete();
                    }
                    Toast.makeText(getActivity(), "Tüm görevler silindi", Toast.LENGTH_SHORT).show();
                    switchToMatrisTab();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Görevleri silerken hata oluştu", Toast.LENGTH_SHORT).show();
                });
    }


    private void switchToMatrisTab() {
        ViewPager viewPager = getActivity().findViewById(R.id.viewPager);
        if (viewPager != null) {
            viewPager.setCurrentItem(1);

            MatrisFragment matrisFragment = (MatrisFragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
            if (matrisFragment != null) {
                matrisFragment.refreshTasks();
            }
        }
    }


}
