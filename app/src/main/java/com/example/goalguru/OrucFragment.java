    package com.example.goalguru;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;

    import androidx.fragment.app.Fragment;

    public class OrucFragment extends Fragment {


        public OrucFragment() {
            // Required empty public constructor
        }


        // TODO: Rename and change types and number of parameters
        public static OrucFragment newInstance(String param1, String param2) {
            OrucFragment fragment = new OrucFragment();
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
            View view = inflater.inflate(R.layout.fragment_oruc, container, false);

            Button btnCecis = view.findViewById(R.id.btnCecis);
            btnCecis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), OrucTakibi.class);
                    startActivity(intent);
                }
            });
            Button btnGeri = view.findViewById(R.id.btnGeri);
            btnGeri.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Geri butonuna tıklandığında yapılacak işlemleri buraya yazın
                    Intent intent = new Intent(getActivity(), bottom.class);
                    startActivity(intent);
                }
            });
            return view;


        }
    }