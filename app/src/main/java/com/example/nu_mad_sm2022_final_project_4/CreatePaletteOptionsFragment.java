package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CreatePaletteOptionsFragment extends Fragment implements View.OnClickListener {
    private IAddFragment fragmentListener;
    private Button buttonTakePhoto, buttonAddPhoto, buttonManuallyAddColors;

    public CreatePaletteOptionsFragment() {}

    public static CreatePaletteOptionsFragment newInstance() {
        CreatePaletteOptionsFragment fragment = new CreatePaletteOptionsFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_palette_options, container, false);
        getActivity().setTitle("Add New Palette");

        // Defining UI Elements
        buttonTakePhoto = view.findViewById(R.id.buttonCreatePaletteOptionsTakePhoto);
        buttonTakePhoto.setOnClickListener(this);
        buttonAddPhoto = view.findViewById(R.id.buttonCreatePaletteOptionsAddPhoto);
        buttonAddPhoto.setOnClickListener(this);
        buttonManuallyAddColors = view.findViewById(R.id.buttonCreatePaletteOptionsManuallyAddColors);
        buttonManuallyAddColors.setOnClickListener(this);

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
    public void onClick(View v) {
        if (v.getId() == R.id.buttonCreatePaletteOptionsTakePhoto) {
            fragmentListener.addCameraFragment();
        }
        else if (v.getId() == R.id.buttonCreatePaletteOptionsAddPhoto) {
            fragmentListener.addDisplayPhotoGalleryFragment();
        }
        else if (v.getId() == R.id.buttonCreatePaletteOptionsManuallyAddColors) {
            fragmentListener.addCreatePaletteManuallyFragment();
        }
    }
}