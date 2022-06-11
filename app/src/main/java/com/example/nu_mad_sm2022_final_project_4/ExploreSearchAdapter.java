package com.example.nu_mad_sm2022_final_project_4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExploreSearchAdapter extends RecyclerView.Adapter<ExploreSearchAdapter.ViewHolder> {
    private ArrayList<String> searchResults;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewPaletteName;
        private final Button buttonAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewPaletteName = itemView.findViewById(R.id.textViewExploreSearchRowName);
            buttonAdd = itemView.findViewById(R.id.buttonExploreSearchRowAdd);
        }

        public TextView getTextViewPaletteName() {
            return textViewPaletteName;
        }

        public Button getButtonAdd() {
            return buttonAdd;
        }
    }

    public ExploreSearchAdapter(ArrayList<String> searchResults) {
        this.searchResults = searchResults;
    }

    @NonNull
    @Override
    public ExploreSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.explore_search_color_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreSearchAdapter.ViewHolder holder, int position) {
        holder.getButtonAdd().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }
}
