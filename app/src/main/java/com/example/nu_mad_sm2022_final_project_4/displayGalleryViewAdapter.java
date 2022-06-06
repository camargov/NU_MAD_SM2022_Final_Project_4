package com.example.nu_mad_sm2022_final_project_4;

import android.graphics.Picture;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URI;
import java.util.List;

public class displayGalleryViewAdapter extends RecyclerView.Adapter<displayGalleryViewAdapter.ViewHolder> {
    private final List<Uri> pictures;
    private final PictureItemFragment.OnDisplayImageFragmentListener listener;

    public displayGalleryViewAdapter(List<Uri> pictures,PictureItemFragment.OnDisplayImageFragmentListener listener){
        this.pictures = pictures;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View gView;
        public final ImageView gImageView;
        public URI uri;

        public ViewHolder (View v){
            super(v);
            gView = v;
            gImageView = v.findViewById(R.id.imageView_picture_item);
        }
    }
}
