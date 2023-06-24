package com.example.goalguru;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class CapitalDetailFragment extends Fragment {

    private StorageReference storageReference;
    private ImageView flagImageView;
    private Button populationButton;
    private TextView capitalTextView;

    private static final String ARG_CAPITAL_NAME = "capitalName";

    private String capitalName;

    private HashMap<String, String> flagMap;
    private HashMap<String, String> populationMap;

    public static CapitalDetailFragment newInstance(String capitalName) {
        CapitalDetailFragment fragment = new CapitalDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CAPITAL_NAME, capitalName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bayrak dosya adlarını ve ülke isimlerini içeren HashMap
        flagMap = new HashMap<>();

        flagMap.put("Azerbaijan", "azerbaijan.png");
        flagMap.put("Brazil", "brazil-.png");
        flagMap.put("China", "china.png");
        flagMap.put("France", "france.png");
        flagMap.put("Germany", "germany.png");
        flagMap.put("Hong Kong", "hong-kong.png");
        flagMap.put("Japan", "japan.png");
        flagMap.put("Luxembourg", "luxembourg.png");
        flagMap.put("Portugal", "portugal.png");
        flagMap.put("Spain", "spain.png");
        flagMap.put("Sri Lanka", "sri-lanka.png");
        flagMap.put("Turkey", "turkey.png");
        flagMap.put("Uganda", "uganda.png");
        flagMap.put("United States", "united-states.png");



        populationMap = new HashMap<>();
        populationMap.put("Azerbaijan", "Bakü Nüfusu: 10,14 milyon");
        populationMap.put("Brazil", "Brasília Nüfus: 214,3 milyon");
        populationMap.put("China", "Pekin  Nüfus 1,412 milyar");
        populationMap.put("France", "Paris Nüfus: 67,3 milyon");
        populationMap.put("Germany", "Berlin Nüfus: 83,1 milyon");
        populationMap.put("Hong Kong", "Hong KongNüfus: 7,403 milyon");
        populationMap.put("Japan", "Tokyo Nüfus: 125 milyon");
        populationMap.put("Luxembourg", "Lüksemburg Nüfus: 645 bin");
        populationMap.put("Portugal", "Lizbon Nüfus: 10,3 milyon");
        populationMap.put("Spain", "Madrid Nüfus: 47,3 milyon");
        populationMap.put("Sri Lanka", "Kolombo Nüfus: 22,1 milyon");
        populationMap.put("Turkey", "Ankara Nüfus: 84,68 milyon");
        populationMap.put("Uganda", "Kampala Nüfus: 42,8 milyon");
        populationMap.put("United States", "Washington D.C. Nüfus: 332,8 milyon");
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_capital_detail, container, false);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyCache", Context.MODE_PRIVATE);

        flagImageView = view.findViewById(R.id.flag_image_view);
        populationButton = view.findViewById(R.id.population_button);
        capitalTextView = view.findViewById(R.id.capital_text_view);

        // Başkentin adını al
        if (getArguments() != null) {
            capitalName = getArguments().getString(ARG_CAPITAL_NAME);
        }

        // Firebase Storage referansını al
        storageReference = FirebaseStorage.getInstance().getReference();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("capitalName", capitalName);
        editor.putString("populationInfo", populationMap.get(capitalName));
        editor.apply();

        // Bayrak resmini yükle
        loadFlagImage(capitalName);

        // Nüfus bilgilerini göstermek için butona tıklama işlemini tanımla
        populationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String capital = sharedPreferences.getString("capitalName", "");
                String populationInfo = sharedPreferences.getString("populationInfo", "");

                showPopulationInfo(capitalName);
            }
        });

        // Quiz butonuna tıklama işlemini tanımla
        Button quizButton = view.findViewById(R.id.quiz_button);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuizActivity();
            }
        });

        return view;
    }

    private void loadFlagImage(String capital) {
        // Başkent adına karşılık gelen bayrak dosyasının adını al
        String flagFileName = flagMap.get(capital);

        // Bayrak dosyasının adı boş değilse Firebase Storage'dan indir
        if (flagFileName != null) {
            String flagPath = flagFileName; // Bayrak dosya adı
            StorageReference flagRef = storageReference.child(flagPath);

            // Bayrağı indir ve ImageView'a yükle
            flagRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    if (isAdded()) {
                        Glide.with(requireContext())
                                .load(uri)
                                .into(flagImageView);
                        flagImageView.post(new Runnable() {
                            @Override
                            public void run() {
                                flagImageView.requestLayout();
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Bayrak indirme hatası
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Bayrak indirme hatası.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(requireContext(), "Bayrak dosyası bulunamadı.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPopulationInfo(String capital) {
        // Başkent adına karşılık gelen nüfus bilgisini al
        String populationInfo = populationMap.get(capital);

        // Toast mesajı için TextView oluştur
        TextView toastTextView = new TextView(requireContext());
        toastTextView.setTextSize(24); // Yazı tipi boyutunu ayarla (örneğin 24sp)
        toastTextView.setText(populationInfo);

        // Toast mesajını göster
        Toast toast = new Toast(requireContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastTextView);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 1600); // Yükseklik ayarını buradan yapabilirsiniz
        toast.show();
    }

    private void startQuizActivity() {
        Intent intent = new Intent(requireContext(), QuizActivity.class);
        intent.putExtra("capitalName", capitalName);
        intent.putExtra("correctAnswer", populationMap.get(capitalName));
        startActivity(intent);
    }

    public void quizekraninagit(View view) {
        startQuizActivity();
    }
}

