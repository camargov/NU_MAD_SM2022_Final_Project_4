package com.example.nu_mad_sm2022_final_project_4;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraProvider;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.impl.PreviewConfig;
import androidx.camera.core.impl.UseCaseConfig;
import androidx.camera.core.impl.UseCaseConfig.Builder;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.lifecycle.LifecycleOwner;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nu_mad_sm2022_final_project_4.databinding.ActivityMainBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment {

    private ActivityMainBinding viewBinding;

    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;
    private ImageView take_picture;
    private ImageView switch_camera;
    private CameraSelector cameraSelector;
    private boolean cameraBack;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    private View previewView;
    private Preview preview;
    private PreviewView viewFinder;

    private static final int CAMERA_REQUEST_CODE = 100;


    public CameraFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * //@param param1 Parameter 1.
     * //@param param2 Parameter 2.
     * @return A new instance of fragment CameraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {


        }
    }

    private boolean allPermissionsGranted(){
        return (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        getActivity().setTitle("Add Palette");

        switch_camera = view.findViewById(R.id.imageView_switchCamera);
        switch_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cameraBack){
                    cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
                } else {
                    cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                }
                cameraBack = !cameraBack;
                startCamera();

            }
        });




        //Handles preview
        viewFinder = view.findViewById(R.id.viewFinder);
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        startCamera();

        //Take a picture
        take_picture = view.findViewById(R.id.imageView_takePhoto);
        cameraExecutor = Executors.newSingleThreadExecutor();
        take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allPermissionsGranted()){
                    try {
                        ImageCapture.OutputFileOptions outputFileOptions =
                                new ImageCapture.OutputFileOptions.Builder(createImageFile()).build();
                        imageCapture.takePicture(outputFileOptions, cameraExecutor,
                                new ImageCapture.OnImageSavedCallback() {
                                    @Override
                                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                                        Uri imageUri = outputFileResults.getSavedUri();
                                        if(getActivity() instanceof Utils.IPhotoPicked){
                                            Utils.IPhotoPicked photoPickInterface = (Utils.IPhotoPicked) getActivity();
                                            photoPickInterface.photoPicked(imageUri);
                                        }
                                    }
                                    @Override
                                    public void onError(ImageCaptureException error) {
                                        error.printStackTrace();
                                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Unable to capture a photo.", Toast.LENGTH_SHORT).show());
                                    }
                                }
                        );
                    } catch (IOException e) {
                        Log.d("CameraFragment", "Patches does not have the required permissions for camera functionality.");
                    }
                }

            }
        });

        return view;
    }




    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview);
    }

    private void startCamera(){
        if(allPermissionsGranted()) {
            cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
            cameraProviderFuture.addListener(() -> {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    cameraProvider.unbindAll();

                    Preview preview = new Preview.Builder().build();

                    imageCapture = new ImageCapture.Builder()
//                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                            .build();

                    preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

                    cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageCapture);


                } catch (ExecutionException | InterruptedException e) {
                    // Should never be reached
                    Toast.makeText(getContext(), "Camera Provider not found. Something went very wrong.", Toast.LENGTH_SHORT).show();
                }
            }, ContextCompat.getMainExecutor(getContext()));
        }
    }

    //interface for sending signal to activity
    public interface iToGalleryHelper {
        public void showGallery();
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = String.format("JPEG_%s_", timestamp);
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }

}