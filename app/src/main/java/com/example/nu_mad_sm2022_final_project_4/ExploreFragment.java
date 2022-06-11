package com.example.nu_mad_sm2022_final_project_4;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class ExploreFragment extends Fragment implements SearchView.OnQueryTextListener {
    // UI Elements
    private SearchView searchView;
    private TextView textViewSearchWord, textViewSearchResultsFor;

    // RecyclerView-related items
    private RecyclerView recyclerView;
    private ExploreSearchAdapter adapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ArrayList<ColorPalette> searchResults = new ArrayList<>();

    public ExploreFragment() {}

    public static ExploreFragment newInstance() {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        getActivity().setTitle("Explore");

        // Defining UI Elements
        searchView = view.findViewById(R.id.searchViewExplore);
        searchView.setOnQueryTextListener(this);
        textViewSearchWord = view.findViewById(R.id.textViewExploreSearchWord);
        textViewSearchResultsFor = view.findViewById(R.id.textViewExploreSearchResultsFor);
        textViewSearchResultsFor.setVisibility(View.INVISIBLE);

        // Setting up recyclerview
        recyclerView = view.findViewById(R.id.recyclerViewExplore);
        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        adapter = new ExploreSearchAdapter(searchResults);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!query.equals("")) {
            textViewSearchWord.setText(query);
            textViewSearchResultsFor.setVisibility(View.VISIBLE);
            // update words for searchResults
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // update suggestion list
        return false;
    }

    /*
    Add event for when user clicks on a row - different fragment
    Also after entering when searching a word, the textView that says "Search results for..." updates
    After clicking on a specific palette - explore search result fragment is populated
     */
}