package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URI;

/**
 * Representing a single picture.
 */
public class PictureItemFragment extends Fragment {

    public OnDisplayImageFragmentListener listener;


    public PictureItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PictureItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PictureItemFragment newInstance(String param1, String param2) {
        PictureItemFragment fragment = new PictureItemFragment();
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
        View view = inflater.inflate(R.layout.fragment_picture_item, container, false);

        if (view instanceof RecyclerView){
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context,displayPhotoGalleryFragment.COLs));
            //RecyclerView.setAdapter(new displayGalleryViewAdapter(PictureContent.PICTURES,listener));
        }

        return view;
    }

    public interface OnDisplayImageFragmentListener {
        void onDisplayImageInteraction(Uri uri);
    }
}