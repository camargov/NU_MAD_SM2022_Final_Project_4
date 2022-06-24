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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

    private final OkHttpClient client = new OkHttpClient();

    // UI Elements:
    private EditText editTextPaletteName;
    private Button buttonSave;
    private ImageView imageView;
    private ProgressBar loading_asset;

    // Setting up recyclerView
    private RecyclerView recyclerViewProminentColors;
    private ColorDescriptionRowAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ArrayList<Integer> colors = new ArrayList<>();

    public final String API_COLOR_URL = "https://api.imagga.com/v2/colors";

    private final String API_AUTHORIZATION = "Basic YWNjX2ZhZDljY2MxZmUzYzk4NDphMGRmM2ExYzk5ODRiMDUwODA1YTNjYTU1NzJlNWM1Nw==";

    private static final String ARG_IMAGE_URI = "imageUri";
    private String cloudFilepath;

    private StorageReference upload_Path;

    public CreatePaletteFromImageFragment(Uri image_uri) {
        this.image_uri = image_uri;
    }

    public static CreatePaletteFromImageFragment newInstance(Uri image_uri) {
        CreatePaletteFromImageFragment fragment = new CreatePaletteFromImageFragment(image_uri);
        Bundle args = new Bundle();
        args.putParcelable(ARG_IMAGE_URI, image_uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.image_uri = getArguments().getParcelable(ARG_IMAGE_URI);
        }
        this.cloudFilepath = getPhotoPath();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_palette_from_image, container, false);
        Log.d("CreatePaletteFromImageFragment", "onCreateView");

        // Defining UI Elements:
        editTextPaletteName = view.findViewById(R.id.editTextCreatePaletteFromImageName);
        buttonSave = view.findViewById(R.id.buttonCreatePaletteFromImageSave);
        imageView = view.findViewById(R.id.imageViewCreatePaletteFromImage);
        Picasso.get().load(image_uri).into(imageView);
        buttonSave.setOnClickListener(this);
        loading_asset = view.findViewById(R.id.progressBarCreatePaletteFromImage);
        toggleLoading(true);

        // Set up colors array

        uploadImage(uri -> {
                getColorsFromImageJson(uri,
                        () -> Toast.makeText(getContext(), "Error with request.", Toast.LENGTH_SHORT).show(),
                        () -> Toast.makeText(getContext(), "Request was not Successful.", Toast.LENGTH_SHORT).show()
                );
                return null;
        },
                () -> Toast.makeText(getContext(), "Request was not Successful.", Toast.LENGTH_SHORT).show());


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
    }


    public void uploadImage(Function<Uri, Void> useDownloadUrl, Runnable onFail){
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
                        uploadPath.getDownloadUrl()
                                .addOnSuccessListener(useDownloadUrl::apply)
                                .addOnFailureListener(e -> {
                                    e.printStackTrace();
                                    onFail.run();
                                });
                    }));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            onFail.run();
        } catch (IOException e) {
            e.printStackTrace();
            onFail.run();
        }
    }
    private String getPhotoPath() {
        return String.format("photos/%d.jpg", System.currentTimeMillis());
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonCreatePaletteFromImageSave) {
            if (editTextPaletteName.getText().toString().equals("")) {
                toastListener.toastFromFragment("Palette must have a name.");
            }
            else {
                String name = editTextPaletteName.getText().toString();
                ColorPalette colorPalette = new ColorPalette(name,colors);
                colorPalette.setUserId(Utils.getCurrentUserId());
                colorPalette.setCloudPalette(true);
                try {
                    Utils.storePaletteLocally(getActivity(), colorPalette);
                } catch(IOException e) {
                    Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();
                }
                Utils.uploadPalette(colorPalette,
                        () -> getActivity().runOnUiThread(() -> savedPaletteSuccessfully()),
                        () -> getActivity().runOnUiThread(() -> toastListener.toastFromFragment("Something went wrong; adding palette locally, try restarting later to re-sync public data with cloud")));
            }
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

    public void getColorsFromImageJson(Uri downloadUri, Runnable onFail, Runnable onError){
        File fileToUpload = new File(image_uri.toString());
        HttpUrl url = HttpUrl.parse(API_COLOR_URL)
                .newBuilder()
                .addQueryParameter("image_url", downloadUri.toString())
                .build();
//        RequestBody body = new FormBody.Builder()
//                .add("image_url",downloadUri.toString())
//                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization",API_AUTHORIZATION)
                .get()
                .build();
        this.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                toggleLoading(false);
                e.printStackTrace();
                getActivity().runOnUiThread(onFail);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                toggleLoading(false);
                if(response.isSuccessful()){
                    String bodyString = response.body().string();

                    Gson gson = new Gson();
                    JsonObject result = gson.fromJson(bodyString,JsonObject.class).get("result").getAsJsonObject();
                    JsonArray colors = result.get("colors").getAsJsonObject().get("image_colors").getAsJsonArray();
                    List<String>html_colors = new ArrayList<>();
                    for(int i=0;i<colors.size();i++){
                        JsonObject color = colors.get(i).getAsJsonObject();
                        String html_code = color.get("html_code").getAsString();

                        html_colors.add(html_code);
                    }
                    String[] html_colors_arr = html_colors.toArray(new String[0]);
                    getActivity().runOnUiThread(convertStringArrToInt(html_colors_arr));
                } else {
                    Log.d("getColorsFromImageJson", "onResponse: " + response.body().string());
//                    getActivity().runOnUiThread(()->{
//                        try {
//                            Log.d("getColorsFromImageJson", "onResponse: " + response.body().string());
//                        } catch (IOException e) {
//                            Log.d("getColorsFromImageJson", "onResponse: error - " + e.getMessage());
//                        }
//                    });
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

    private Runnable convertStringArrToInt(String[] arr){
        colors.clear();
        for (int i = 0; i < arr.length; i++) {
            colors.add(Integer.parseInt(arr[i].substring(1), 16) + 0xFF000000);
            getActivity().runOnUiThread(()->recyclerViewAdapter.notifyDataSetChanged());
        }

        return null;
    }

    private void toggleLoading(Boolean show){
        if(show){
            this.loading_asset.setVisibility(View.VISIBLE);
        } else {
            this.loading_asset.setVisibility(View.INVISIBLE);
        }
    }

    private void savedPaletteSuccessfully() {
        toastListener.toastFromFragment("Palette saved successfully!");
    }
}