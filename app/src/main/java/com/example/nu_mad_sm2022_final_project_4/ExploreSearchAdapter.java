package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExploreSearchAdapter extends RecyclerView.Adapter<ExploreSearchAdapter.ViewHolder> {
    private ArrayList<ColorPalette> searchResults;
    private IAddFragment fragmentListener;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewPaletteName;
        private final LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPaletteName = itemView.findViewById(R.id.paletteList_textView_name);
            linearLayout = itemView.findViewById(R.id.linearLayoutExploreSearchColorRow);
        }

        public TextView getTextViewPaletteName() {
            return textViewPaletteName;
        }

        public LinearLayout getLinearLayout() {
            return linearLayout;
        }
    }

    public ExploreSearchAdapter(ArrayList<ColorPalette> searchResults, Context context) {
        this.searchResults = searchResults;
        this.fragmentListener = (IAddFragment) context;
        this.context = context;
    }

    @NonNull
    @Override
    public ExploreSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_palette, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreSearchAdapter.ViewHolder holder, int position) {
        holder.getTextViewPaletteName().setText(searchResults.get(position).GetName());

        PaletteColorsViewAdapter adapter =
                new PaletteColorsViewAdapter(context, searchResults.get(position).GetColors());
        for(int i = 0; i < adapter.getCount(); i++) {
            holder.getLinearLayout().addView(adapter.getView(i, null, holder.getLinearLayout()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentListener.addExploreSearchResultFragment(searchResults.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }
}
