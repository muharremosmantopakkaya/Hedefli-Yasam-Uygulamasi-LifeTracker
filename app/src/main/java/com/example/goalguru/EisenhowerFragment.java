package com.example.goalguru;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

public class EisenhowerFragment extends Fragment implements TaskAdapter.OnItemClickListener {
    private List<Task> taskList; // Görevleri tutmak için liste
    private TaskAdapter taskAdapter; // RecyclerView için adapter
    private ViewPager viewPager;

    private FirebaseFirestore db;
    private CollectionReference tasksCollection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eisenhower, container, false);
        viewPager = getActivity().findViewById(R.id.viewPager);

        // Firestore başlatma
        db = FirebaseFirestore.getInstance();
        tasksCollection = db.collection("tasks");

        // RecyclerView ve adapteri ayarlama
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList, EisenhowerFragment.this);

        recyclerView.setAdapter(taskAdapter);

        // Firestore'dan verileri al ve listeyi güncelle
        fetchTasksFromFirestore();

        Button buttonAddTask = view.findViewById(R.id.buttonAddTask);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskToFirestore();
            }
        });

        Button buttonClearMatrix = view.findViewById(R.id.buttonClearMatrix);
        buttonClearMatrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllTasks();
            }
        });

        return view;
    }

    @Override
    public void onItemClick(Task task, int position) {
        deleteTaskFromFirestore(task);
    }

    @Override
    public void onTitleClick(Task task, int position) {

    }

    private void fetchTasksFromFirestore() {
        tasksCollection.get(Source.CACHE)  //Bu anlık alıyor firestoredan ama fazla ağ
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
                    } else {
                        switchToMatrisTab(); // Tablo güncellendiğinde MatrisFragment'a geçiş yap
                    }
                })
                .addOnFailureListener(e -> {
                    // Hata durumunda yapılacak işlemler
                    Toast.makeText(getActivity(), "Görevleri alırken hata oluştu", Toast.LENGTH_SHORT).show();
                });
    }

    private void addTaskToFirestore() {
        EditText editTextTask = getView().findViewById(R.id.editTextTask);
        RadioGroup radioGroupUrgency = getView().findViewById(R.id.radioGroupUrgency);
        RadioGroup radioGroupImportance = getView().findViewById(R.id.radioGroupImportance);

        String taskText = editTextTask.getText().toString();

        if (TextUtils.isEmpty(taskText)) {
            Toast.makeText(getActivity(), "Lütfen görevi girin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (radioGroupUrgency.getCheckedRadioButtonId() == -1 || radioGroupImportance.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getActivity(), "Lütfen aciliyet ve önem durumunu seçin", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUrgent = radioGroupUrgency.getCheckedRadioButtonId() == R.id.radioButtonUrgent;
        boolean isImportant = radioGroupImportance.getCheckedRadioButtonId() == R.id.radioButtonImportant;

        Task newTask = new Task(taskText, isUrgent, isImportant);

        tasksCollection.add(newTask)
                .addOnSuccessListener(documentReference -> {
                    // Başarılı ekleme durumunda yapılacak işlemler
                    Toast.makeText(getActivity(), "Görev eklendi", Toast.LENGTH_SHORT).show();

                    // Matris tab layoutuna geçiş yapın ve Firestore'dan verileri alıp listeyi güncelleyin
                    switchToMatrisTab();
                })
                .addOnFailureListener(e -> {
                    // Ekleme hatası durumunda yapılacak işlemler
                    Toast.makeText(getActivity(), "Görev eklenirken hata oluştu", Toast.LENGTH_SHORT).show();
                });

        editTextTask.setText(""); // Görev alanını sıfırla
        radioGroupUrgency.clearCheck(); // Aciliyet durumu seçimini sıfırla
        radioGroupImportance.clearCheck(); // Önem durumu seçimini sıfırla
    }


    private void clearAllTasks() {
      //  if (taskList.isEmpty()) {
      //      Toast.makeText(getActivity(), "Tablo zaten boş", Toast.LENGTH_SHORT).show();
      //      return;
      //  }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Tüm Görevleri Sil");
        builder.setMessage("Tüm görevleri silmek istediğinize emin misiniz?");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAllTasksAndSwitchToMatrisFragment();
            }
        });
        builder.setNegativeButton("Hayır", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }




    private void deleteAllTasksAndSwitchToMatrisFragment() {
        tasksCollection.get(Source.CACHE)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                    if (documents.isEmpty()) {
                        Toast.makeText(getActivity(), "Tablo zaten boş", Toast.LENGTH_SHORT).show();
                    } else {
                        for (DocumentSnapshot document : documents) {
                            document.getReference().delete();
                        }
                        Toast.makeText(getActivity(), "Tüm görevler silindi", Toast.LENGTH_SHORT).show();
                    }
                    switchToMatrisFragment();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Görevleri silerken hata oluştu", Toast.LENGTH_SHORT).show();
                });
    }


    private void switchToMatrisFragment() {
        // MatrisFragment'a geçiş yapma işlemleri burada gerçekleştirilir
        ViewPager viewPager = getActivity().findViewById(R.id.viewPager);
        if (viewPager != null) {
            viewPager.setCurrentItem(1);

            MatrisFragment matrisFragment = (MatrisFragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
            if (matrisFragment != null) {
                matrisFragment.refreshTasks(); // Verileri güncelle
            }
        }
    }


    private void deleteTaskFromFirestore(Task task) {
        tasksCollection.document(task.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getActivity(), "Görev silindi", Toast.LENGTH_SHORT).show();
                    fetchTasksFromFirestore(); // Görevleri yeniden yükle
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Görevi silerken hata oluştu", Toast.LENGTH_SHORT).show();
                });
    }

    private void switchToMatrisTab() {
        ViewPager viewPager = getActivity().findViewById(R.id.viewPager);
        if (viewPager != null) {
            viewPager.setCurrentItem(1);

            MatrisFragment matrisFragment = (MatrisFragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
            if (matrisFragment != null) {
                matrisFragment.refreshTasks(); // Verileri güncelle
            }
        }
    }

}
