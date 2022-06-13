package com.example.nu_mad_sm2022_final_project_4;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CreatePaletteFromImageSeeMorePalettesFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
     */
    // UI ELements
    private ImageView imageViewBack;
    private ListView listView;
    private List<ColorPalette> palettes = new ArrayList<>();
    private PaletteListEntryAdapter adapter;

    public CreatePaletteFromImageSeeMorePalettesFragment() {}

    public static CreatePaletteFromImageSeeMorePalettesFragment newInstance() {
        CreatePaletteFromImageSeeMorePalettesFragment fragment = new CreatePaletteFromImageSeeMorePalettesFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
         */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_palette_from_image_see_more_palettes, container, false);

        // Set up list of palettes

        // Defining UI Elements
        imageViewBack = view.findViewById(R.id.imageViewCreatePaletteFromImageSeeMorePalettesBack);
        imageViewBack.setOnClickListener(this);
        listView = view.findViewById(R.id.listViewCreatePaletteFromImageSeeMorePalettes);
        adapter = new PaletteListEntryAdapter(this.getContext(), palettes);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // go to previous fragment and pass in the chosen palette to reset the row description
        // pass in the previous fragment and save to a variable clickListener
        // maybe the previous fragment implements an interface that allows this fragment to set
        // a clicked color palette that the previous fragment can get
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageViewCreatePaletteFromImageSeeMorePalettesBack) {
            // go to previous fragment - pop back this fragment
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}