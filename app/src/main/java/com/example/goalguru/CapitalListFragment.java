package com.example.goalguru;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CapitalListFragment extends Fragment implements CapitalAdapter.OnItemClickListener {

    private OnCapitalClickListener listener;
    private RecyclerView recyclerView;
    private CapitalAdapter adapter;

    public interface OnCapitalClickListener {
        void onCapitalClick(String capitalName);

        void onItemClick(String capitalName);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnCapitalClickListener) {
            listener = (OnCapitalClickListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnCapitalClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        // RecyclerView'ı oluştur ve yapılandır
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CapitalAdapter(getCapitalList(), this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<String> getCapitalList() {
        // Başkentlerin listesini döndür
        List<String> capitalList = new ArrayList<>();
        capitalList.add("Azerbaijan");
        capitalList.add("Brazil");
        capitalList.add("China");
        capitalList.add("France");
        capitalList.add("Germany");
        capitalList.add("Hong Kong");
        capitalList.add("Japan");
        capitalList.add("Luxembourg");
        capitalList.add("Portugal");
        capitalList.add("Spain");
        capitalList.add("Sri Lanka");
        capitalList.add("Turkey");
        capitalList.add("Uganda");
        capitalList.add("United States");

        return capitalList;
    }

    @Override
    public void onItemClick(String capitalName) {
        if (listener != null) {
            listener.onCapitalClick(capitalName);
        }
    }
}
