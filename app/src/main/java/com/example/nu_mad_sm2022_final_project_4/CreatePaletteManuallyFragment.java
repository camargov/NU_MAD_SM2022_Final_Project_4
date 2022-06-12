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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CreatePaletteManuallyFragment extends Fragment implements View.OnClickListener {
    private IAddFragment fragmentListener;
    private IToastFromFragmentToMain toastListener;
    private boolean isAddButton = true;
    private int colorPosition;

    // UI Elements
    private EditText editTextPaletteName, editTextColorHexCode;
    private Button buttonSave, buttonAdd;
    private TextView textViewAddColorHex;
    private LinearLayout linearLayout;
    private PaletteColorsViewAdapter linearLayoutAdapter;

    // RecyclerView-related items
    private RecyclerView recyclerView;
    private AddColorManuallyAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ArrayList<String> colors = new ArrayList<>();

    public CreatePaletteManuallyFragment() {}

    public static CreatePaletteManuallyFragment newInstance() {
        CreatePaletteManuallyFragment fragment = new CreatePaletteManuallyFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_palette_manually, container, false);
        getActivity().setTitle("Add New Palette");
        editTextPaletteName = view.findViewById(R.id.editTextCreatePaletteManuallyName);
        editTextColorHexCode = view.findViewById(R.id.editTextCreatePaletteManuallyColorHex);
        textViewAddColorHex = view.findViewById(R.id.textViewCreatePaletteManuallyAddColorHexCode);
        buttonAdd = view.findViewById(R.id.buttonCreatePaletteManuallyAdd);
        buttonAdd.setOnClickListener(this);
        buttonSave = view.findViewById(R.id.buttonCreatePaletteManuallySave);
        buttonSave.setOnClickListener(this);

        // Setting up linear layout
       /*linearLayout = view.findViewById(R.id.linearLayoutCreatePaletteManually);
        linearLayoutAdapter = new PaletteColorsViewAdapter(this.getContext(), getColorsIntList());
        for(int i = 0; i < linearLayoutAdapter.getCount(); i++) {
            linearLayout.addView(linearLayoutAdapter.getView(i, null, linearLayout));
        }
        */

        // Setting up recyclerview
        recyclerView = view.findViewById(R.id.recyclerViewCreatePaletteManually);
        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewAdapter = new AddColorManuallyAdapter(colors, this);
        recyclerView.setAdapter(recyclerViewAdapter);

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
    public void onClick(View v) {
        if (v.getId() == R.id.buttonCreatePaletteManuallySave) {
            // save palette to user's database
        }
        else if (v.getId() == R.id.buttonCreatePaletteManuallyAdd) {
            addHexFormat(editTextColorHexCode.getText().toString());
        }
    }

    private void addHexFormat(String hexCode) {
        if ((hexCode.substring(0, 1).equals("#") && hexCode.length() == 7)
        || (hexCode.length() == 6)) {
            boolean validChar = true;
            for (int i = 1; i < hexCode.length(); i++) {
                validChar = validChar && (Character.digit(hexCode.charAt(i), 16) != -1);
            }
            if (validChar) {
                if (!isAddButton) {
                    if (hexCode.length() == 6) {
                        colors.set(colorPosition, "#" + editTextColorHexCode.getText().toString());
                    }
                    else {
                        colors.set(colorPosition, editTextColorHexCode.getText().toString());
                    }
                    //linearLayoutAdapter.notifyDataSetChanged();
                    //linearLayout.addView(linearLayoutAdapter.getView(colors.size() - 1, null, linearLayout));
                    // Resetting the UI elements
                    textViewAddColorHex.setText("Add Color Hex Code");
                    buttonAdd.setText("Add");
                    editTextColorHexCode.setText("");
                    isAddButton = true;
                }
                else {
                    if (hexCode.length() == 6) {
                        colors.add("#" + hexCode);
                    }
                    else {
                        colors.add(hexCode);
                    }
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }
            else {
                toastListener.toastFromFragment("Invalid Character. Hex Characters: 0-9 and A-F");
            }
        }
        else {
            toastListener.toastFromFragment("Invalid Hex Color Format. Correct format: #000000 or 000000");
        }
    }

    public void editButtonAction(int position) {
        isAddButton = false;
        textViewAddColorHex.setText("Edit Hex Color");
        buttonAdd.setText("Save");
        editTextColorHexCode.setText(colors.get(position));
        colorPosition = position;
    }

    // Converts the list of colors from a String to an Integer
    private List<Integer> getColorsIntList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < colors.size(); i++) {
            list.add(Integer.decode(colors.get(i)));
        }
        return list;
    }
}