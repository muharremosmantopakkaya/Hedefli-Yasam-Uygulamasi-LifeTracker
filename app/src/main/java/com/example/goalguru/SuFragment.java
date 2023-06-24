package com.example.goalguru;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SuFragment extends Fragment {

    private TextView tvCurrentIntake;
    private LinearLayout op50ml, op100ml, op150ml;
    private FloatingActionButton fabAdd, fabSubtract;
    private int currentIntake = 0;
    private int totalIntake = 2000;
    private int lastSelectedAmount = 50;
    private TextView tvCompletionMessage;
    private Handler handler;
    private Runnable hideCompletionMessageRunnable;

    public SuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_su, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvCurrentIntake = view.findViewById(R.id.tvCurrentIntake);
        TextView tvRemainingIntake = view.findViewById(R.id.tvRemainingIntake);
        tvCompletionMessage = view.findViewById(R.id.tvCompletionMessage);
        op50ml = view.findViewById(R.id.op50ml);
        op100ml = view.findViewById(R.id.op100ml);
        op150ml = view.findViewById(R.id.op150ml);
        fabAdd = view.findViewById(R.id.fabAdd);
        fabSubtract = view.findViewById(R.id.fabSubtract);

        updateCurrentIntake();
        tvRemainingIntake.setText("Hedef: " + (totalIntake - currentIntake) + " ml");
        tvCompletionMessage.setVisibility(View.GONE);

        op50ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedAmount = 50;
                currentIntake += lastSelectedAmount;
                updateCurrentIntake();
                updateRemainingIntake();
                checkCompletion();
            }
        });

        op100ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedAmount = 100;
                currentIntake += lastSelectedAmount;
                updateCurrentIntake();
                updateRemainingIntake();
                checkCompletion();
            }
        });

        op150ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedAmount = 150;
                currentIntake += lastSelectedAmount;
                updateCurrentIntake();
                updateRemainingIntake();
                checkCompletion();
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIntake += lastSelectedAmount;
                updateCurrentIntake();
                updateRemainingIntake();
                checkCompletion();
            }
        });

        fabSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIntake -= lastSelectedAmount;
                if (currentIntake < 0) {
                    currentIntake = 0;
                }
                updateCurrentIntake();
                updateRemainingIntake();
                checkCompletion();
            }
        });

        handler = new Handler();
        hideCompletionMessageRunnable = new Runnable() {
            @Override
            public void run() {
                tvCompletionMessage.setVisibility(View.GONE);
            }
        };

    }

    private void updateCurrentIntake() {
        tvCurrentIntake.setText(currentIntake + " ml");
    }

    private void updateRemainingIntake() {
        TextView tvRemainingIntake = getView().findViewById(R.id.tvRemainingIntake);
        tvRemainingIntake.setText("Hedef: " + (totalIntake - currentIntake) + " ml");
    }

    private boolean isCompletionShown = false;

    private void checkCompletion() {
        if (currentIntake >= totalIntake && !isCompletionShown) {
            tvCompletionMessage.setText("Hedef başarıyla tamamlandı!");
            tvCompletionMessage.setVisibility(View.VISIBLE);
            isCompletionShown = true;
            handler.postDelayed(hideCompletionMessageRunnable, 2000);
        } else if (currentIntake < totalIntake && isCompletionShown) {
            isCompletionShown = false;
            tvCompletionMessage.setVisibility(View.GONE);
            handler.removeCallbacks(hideCompletionMessageRunnable);
        }
    }



}
