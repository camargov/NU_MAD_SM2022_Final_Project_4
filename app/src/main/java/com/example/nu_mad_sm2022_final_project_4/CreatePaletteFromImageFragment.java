package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

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

    private final String API_AUTHORIZATION = "Basic YWNjX2ZhZDljY2MxZmUzYzk4NDphMGRmM2ExYzk5ODRiMDUwODA1YTNjYTU1NzJlNWM1Nw==";

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

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_palette_from_image, container, false);
        Log.d("CreatePaletteFromImageFragment", "onCreateView");

        // Defining UI Elements:
        editTextPaletteName = view.findViewById(R.id.editTextCreatePaletteFromImageName);
        buttonSeeMorePalettes = view.findViewById(R.id.buttonCreatePaletteFromImageSeeMorePalettes);
        buttonSave = view.findViewById(R.id.buttonCreatePaletteFromImageSave);
        imageView = view.findViewById(R.id.imageViewCreatePaletteFromImage);
        Picasso.get().load(image_uri).into(imageView);
        buttonSeeMorePalettes.setOnClickListener(this);
        buttonSave.setOnClickListener(this);

        // Set up colors array

        try {
            getColorsFromImagga();
        } catch (IOException e) {
            Log.d("CreatePaletteFromImageFragment", "onCreateView: " + e.getMessage());
        }

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

    public void getColorsFromImagga() throws IOException {
        Thread worker = new Thread(new imaggaAPIWorker(image_uri,new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Bundle receivedData = msg.getData();
                switch(msg.what){
                    case imaggaAPIWorker.STATUS_UNABLE_TO_EXECUTE:
                        Log.d("CreatePaletteFromImageFragment", "getColorsFromImage: handleMessage: immagaAPIWorker Unable to execute.");
                        String error = receivedData.getString("error");
                        Log.d("CreatePaletteFromImageFragment", "getColorsFromImage: handleMessage error message: " + error);
                        break;
                    case imaggaAPIWorker.STATUS_SUCCESS_COLORS_RETRIEVED:
                        String[] colors = receivedData.getStringArray(imaggaAPIWorker.COLOR_ARRAY_KEY);
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int i=0;i<colors.length;i++){
                            stringBuilder.append(colors[i]).append("\n");
                        }
                        Log.d("CreatePaletteFromImageFragment", "color array:\n" + stringBuilder.toString());
                }
                return false;
            }
        })),"imagga worker using request");
        worker.start();
    }

    public void getColorsFromImage(){
        Log.d("CreatePaletteFromImageFragment", "getColorsFromImage: ");
        Thread worker = new Thread(new imaggaAPIWorker(image_uri, new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Bundle receivedData = msg.getData();
                switch(msg.what){
                    case imaggaAPIWorker.STATUS_REQUEST:
                        String request_msg = receivedData.getString("request");
                        Log.d("CreatePaletteFromImageFragment", "imaggaAPIWorker - handleMessage: " + request_msg);
                        break;
                    case imaggaAPIWorker.STATUS_UNABLE_TO_EXECUTE:
                        Log.d("CreatePaletteFromImageFragment", "getColorsFromImage: handleMessage: immagaAPIWorker Unable to execute.");
                        String error = receivedData.getString("error");
                        Log.d("CreatePaletteFromImageFragment", "getColorsFromImage: handleMessage error message: " + error);
                        Toast.makeText(getActivity(), "We were unable process your image. Please wait and try again.", Toast.LENGTH_SHORT).show();
                        break;
                    case imaggaAPIWorker.STATUS_UNSUCESSFUL:
                        Log.d("CreatePaletteFromImageFragment", "getColorsFromImage: handleMessage: immagaAPIWorker Unsucessful.");

                        Toast.makeText(getActivity(), "We were unable to connect to the API, please ensure you have a stable internet connection.", Toast.LENGTH_SHORT).show();
                        break;
                    case imaggaAPIWorker.STATUS_SUCCESS_COLORS_RETRIEVED:
                        Log.d("CreatePaletteFromImageFragment", "getColorsFromImage: handleMessage: immagaAPIWorker Success.");
                        String upload_id = receivedData.getString("upload_id");
                        Log.d("CreatePaletteFromImageFragment", "handleMessage - upload_id:" + upload_id);
                        String[] html_colors = receivedData.getStringArray(imaggaAPIWorker.COLOR_ARRAY_KEY);
                        StringBuilder colorsAsString = new StringBuilder();
                        for(String color: html_colors){
                            colorsAsString.append(color);
                            colorsAsString.append("\n");
                        }
                        Log.d("CreatePaletteFromImageFragment", "getColorsFromImage: handleMessage: " + colorsAsString.toString() );
                        convertStringArrToInt(html_colors);
                        break;

                }
                return false;
            }
        })),"imagga worker");
        worker.start();
    }

    private void convertStringArrToInt(String[] arr){
        colors.clear();
        for (int i = 0; i < arr.length; i++) {
            colors.add(Integer.parseInt(arr[i].substring(1), 16) + 0xFF000000);
        }

    }
}