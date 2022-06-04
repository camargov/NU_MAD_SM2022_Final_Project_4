package com.example.nu_mad_sm2022_final_project_4;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CreatePaletteFromImageFragment extends Fragment implements View.OnClickListener {
    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;*/

    // UI Elements:
    private EditText editTextPaletteName;
    private Button buttonSeeMore, buttonSave;
    private ImageView imageView;
    private TextView textViewMainColors;
    private RecyclerView recyclerViewSuggestedPalettes;

    public CreatePaletteFromImageFragment() {}

    public static CreatePaletteFromImageFragment newInstance() {
        CreatePaletteFromImageFragment fragment = new CreatePaletteFromImageFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_palette_from_image, container, false);

        // Defining UI Elements:
        editTextPaletteName = view.findViewById(R.id.editTextCreatePaletteFromImageName);
        buttonSeeMore = view.findViewById(R.id.buttonCreatePaletteFromImageSeeMore);
        buttonSave = view.findViewById(R.id.buttonCreatePaletteFromImageSave);
        imageView = view.findViewById(R.id.imageViewCreatePaletteFromImage);
        textViewMainColors = view.findViewById(R.id.textViewCreatePaletteFromImageMainColors);
        recyclerViewSuggestedPalettes = view.findViewById(R.id.recyclerViewCreatePaletteFromImageSuggestedPalettes);
        buttonSeeMore.setOnClickListener(this);
        buttonSave.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonCreatePaletteFromImageSeeMore) {

        }
        else if (v.getId() == R.id.buttonCreatePaletteFromImageSave) {

        }
    }
}