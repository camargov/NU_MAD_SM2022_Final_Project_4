package com.example.nu_mad_sm2022_final_project_4;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

public class ExploreFragment extends Fragment {
    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;*/
    private SearchView searchView;
    private TextView textViewSearchWord;
    private RecyclerView recyclerView;

    public ExploreFragment() {}

    public static ExploreFragment newInstance() {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        searchView = view.findViewById(R.id.searchViewExplore);
        textViewSearchWord = view.findViewById(R.id.textViewExploreSearchWord);
        recyclerView = view.findViewById(R.id.recyclerViewExplore);

        getActivity().setTitle("Explore");
        return view;
    }
}