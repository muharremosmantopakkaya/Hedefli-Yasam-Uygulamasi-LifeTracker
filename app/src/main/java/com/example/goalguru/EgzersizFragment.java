package com.example.goalguru;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class EgzersizFragment extends Fragment {

    private static final String ARG_EGZERSIZ = "egzersiz";
    private static final long TOTAL_TIME = 60000;

    private CardView cardView;
    private TextView exerciseName;
    private ImageView yogaImage;
    private TextView exerciseInstruction;
    private ImageView restImage;
    private TextView startBtnText;
    private ProgressBar progressBar;
    private TextView counter;
    private Button addExerciseButton;

    private Egzersiz egzersiz;
    private List<Egzersiz> egzersizListesi;
    private ArrayList<Egzersiz> exercises;
    private int currentExerciseIndex = 0;
    public EgzersizFragment() {
        // Gerekli boş public yapılandırıcı
    }

    public static EgzersizFragment newInstance(Egzersiz egzersiz) {
        EgzersizFragment fragment = new EgzersizFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_EGZERSIZ, egzersiz);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_egzersiz, container, false);

        cardView = rootView.findViewById(R.id.cardView);
        exerciseName = rootView.findViewById(R.id.excerise_name);
        yogaImage = rootView.findViewById(R.id.youga_image);
        exerciseInstruction = rootView.findViewById(R.id.excerise_instruction);
        restImage = rootView.findViewById(R.id.rest_image);
        startBtnText = rootView.findViewById(R.id.start_btn_text);
        progressBar = rootView.findViewById(R.id.progressBar);
        counter = rootView.findViewById(R.id.counter);
        addExerciseButton = rootView.findViewById(R.id.add_exercise_button);

        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewExercise();
            }
        });

        startBtnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExercise();
            }
        });

        // Egzersiz verilerini al
        if (getArguments() != null) {
            egzersiz = getArguments().getParcelable(ARG_EGZERSIZ);
            if (egzersiz != null) {
                exerciseName.setText(egzersiz.getAd());
                yogaImage.setImageResource(egzersiz.getResimId());
            }
        }

        return rootView;
    }

    private void addNewExercise() {
        if (egzersizListesi == null) {
            egzersizListesi = createEgzersizListesi();
        }

        if (egzersizListesi.size() > 1) {
            int currentIndex = egzersizListesi.indexOf(egzersiz);
            int nextIndex = currentIndex + 1;
            if (nextIndex < egzersizListesi.size()) {
                Egzersiz nextEgzersiz = egzersizListesi.get(nextIndex);
                EgzersizFragment fragment = EgzersizFragment.newInstance(nextEgzersiz);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit();
            }
        }
    }

    private void startExercise() {
        yogaImage.setVisibility(View.GONE);
        exerciseInstruction.setVisibility(View.GONE);
        restImage.setVisibility(View.VISIBLE);
        updateProgressBar(0);
        startTimer();

        animateImage();
    }

    private void updateProgressBar(int progress) {
        progressBar.setProgress(progress);
        counter.setText(String.valueOf(progress));
    }

    private void animateImage() {
        restImage.setTranslationX(-200f);
        restImage.animate()
                .translationX(0f)
                .setDuration(1000)
                .start();
    }

    private void startTimer() {
        new CountDownTimer(TOTAL_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) ((TOTAL_TIME - millisUntilFinished) * 100 / TOTAL_TIME);
                updateProgressBar(progress);
            }

            @Override
            public void onFinish() {
                // Egzersiz tamamlandığında yapılacak işlemler
            }
        }.start();
    }

    private List<Egzersiz> createEgzersizListesi() {
        List<Egzersiz> egzersizler = new ArrayList<>();
        egzersizler.add(new Egzersiz("Egzersiz 1", R.mipmap.ic_excercise));
        egzersizler.add(new Egzersiz("Egzersiz 2", R.mipmap.boat_pose));
        egzersizler.add(new Egzersiz("Egzersiz 3", R.mipmap.rest_person_icon));
        return egzersizler;
    }
}
