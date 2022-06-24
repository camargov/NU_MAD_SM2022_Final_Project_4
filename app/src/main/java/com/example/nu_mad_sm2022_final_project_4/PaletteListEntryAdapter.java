package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PaletteListEntryAdapter extends ArrayAdapter<ColorPalette> {

    private final boolean userOwnedPalette;
    private final IAddFragment addFragment;

    public PaletteListEntryAdapter(@NonNull Context context, @NonNull List<ColorPalette> palettes, boolean userOwnedPalette, IAddFragment addFragment) {
        super(context, 0, palettes.toArray(new ColorPalette[0]));
        this.userOwnedPalette = userOwnedPalette;
        this.addFragment = addFragment;
    }

    public PaletteListEntryAdapter(@NonNull Context context, @NonNull List<ColorPalette> palettes) {
        this(context, palettes, false, null);
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
        colorList.removeAllViews();
        Button edit = convertView.findViewById(R.id.paletteList_button_edit);

        name.setText(palette.getName());
        edit.setVisibility(userOwnedPalette ? View.VISIBLE : View.INVISIBLE);
        if (userOwnedPalette) {
            edit.setOnClickListener(view -> addFragment.addEditPaletteFragment(palette));
        }

        PaletteColorsViewAdapter adapter = new PaletteColorsViewAdapter(this.getContext(), palette.getColors());
        for(int i = 0; i < adapter.getCount(); i++) {
            colorList.addView(adapter.getView(i, null, colorList));
        }

        return convertView;
    }
}
