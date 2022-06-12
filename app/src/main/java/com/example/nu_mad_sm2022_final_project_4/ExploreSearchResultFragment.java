package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExploreSearchResultFragment extends Fragment implements View.OnClickListener, IResponseToBigPaletteColorClickAction {
    private static final String ARG_COLOR_PALETTE = "ARG_COLOR_PALETTE";
    private ColorPalette colorPalette;
    private IBigPaletteColorClickAction paletteColorClickActionListener;

    // UI Elements
    private TextView textViewPaletteName, textViewHex, textViewRGB, textViewCMYK;
    private Button buttonSavePalette;
    private LinearLayout linearLayout;
    private BigPaletteColorsAdapter adapter;
    private ConstraintLayout constraintLayoutSelectedColor;

    // Firestore-related items
    //private db;
    //private mAuth;
    //private FirebaseUser mUser;



    public ExploreSearchResultFragment() {}

    public static ExploreSearchResultFragment newInstance(ColorPalette colorPalette) {
        ExploreSearchResultFragment fragment = new ExploreSearchResultFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_COLOR_PALETTE, colorPalette);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            colorPalette = (ColorPalette) getArguments().getSerializable(ARG_COLOR_PALETTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_search_result, container, false);
        getActivity().setTitle("Explore");

        // Defining UI Elements
        textViewPaletteName = view.findViewById(R.id.textViewExploreSearchResultPaletteName);
        textViewPaletteName.setText(colorPalette.GetName());
        // convert colors to hex, rgb, cmyk
        textViewHex = view.findViewById(R.id.textViewExploreSearchResultHex);
        textViewRGB = view.findViewById(R.id.textViewExploreSearchResultRGB);
        textViewCMYK = view.findViewById(R.id.textViewExploreSearchResultCMYK);
        buttonSavePalette = view.findViewById(R.id.buttonExploreSearchResultSavePalette);
        buttonSavePalette.setOnClickListener(this);
        constraintLayoutSelectedColor = view.findViewById(R.id.constraintLayoutExploreSearchResultSelectedColor);

        // Setting up linearLayout
        linearLayout = view.findViewById(R.id.linearLayoutExploreSearchResult);
        adapter = new BigPaletteColorsAdapter(this.getContext(), colorPalette.GetColors());
        for(int i = 0; i < adapter.getCount(); i++) {
            linearLayout.addView(adapter.getView(i, null, linearLayout));
        }
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof IBigPaletteColorClickAction) {
            paletteColorClickActionListener = (IBigPaletteColorClickAction) context;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonExploreSearchResultSavePalette) {
            // add palette to user's palette collection
            /*
            db.collection("users")
            .document(mUser.getEmail())
            .collection("palettes")
            .document()
            .set(colorPalette);
             */
        }
    }

    @Override
    public void bigPaletteColorClickResponse() {
        Integer num = paletteColorClickActionListener.getExploreSearchResultColorInformation();
        constraintLayoutSelectedColor.setBackgroundColor(num);
        Log.d("demo", "bigPaletteColorClickResponse: " + num);
    }

    /*
    public class RgbToCmyk {
        public static void main(String args[]) {
            double red = 50;
            double green = 60;
            double blue = 90;
            double white = 0;

            double cyan = 0;
            double magenta = 0;
            double yellow = 0;
            double black = 0;
            if (red == 0 && green == 0 && blue == 0) {
                black = 1;//from ww w.j a  va2 s.co m

            } else {
                if ((red / 255) > (green / 255) && (red / 255) > (blue / 255)) {
                    white = red / 255;
                } else if ((green / 255) > (blue / 255)) {
                    white = green / 255;
                } else {
                    white = blue / 255;
                }

                cyan = (white - (red / 255)) / white;
                magenta = (white - (green / 255)) / white;
                yellow = (white - (blue / 255)) / white;
                black = (1 - white);

            }
            System.out.println("cyan value is " + cyan);
            System.out.println("magenta value is" + magenta);
            System.out.println("yellow value is" + yellow);
            System.out.println("black value is " + black);
        }
    }*/
}