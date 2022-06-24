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
import android.widget.CompoundButton;
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

    private View.OnClickListener saveListener;
    private View.OnClickListener deleteListener;

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

        delete.setVisibility(View.VISIBLE);
        delete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchSaveButton(isChecked);
        });

        this.saveListener = v -> {
            if (colors.size() == 0) {
                toastListener.toastFromFragment("Palette must have at least one color.");
            }
            else {
                if (editTextPaletteName.getText().toString().equals("")) {
                    toastListener.toastFromFragment("Palette must have a name");
                }
                else {
                    boolean makeCloud = makePublic.isChecked();
                    String newName = editTextPaletteName.getText().toString();
                    ColorPalette newPalette = new ColorPalette(
                            newName,
                            Utils.getCurrentUserId(),
                            makeCloud,
                            convertColorsToInt()
                    );

                    try {
                        Utils.replacePaletteLocally(getActivity(), palette, newPalette);
                        Utils.syncLocalPaletteDataToCloud(getActivity(), () -> fragmentListener.addCreatePaletteOptionsFragment(),
                                () -> {
                                    Toast.makeText(getActivity(), "Something went wrong; adding palette locally, try restarting later to re-sync public data with cloud", Toast.LENGTH_LONG).show();
                                    fragmentListener.addCreatePaletteOptionsFragment();
                                }, true);
                    } catch(IOException e) {
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
        this.deleteListener = v -> {
            try {
                Utils.deletePaletteLocally(getActivity(), palette);
                Utils.syncLocalPaletteDataToCloud(
                        getActivity(),
                        fragmentListener::addCreatePaletteOptionsFragment,
                        () -> {
                            Toast.makeText(getActivity(), "Something went wrong with deletion, try again later!", Toast.LENGTH_LONG).show();
                            fragmentListener.addCreatePaletteOptionsFragment();
                        },
                        true);
            } catch (IOException e) {
                Toast.makeText(getActivity(), "Something went wrong with deletion, try again later!", Toast.LENGTH_LONG).show();
                fragmentListener.addCreatePaletteOptionsFragment();
            }
        };

        editTextPaletteName.setText(palette.getName());
        makePublic.setChecked(palette.getCloudPalette());
        List<String> currentColors = palette.getColors().stream()
                .map(val -> String.format("#%06X", val & 0x00FFFFFF))
                .collect(Collectors.toList());
        colors.addAll(currentColors);
        recyclerViewAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonCreatePaletteManuallySave) {
            if (delete.isChecked()) {
                deleteListener.onClick(buttonSave);
            } else {
                saveListener.onClick(buttonSave);
            }
        } else {
            super.onClick(v);
        }
    }

    private void switchSaveButton(boolean deleteActive) {
        buttonSave.setText(deleteActive ? "DELETE" : "SAVE");
    }
}