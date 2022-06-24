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
        holder.getConstraintLayoutColorContainer().setBackgroundColor(colors.get(position));

        // Setting the hex and rgb values
        String hexColor = Integer.toHexString(colors.get(position) - 0xFF000000);
        while(hexColor.length()!=6){
            hexColor = "0"+hexColor;
        }
        holder.getTextViewHex().setText("#" + hexColor.toUpperCase());
        holder.getTextViewRGB().setText(convertHexToRGB(hexColor));
    }

    private String convertHexToRGB(String colorStr) {
        return "RGB: ("
                + Integer.valueOf(colorStr.substring(0, 2), 16) + ","
                + Integer.valueOf(colorStr.substring(2, 4), 16) + ","
                + Integer.valueOf(colorStr.substring(4, 6), 16) + ")";
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }
}
