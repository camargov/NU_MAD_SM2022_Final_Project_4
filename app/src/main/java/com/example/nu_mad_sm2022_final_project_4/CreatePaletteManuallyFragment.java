package com.example.nu_mad_sm2022_final_project_4;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CreatePaletteManuallyFragment extends Fragment implements View.OnClickListener {
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";
    //private String mParam1;
    //private String mParam2;
    private EditText editTextPaletteName, editTextNumber;
    private RecyclerView recyclerView;
    private Button buttonSave;
    private Spinner spinner;

    public CreatePaletteManuallyFragment() {}

    public static CreatePaletteManuallyFragment newInstance() {
        CreatePaletteManuallyFragment fragment = new CreatePaletteManuallyFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_palette_manually, container, false);
        editTextPaletteName = view.findViewById(R.id.editTextCreatePaletteManuallyName);
        editTextNumber = view.findViewById(R.id.editTextCreatePaletteManuallyNumber);
        recyclerView = view.findViewById(R.id.recyclerViewCreatePaletteManually);
        buttonSave = view.findViewById(R.id.buttonCreatePaletteManuallySave);
        spinner = view.findViewById(R.id.spinnerCreatePaletteManually);
        buttonSave.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonCreatePaletteManuallySave) {

        }
    }
}