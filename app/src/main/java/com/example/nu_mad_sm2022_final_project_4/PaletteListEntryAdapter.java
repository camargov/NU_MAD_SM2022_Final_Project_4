package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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
        ListView colorList = convertView.findViewById(R.id.paletteList_listView_colors);
        colorList.setAdapter(new PaletteColorsViewAdapter(this.getContext(), palette.GetColors()));

        name.setText(palette.GetName());

        return convertView;
    }
}
