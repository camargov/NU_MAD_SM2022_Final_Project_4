package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreatePaletteFromImageFragment extends Fragment implements View.OnClickListener {

    private Uri image_uri;
    private IToastFromFragmentToMain toastListener;
    private IAddFragment fragmentListener;

    private final OkHttpClient client = new OkHttpClient();

    // UI Elements:
    private EditText editTextPaletteName;
    private Button buttonSeeMorePalettes, buttonSave;
    private ImageView imageView;

    // Setting up recyclerView
    private RecyclerView recyclerViewProminentColors;
    private ColorDescriptionRowAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ArrayList<Integer> colors = new ArrayList<>();

    public final String API_COLOR_URL = "https://api.imagga.com/v2/colors";

    private final String API_AUTHORIZATION = "Basic YWNjX2ZhZDljY2MxZmUzYzk4NDphMGRmM2ExYzk5ODRiMDUwODA1YTNjYTU1NzJlNWM1Nw==";

    private static final String ARG_CLOUD_FILE_PATH = "cloudFilePath";
    private String cloudFilepath;

    private StorageReference upload_Path;

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
            this.cloudFilepath = getArguments().getString(ARG_CLOUD_FILE_PATH);
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

        getColorsFromImageJson(
                ()->{
                    Toast.makeText(getContext(), "Error with request.", Toast.LENGTH_SHORT).show();
                },
                () -> {
                    Toast.makeText(getContext(), "Request was not Successful.", Toast.LENGTH_SHORT).show();
                }
        );

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


    public void uploadImage(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference root = storage.getReference();
        StorageReference uploadPath = root.child(cloudFilepath);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image_uri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            uploadPath.putBytes(data)
                    .addOnCompleteListener(task -> getActivity().runOnUiThread(() -> {
                        getActivity().onBackPressed();
                        upload_Path = root.child(getPhotoPath());
                    }));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String getPhotoPath() {
        return String.format("photos/%d.jpg", System.currentTimeMillis());
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

    public void getColorsFromImageJson(Runnable onFail,Runnable onError){
        File fileToUpload = new File(image_uri.toString());
        HttpUrl url = HttpUrl.parse(API_COLOR_URL);
        RequestBody body = new FormBody.Builder()
                .add("image","=@/"+image_uri.toString())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization",API_AUTHORIZATION)
                .post(body)
                .build();
        this.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(onFail);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String bodyString = response.body().string();

                    Gson gson = new Gson();
                    JsonObject result = gson.fromJson(bodyString,JsonObject.class).get("result").getAsJsonObject();
                    JsonArray colors = result.get("colors").getAsJsonObject().get("background_colors").getAsJsonArray();
                    List<String>html_colors = new ArrayList<>();
                    for(int i=0;i<colors.size();i++){
                        JsonObject color = colors.get(i).getAsJsonObject();
                        String html_code = color.get("html_code").getAsString();
                        html_colors.add(html_code);
                    }
                    String[] html_colors_arr = (String[])html_colors.toArray();
                    getActivity().runOnUiThread(convertStringArrToInt(html_colors_arr));
                } else {
                    System.out.println("response body String: " + response.body().string());
                    Log.d("getColorsFromImageJson", "onResponse: " + response.body().string());
                    getActivity().runOnUiThread(()->{
                        try {
                            Log.d("getColorsFromImageJson", "onResponse: " + response.body().string());
                        } catch (IOException e) {
                            Log.d("getColorsFromImageJson", "onResponse: error - " + e.getMessage());
                        }
                    });
                    getActivity().runOnUiThread(onError);
                }
            }
        });

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

    private Runnable convertStringArrToInt(String[] arr){
        colors.clear();
        for (int i = 0; i < arr.length; i++) {
            colors.add(Integer.parseInt(arr[i].substring(1), 16) + 0xFF000000);
        }

        return null;
    }
}