package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExploreFragment extends Fragment implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {
    private IAddFragment fragmentListener;
    private IToastFromFragmentToMain toastListener;

    // UI Elements
    private SearchView searchView;
    private TextView textViewSearchWord, textViewSearchResultsFor;
    private List<ColorPalette> searchResults = new ArrayList<>();
    private ListView listView;
    private PaletteListEntryAdapter adapter;

    // Thread-related items
    private ExecutorService threadPool;
    private Handler messageQueue;

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

        // Setting up listView
        listView = view.findViewById(R.id.listViewExplore);
        listView.setOnItemClickListener(this);

        // Setting up thread
        threadPool = Executors.newFixedThreadPool(1);
        messageQueue = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch(msg.what) {
                    case LoadExploreSearchResults.STATUS_FAILURE:
                        Bundle receivedDataFailure = msg.getData();
                        if (receivedDataFailure.getString(LoadExploreSearchResults.TOAST_KEY) != null) {
                            toastListener.toastFromFragment(receivedDataFailure.getString(LoadExploreSearchResults.TOAST_KEY));
                        }
                        break;
                    case LoadExploreSearchResults.STATUS_SUCCESS:
                        Bundle receivedDataSuccess = msg.getData();
                        if (receivedDataSuccess.getStringArrayList(LoadExploreSearchResults.PALETTE_ARRAY) != null) {
                            // Setting up listview when a word is successfully searched
                            searchResults = convertStringToPalette(receivedDataSuccess.getStringArrayList(LoadExploreSearchResults.PALETTE_ARRAY));
                            adapter = new PaletteListEntryAdapter(getContext(), searchResults);
                            listView.setAdapter(adapter);
                        }
                        break;
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IAddFragment) {
            fragmentListener = (IAddFragment) context;
        }
        if (context instanceof IToastFromFragmentToMain) {
            toastListener = (IToastFromFragmentToMain) context;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!query.equals("")) {
            textViewSearchWord.setText(query);
            textViewSearchResultsFor.setVisibility(View.VISIBLE);
            threadPool.execute(new LoadExploreSearchResults(query, messageQueue));
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        fragmentListener.addExploreSearchResultFragment(searchResults.get(position));
    }

    private List<ColorPalette> convertStringToPalette(ArrayList<String> paletteListStr) {
        List<ColorPalette> paletteList = new ArrayList<>();
        for (int i = 0; i < paletteListStr.size(); i++) {
            ColorPalette palette = new ColorPalette();
            List<Integer> paletteColors = new ArrayList<>();
            String paletteStr = paletteListStr.get(i);
            String currentStr = "";
            int counter = 0;
            for (int j = 0; j < paletteStr.length(); j++) {
                String currentChar = paletteStr.substring(j, j + 1);
                if (currentChar.equals(",")) {
                    // The case that the current string is the palette title
                    if (counter == 0) {
                        palette.setName(currentStr);
                        currentStr = "";
                        counter++;
                    }
                    // The case that the current string is a palette hex color
                    else {
                        paletteColors.add(Integer.parseInt(currentStr, 16) + 0xFF000000);
                        currentStr = "";
                    }
                }
                else {
                    currentStr += currentChar;
                }
            }
            palette.setColors(paletteColors);
            paletteList.add(palette);
        }
        return paletteList;
    }
}