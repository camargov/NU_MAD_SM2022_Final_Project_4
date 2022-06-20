package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CreatePaletteFromImageFragment extends Fragment implements View.OnClickListener {
    private Uri image_uri;
    private IToastFromFragmentToMain toastListener;
    private IAddFragment fragmentListener;

    // UI Elements:
    private EditText editTextPaletteName;
    private Button buttonSeeMorePalettes, buttonSave;
    private ImageView imageView;

    // Setting up recyclerView
    private RecyclerView recyclerViewProminentColors;
    private ColorDescriptionRowAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ArrayList<Integer> colors = new ArrayList<>();

    public CreatePaletteFromImageFragment(Uri image_uri) {
        this.image_uri = image_uri;
    }

    public static CreatePaletteFromImageFragment newInstance(Uri image_uri) {
        CreatePaletteFromImageFragment fragment = new CreatePaletteFromImageFragment(image_uri);
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
        buttonSeeMorePalettes = view.findViewById(R.id.buttonCreatePaletteFromImageSeeMorePalettes);
        buttonSave = view.findViewById(R.id.buttonCreatePaletteFromImageSave);
        imageView = view.findViewById(R.id.imageViewCreatePaletteFromImage);
        Picasso.get().load(image_uri).into(imageView);
        buttonSeeMorePalettes.setOnClickListener(this);
        buttonSave.setOnClickListener(this);

        // Set up colors array

        // Setting up recyclerView
        recyclerViewProminentColors = view.findViewById(R.id.recyclerViewCreatePaletteFromImageProminentColors);
        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewProminentColors.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewAdapter = new ColorDescriptionRowAdapter(colors);
        recyclerViewProminentColors.setAdapter(recyclerViewAdapter);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof IToastFromFragmentToMain) {
            toastListener = (IToastFromFragmentToMain) context;
        }
        if (context instanceof IAddFragment) {
            fragmentListener = (IAddFragment) context;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonCreatePaletteFromImageSeeMorePalettes) {
            // pass this to the next fragment for when user chooses another palette or for a back button
            fragmentListener.addCreatePaletteFromImageSeeMorePalettesFragment();
        }
        else if (v.getId() == R.id.buttonCreatePaletteFromImageSave) {
            if (editTextPaletteName.getText().toString().equals("")) {
                toastListener.toastFromFragment("Palette must have a name.");
            }
            else {
                /*
            Map<String, Object> palette = new HashMap<>();
            palette.put("name", editTextPaletteName.getText().toString());
            palette.put("colors", colors);
            palette.put("image", );

            db.collection("users)
            .document(mUser.getEmail())
            .collection("palettes")
            .set(palette);
             */
            }
        }
    }
}