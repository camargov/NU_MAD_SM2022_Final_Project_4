package com.example.nu_mad_sm2022_final_project_4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IAddFragment, IToastFromFragmentToMain {
    final String FAVORITE_FRAGMENT = "FAVORITE_FRAGMENT";
    final String CREATE_PALETTE_OPTIONS_FRAGMENT = "CREATE_PALETTE_OPTIONS_FRAGMENT";
    final String CAMERA_FRAGMENT = "CAMERA_FRAGMENT";
    final String DISPLAY_PHOTO_GALLERY_FRAGMENT = "DISPLAY_PHOTO_GALLERY_FRAGMENT";
    final String CREATE_PALETTE_MANUALLY_FRAGMENT = "CREATE_PALETTE_MANUALLY_FRAGMENT";
    final String EXPLORE_FRAGMENT = "EXPLORE_FRAGMENT";
    final String EXPLORE_SEARCH_RESULT_FRAGMENT = "EXPLORE_SEARCH_RESULT_FRAGMENT";
    private ImageView imageViewFavorite, imageViewAddPalette, imageViewExplore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentConstraintLayout, FavoriteFragment.newInstance(), FAVORITE_FRAGMENT)
                .commit();

        imageViewFavorite = findViewById(R.id.imageViewFavorite);
        imageViewAddPalette = findViewById(R.id.imageViewAddPalette);
        imageViewExplore = findViewById(R.id.imageViewExplore);
        imageViewFavorite.setOnClickListener(this);
        imageViewAddPalette.setOnClickListener(this);
        imageViewExplore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageViewFavorite) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentConstraintLayout, FavoriteFragment.newInstance(), FAVORITE_FRAGMENT)
                    .commit();
        }
        else if (v.getId() == R.id.imageViewAddPalette) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentConstraintLayout, CreatePaletteOptionsFragment.newInstance(), CREATE_PALETTE_OPTIONS_FRAGMENT)
                    .commit();

        }
        else if (v.getId() == R.id.imageViewExplore) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentConstraintLayout, ExploreFragment.newInstance(), EXPLORE_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public void toastFromFragment(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addCameraFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentConstraintLayout, CameraFragment.newInstance(), CAMERA_FRAGMENT)
                .commit();
    }

    @Override
    public void addDisplayPhotoGalleryFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentConstraintLayout, displayPhotoGalleryFragment.newInstance(), DISPLAY_PHOTO_GALLERY_FRAGMENT)
                .commit();
    }

    @Override
    public void addCreatePaletteManuallyFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentConstraintLayout, CreatePaletteManuallyFragment.newInstance(), CREATE_PALETTE_MANUALLY_FRAGMENT)
                .commit();
    }

    @Override
    public void addExploreSearchResultFragment(ColorPalette palette) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentConstraintLayout, ExploreSearchResultFragment.newInstance(palette), EXPLORE_SEARCH_RESULT_FRAGMENT)
                .commit();
    }

    @Override
    public void addCreatePaletteOptionsFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentConstraintLayout, CreatePaletteOptionsFragment.newInstance(), CREATE_PALETTE_OPTIONS_FRAGMENT)
                .commit();
    }


}