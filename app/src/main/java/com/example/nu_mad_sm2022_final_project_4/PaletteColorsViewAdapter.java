package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

public class PaletteColorsViewAdapter extends ArrayAdapter<Integer> {

    public PaletteColorsViewAdapter(@NonNull Context context, @NonNull List<Integer> colors) {
        super(context, 0, colors);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Integer color = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_palette_color, parent, false);
        }

        ConstraintLayout constraint = convertView.findViewById(R.id.paletteColors_constraintLayout_colorContainer);
        // TextView text = convertView.findViewById(R.id.paletteColors_textView_colorText);
        constraint.setBackgroundColor(color);
        // text.setBackgroundColor(color);

        return convertView;
    }
}
