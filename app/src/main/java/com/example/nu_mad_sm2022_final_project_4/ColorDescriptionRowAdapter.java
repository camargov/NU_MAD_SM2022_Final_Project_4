package com.example.nu_mad_sm2022_final_project_4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ColorDescriptionRowAdapter extends RecyclerView.Adapter<ColorDescriptionRowAdapter.ViewHolder> {
    private ArrayList<Integer> colors;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout constraintLayoutColorContainer;
        private final TextView textViewHex, textViewRGB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayoutColorContainer = itemView.findViewById(R.id.constraintLayoutColorPaletteDescriptionRowColorContainer);
            textViewHex = itemView.findViewById(R.id.textViewColorPaletteDescriptionRowHex);
            textViewRGB = itemView.findViewById(R.id.textViewColorPaletteDescriptionRowRGB);
        }

        public ConstraintLayout getConstraintLayoutColorContainer() {
            return constraintLayoutColorContainer;
        }

        public TextView getTextViewHex() {
            return textViewHex;
        }

        public TextView getTextViewRGB() {
            return textViewRGB;
        }
    }

    public ColorDescriptionRowAdapter(@NonNull ArrayList<Integer> colorPalette) {
        this.colors = colorPalette;
    }

    @NonNull
    @Override
    public ColorDescriptionRowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.color_palette_description_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorDescriptionRowAdapter.ViewHolder holder, int position) {
        // THIS MAY CHANGE BASED ON WHAT TYPE OF INTEGER IS SENT TO THIS ADAPTER - CONVERSION MAY BE NEEDED
        holder.getConstraintLayoutColorContainer().setBackgroundColor(colors.get(position));
        // SET TEXT BASED ON INT
        //holder.getTextViewHex().setText(convertIntToHex(colors.get(position)));
        // SET TEXT BASED ON INT
        //holder.getTextViewRGB().setText(convertIntToRGB(colors.get(position)));

    }

    private String convertIntToHex(Integer num) {
        return "HEX";
    }
    private String convertIntToRGB(Integer num) {
        return "RGB";
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }
}
