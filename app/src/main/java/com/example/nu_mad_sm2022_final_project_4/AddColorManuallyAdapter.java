package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AddColorManuallyAdapter extends RecyclerView.Adapter<AddColorManuallyAdapter.ViewHolder> {
    private ArrayList<String> colors;
    private CreatePaletteManuallyFragment fragmentListener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewColorHex;
        private final ImageView imageViewEdit, imageViewTrash;
        private final ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewColorHex = itemView.findViewById(R.id.textViewCreatePaletteManuallyColorHex);
            imageViewEdit = itemView.findViewById(R.id.imageViewCreatePaletteManuallyEdit);
            imageViewTrash = itemView.findViewById(R.id.imageViewCreatePaletteManuallyTrash);
            constraintLayout = itemView.findViewById(R.id.constraintLayoutCreatePaletteManuallyAddColor);
        }

        public TextView getTextViewColorHex() {
            return textViewColorHex;
        }

        public ImageView getImageViewEdit() {
            return imageViewEdit;
        }

        public ImageView getImageViewTrash() {
            return imageViewTrash;
        }

        public ConstraintLayout getConstraintLayout() {
            return constraintLayout;
        }

        public void toggleClickability(boolean clickable){
            textViewColorHex.setClickable(clickable);
            imageViewEdit.setClickable(clickable);
            imageViewTrash.setClickable(clickable);
        }
    }


    public AddColorManuallyAdapter(ArrayList<String> colors, CreatePaletteManuallyFragment fragmentListener) {
        this.colors = colors;
        this.fragmentListener = fragmentListener;
    }

    @NonNull
    @Override
    public AddColorManuallyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.create_palette_manually_add_color, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddColorManuallyAdapter.ViewHolder holder, int position) {
        holder.getTextViewColorHex().setText(colors.get(position));
        holder.getConstraintLayout().setBackgroundColor(Color.parseColor(colors.get(position)));

        // Changing the color of the text and backgrounds of image based on the color of the row
        if (ColorUtils.calculateLuminance(Color.parseColor(colors.get(position))) < 0.5) {
            holder.getTextViewColorHex().setTextColor(Color.WHITE);
            holder.getImageViewEdit().setBackgroundColor(Color.WHITE);
            holder.getImageViewTrash().setBackgroundColor(Color.WHITE);
        }
        else {
            holder.getTextViewColorHex().setTextColor(Color.BLACK);
            holder.getImageViewEdit().setBackgroundColor(Color.TRANSPARENT);
            holder.getImageViewTrash().setBackgroundColor(Color.TRANSPARENT);
        }

        holder.getImageViewEdit().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentListener.editButtonAction(holder.getAdapterPosition());
            }
        });
        holder.getImageViewTrash().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colors.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }
}
