package com.example.nu_mad_sm2022_final_project_4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final String FAVORITE_FRAGMENT = "FAVORITE_FRAGMENT";
    final String CAMERA_FRAGMENT = "CAMERA_FRAGMENT";
    final String EXPLORE_FRAGMENT = "EXPLORE_FRAGMENT";
    private ImageView imageViewFavorite, imageViewCamera, imageViewExplore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentConstraintLayout, FavoriteFragment.newInstance(), FAVORITE_FRAGMENT)
                .commit();

        imageViewFavorite = findViewById(R.id.imageViewFavorite);
        imageViewCamera = findViewById(R.id.imageViewCamera);
        imageViewExplore = findViewById(R.id.imageViewExplore);
        imageViewFavorite.setOnClickListener(this);
        imageViewCamera.setOnClickListener(this);
        imageViewExplore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageViewFavorite) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentConstraintLayout, FavoriteFragment.newInstance(), FAVORITE_FRAGMENT)
                    .commit();
        }
        else if (v.getId() == R.id.imageViewCamera) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentConstraintLayout, CameraFragment.newInstance(), CAMERA_FRAGMENT)
                    .commit();

        }
        else if (v.getId() == R.id.imageViewExplore) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentConstraintLayout, ExploreFragment.newInstance(), EXPLORE_FRAGMENT)
                    .commit();
        }
    }
}