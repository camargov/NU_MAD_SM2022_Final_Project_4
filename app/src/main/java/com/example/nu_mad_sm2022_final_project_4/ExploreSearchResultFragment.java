package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ExploreSearchResultFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_COLOR_PALETTE = "ARG_COLOR_PALETTE";
    private ColorPalette colorPalette;

    // UI Elements
    private TextView textViewPaletteName, textViewBack;
    private ImageView imageViewBack;
    private Button buttonSavePalette;
    private LinearLayout linearLayout;
    private BigPaletteColorsAdapter linearLayoutAdapter;
    private RecyclerView recyclerView;
    private ColorDescriptionRowAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ArrayList<Integer> colors = new ArrayList<>();

    // Firestore-related items
    //private db;
    //private mAuth;
    //private FirebaseUser mUser;

    public ExploreSearchResultFragment() {}

    public static ExploreSearchResultFragment newInstance(ColorPalette colorPalette) {
        ExploreSearchResultFragment fragment = new ExploreSearchResultFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_COLOR_PALETTE, colorPalette);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            colorPalette = (ColorPalette) getArguments().getSerializable(ARG_COLOR_PALETTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_search_result, container, false);
        getActivity().setTitle("Explore");

        // Defining UI Elements
        textViewPaletteName = view.findViewById(R.id.textViewExploreSearchResultPaletteName);
        textViewPaletteName.setText(colorPalette.getName());
        textViewBack = view.findViewById(R.id.textViewExploreSearchResultBack);
        textViewBack.setOnClickListener(this);
        buttonSavePalette = view.findViewById(R.id.buttonExploreSearchResultSavePalette);
        buttonSavePalette.setOnClickListener(this);
        imageViewBack = view.findViewById(R.id.imageViewExploreSearchResultBack);
        imageViewBack.setOnClickListener(this);

        // Setting up recyclerView
        recyclerView = view.findViewById(R.id.recyclerViewExploreSearchResult);
        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        for (Integer i: colorPalette.getColors()) {
            colors.add(i);
        }
        recyclerViewAdapter = new ColorDescriptionRowAdapter(colors);
        recyclerView.setAdapter(recyclerViewAdapter);

        // Setting up linearLayout
        linearLayout = view.findViewById(R.id.linearLayoutExploreSearchResult);
        linearLayoutAdapter = new BigPaletteColorsAdapter(this.getContext(), colorPalette.getColors());
        for(int i = 0; i < linearLayoutAdapter.getCount(); i++) {
            linearLayout.addView(linearLayoutAdapter.getView(i, null, linearLayout));
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonExploreSearchResultSavePalette) {
            // add palette to user's palette collection
            /*
            db.collection("users")
            .document(mUser.getEmail())
            .collection("palettes")
            .document()
            .set(colorPalette);

            // after saving is successful, go back to explore page with search results
             (pop back this fragment)
             */
            getActivity().getSupportFragmentManager().popBackStack();
        }
        else if (v.getId() == R.id.imageViewExploreSearchResultBack
                || v.getId() == R.id.textViewExploreSearchResultBack) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}