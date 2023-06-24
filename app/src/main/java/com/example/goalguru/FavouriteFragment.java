package com.example.goalguru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private EgzersizAdapter adapter;
    private List<Egzersiz> egzersizListesi;

    public FavouriteFragment() {
        // Gerekli boş public yapılandırıcı
    }

    public static FavouriteFragment newInstance() {
        return new FavouriteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favourite, container, false);

        recyclerView = rootView.findViewById(R.id.fav_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Egzersiz listesini oluşturun ve adapteri ayarlayın
        egzersizListesi = createEgzersizListesi(); // Kendi egzersiz listesini oluşturun
        adapter = new EgzersizAdapter(egzersizListesi, new EgzersizAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Egzersiz egzersiz) {
                // Seçilen egzersiz öğesine tıklandığında yapılacak işlemler
                startEgzersizFragment(egzersiz);
            }
        });
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private List<Egzersiz> createEgzersizListesi() {
        // Egzersiz listesini burada oluşturun veya gerektiği şekilde alın
        // Örnek bir liste döndürüyorum
        List<Egzersiz> egzersizler = new ArrayList<>();
        egzersizler.add(new Egzersiz("Egzersiz 1", R.mipmap.ic_excercise));
        egzersizler.add(new Egzersiz("Egzersiz 2", R.mipmap.boat_pose));
        egzersizler.add(new Egzersiz("Egzersiz 3", R.mipmap.rest_person_icon));
        return egzersizler;
    }

    private void startEgzersizFragment(Egzersiz egzersiz) {
        EgzersizFragment fragment = EgzersizFragment.newInstance(egzersiz);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_activity_viewPager, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
