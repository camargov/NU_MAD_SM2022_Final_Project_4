package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditPaletteFragment extends CreatePaletteManuallyFragment {
    private static final String ARG_PALETTE_NAME = "paletteName";

    private ColorPalette palette;

    public EditPaletteFragment() {

    }

    public static EditPaletteFragment newInstance(ColorPalette palette) {
        EditPaletteFragment fragment = new EditPaletteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PALETTE_NAME, palette.getName());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String paletteName = getArguments().getString(ARG_PALETTE_NAME);
        try {
            ColorPalette palette = Utils.readPalettesLocally(getActivity())
                    .stream().filter(other -> paletteName.equals(other.getName()))
                    .collect(Collectors.toList())
                    .get(0);
            this.palette = palette;
        } catch (IOException e) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        editTextPaletteName.setText(palette.getName());
        makePublic.setChecked(palette.getCloudPalette());
        List<String> currentColors = palette.getColors().stream()
                .map(val -> String.format("#%06X", val & 0x00FFFFFF))
                .collect(Collectors.toList());
        colors.addAll(currentColors);
        recyclerViewAdapter.notifyDataSetChanged();
        buttonSave.setOnClickListener(v -> {
            // Update local data
            // Resync everything
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}