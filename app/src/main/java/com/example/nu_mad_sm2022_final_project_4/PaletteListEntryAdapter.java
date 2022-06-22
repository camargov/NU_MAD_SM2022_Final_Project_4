package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PaletteListEntryAdapter extends ArrayAdapter<ColorPalette> {

    public PaletteListEntryAdapter(@NonNull Context context, @NonNull List<ColorPalette> palettes) {
        super(context, 0, palettes.toArray(new ColorPalette[0]));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ColorPalette palette = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_palette, parent, false);
        }

        TextView name = convertView.findViewById(R.id.paletteList_textView_name);
        LinearLayout colorList = convertView.findViewById(R.id.paletteList_linearLayout_palettes);

        name.setText(palette.getName());

        PaletteColorsViewAdapter adapter = new PaletteColorsViewAdapter(this.getContext(), palette.getColors());
        for(int i = 0; i < adapter.getCount(); i++) {
            colorList.addView(adapter.getView(i, null, colorList));
        }

        return convertView;
    }
}
