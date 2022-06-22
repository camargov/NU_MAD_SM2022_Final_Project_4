package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExploreFragment extends Fragment implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {
    private IAddFragment fragmentListener;

    // UI Elements
    private SearchView searchView;
    private TextView textViewSearchWord, textViewSearchResultsFor;
    private List<ColorPalette> searchResults = new ArrayList<>();
    private ListView listView;
    private PaletteListEntryAdapter adapter;

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

        // TEMPORARY FOR TESTING:
        searchResults.add(
                new ColorPalette("Primary RGB",
                new ArrayList<Integer>(Arrays.asList(Color.RED, Color.GREEN, Color.BLUE))));
        searchResults.add(
                new ColorPalette("Primary CMYK",
                        new ArrayList<Integer>(Arrays.asList(Color.CYAN, Color.MAGENTA, Color.YELLOW))));
        searchResults.add(
                new ColorPalette("Valentines Day",
                        new ArrayList<Integer>(Arrays.asList(Color.MAGENTA, Color.BLUE, Color.RED))));

        // Defining UI Elements
        searchView = view.findViewById(R.id.searchViewExplore);
        searchView.setOnQueryTextListener(this);
        textViewSearchWord = view.findViewById(R.id.textViewExploreSearchWord);
        textViewSearchResultsFor = view.findViewById(R.id.textViewExploreSearchResultsFor);
        textViewSearchResultsFor.setVisibility(View.INVISIBLE);

        // Setting up listView
        listView = view.findViewById(R.id.listViewExplore);
        adapter = new PaletteListEntryAdapter(this.getContext(), searchResults);
        this.listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IAddFragment) {
            fragmentListener = (IAddFragment) context;
        }
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
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        fragmentListener.addExploreSearchResultFragment(searchResults.get(position), true);
    }
}