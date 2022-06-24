package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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
        toggleClickability(true);

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
            toggleClickability(false);
            fragmentListener.addCameraFragment();
        }
        else if (v.getId() == R.id.buttonCreatePaletteOptionsAddPhoto) {
            Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            toggleClickability(false);
            startActivityForResult(gallery_intent,3);
//            fragmentListener.addDisplayPhotoGalleryFragment();
        }
        else if (v.getId() == R.id.buttonCreatePaletteOptionsManuallyAddColors) {
            toggleClickability(false);
            fragmentListener.addCreatePaletteManuallyFragment();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(data !=null){
            Uri selectedImage = data.getData();
            if(getActivity() instanceof Utils.IPhotoPicked){
                Utils.IPhotoPicked photoPickedInstance = (Utils.IPhotoPicked) getActivity();
                photoPickedInstance.photoPicked(selectedImage);
            }
        }
    }

    private void toggleClickability(boolean clickable){
        buttonAddPhoto.setClickable(clickable);
        buttonTakePhoto.setClickable(clickable);
        buttonManuallyAddColors.setClickable(clickable);
    }

}