package com.example.goalguru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    // RecyclerView ve Adapter için değişkenler
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;

    // Diğer kodlar...

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // TabLayout ve ViewPager'ı bulma
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);

        // CustomViewPager oluşturma ve adapter atama
        CustomViewPager adapter = new CustomViewPager(getChildFragmentManager());
        adapter.addFragment(new EisenhowerFragment(), "Eisenhower");
        adapter.addFragment(new MatrisFragment(), "Matris");

        // ViewPager'a adapter atama
        viewPager.setAdapter(adapter);

        // TabLayout ile ViewPager'ı bağlama
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }


    // Örnek veri listesi oluşturma metodu
    private List<Task> createSampleTasks() {
        List<Task> tasks = new ArrayList<>();

        // Diğer görevleri ekleyin
        return tasks;
    }
}
