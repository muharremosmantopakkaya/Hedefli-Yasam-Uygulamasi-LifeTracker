package com.example.goalguru;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;


public class OrucPlanFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters


    public OrucPlanFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static OrucPlanFragment newInstance(String param1, String param2) {
        OrucPlanFragment fragment = new OrucPlanFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oruc_plan, container, false);

        Button btnLecis = view.findViewById(R.id.btnLecis);
        btnLecis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrucPlanActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
