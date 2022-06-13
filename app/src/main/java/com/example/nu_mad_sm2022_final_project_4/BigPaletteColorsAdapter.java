package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

public class BigPaletteColorsAdapter extends ArrayAdapter<Integer> {
    private IBigPaletteColorClickAction colorClickActionListener;

    public BigPaletteColorsAdapter(@NonNull Context context, @NonNull List<Integer> colors) {
        super(context, 0, colors);
        colorClickActionListener = (IBigPaletteColorClickAction) context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Integer color = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.big_palette_color_view, parent, false);
        }

        ConstraintLayout constraint = convertView.findViewById(R.id.constraintLayoutBigPaletteColorViewContainer);
        constraint.setBackgroundColor(color);
        constraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorClickActionListener.setExploreSearchResultColorInformation(getItem(position));
            }
        });

        return convertView;
    }
}
