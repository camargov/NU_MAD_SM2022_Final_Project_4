package com.example.nu_mad_sm2022_final_project_4;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link displayPhotoGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class displayPhotoGalleryFragment extends Fragment implements PictureItemFragment.OnDisplayImageFragmentListener {

    public static final int COLs = 4;

    private PictureItemFragment.OnDisplayImageFragmentListener listener;

    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    public displayPhotoGalleryFragment() {
        // Required empty public constructor
    }

    public static displayPhotoGalleryFragment newInstance() {
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
        View view = inflater.inflate(R.layout.fragment_display_photo_gallery, container, false);
        if(recyclerViewAdapter == null){
            recyclerView = (RecyclerView) view;
        }

        return view;
    }

    @Override
    public void onDisplayImageInteraction(Uri uri) {
        //TODO: this is where you handle selecting a certain item in list i.e. send it to the Palette Creation Fragment...oof
    }
}