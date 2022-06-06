package com.example.nu_mad_sm2022_final_project_4;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link displayPhotoGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class displayPhotoGalleryFragment extends Fragment {

    public static final int COLs = 4;

    public displayPhotoGalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment displayPhotoGalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static displayPhotoGalleryFragment newInstance(String param1, String param2) {
        displayPhotoGalleryFragment fragment = new displayPhotoGalleryFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_photo_gallery, container, false);
    }
}