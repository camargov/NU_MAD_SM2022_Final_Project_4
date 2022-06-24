package com.example.nu_mad_sm2022_final_project_4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IAddFragment,
        IToastFromFragmentToMain, Utils.IPhotoPicked {
    final String FAVORITE_FRAGMENT = "FAVORITE_FRAGMENT";
    final String CREATE_PALETTE_OPTIONS_FRAGMENT = "CREATE_PALETTE_OPTIONS_FRAGMENT";
    final String CAMERA_FRAGMENT = "CAMERA_FRAGMENT";
    final String DISPLAY_PHOTO_GALLERY_FRAGMENT = "DISPLAY_PHOTO_GALLERY_FRAGMENT";
    final String CREATE_PALETTE_MANUALLY_FRAGMENT = "CREATE_PALETTE_MANUALLY_FRAGMENT";
    final String CREATE_PALETTE_FROM_IMAGE_SEE_MORE_PALETTES_FRAGMENT = "CREATE_PALETTE_FROM_IMAGE_SEE_MORE_PALETTES_FRAGMENT";
    final String CREATE_PALETTE_FROM_IMAGE_FRAGMENT = "CREATE_PALETTE_FROM_IMAGE_FRAGMENT";
    final String EXPLORE_FRAGMENT = "EXPLORE_FRAGMENT";
    final String EXPLORE_SEARCH_RESULT_FRAGMENT = "EXPLORE_SEARCH_RESULT_FRAGMENT";
    final String LOG_IN_FRAGMENT = "LOG_IN_FRAGMENT";
    final String REGISTER_FRAGMENT = "REGISTER_FRAGMENT";
    final String ONBOARDING_FRAGMENT = "ONBOARDING_FRAGMENT";
    final String EDIT_PALETTE_FRAGMENT = "EDIT_PALETTE_FRAGMENT";

    private ImageView imageViewFavorite, imageViewAddPalette, imageViewExplore;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewFavorite = findViewById(R.id.imageViewFavorite);
        imageViewAddPalette = findViewById(R.id.imageViewAddPalette);
        imageViewExplore = findViewById(R.id.imageViewExplore);
        logout = findViewById(R.id.button_logout);
        imageViewFavorite.setOnClickListener(this);
        imageViewAddPalette.setOnClickListener(this);
        imageViewExplore.setOnClickListener(this);
        logout.setOnClickListener(this);
        setNavVisibility(false);

        if (Utils.getCurrentUser() != null) {
            Runnable onSyncComplete = () -> {
                setNavVisibility(true);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentConstraintLayout, FavoriteFragment.newInstance(), FAVORITE_FRAGMENT)
                        .commit();
            };
            try {
                Utils.syncLocalPaletteDataToCloud(this, onSyncComplete, onSyncComplete);
            } catch (IOException e) {
                onSyncComplete.run();
            }
        } else {
            this.addOnboardingFragment();
        }
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
        } else if (v.getId() == R.id.button_logout) {
            FirebaseAuth.getInstance().signOut();
            addOnboardingFragment();
        }
    }

    public void photoPicked(Uri photoUri){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentConstraintLayout, CreatePaletteFromImageFragment.newInstance(photoUri), CREATE_PALETTE_FROM_IMAGE_FRAGMENT)
                .commit();
    }

    @Override
    public void toastFromFragment(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addCameraFragment() {
        this.setNavVisibility(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentConstraintLayout, CameraFragment.newInstance(), CAMERA_FRAGMENT)
                .commit();
    }

    @Override
    public void addDisplayPhotoGalleryFragment() {
        this.setNavVisibility(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentConstraintLayout, displayPhotoGalleryFragment.newInstance(), DISPLAY_PHOTO_GALLERY_FRAGMENT)
                .commit();
    }

    @Override
    public void addCreatePaletteManuallyFragment() {
        this.setNavVisibility(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentConstraintLayout, CreatePaletteManuallyFragment.newInstance(), CREATE_PALETTE_MANUALLY_FRAGMENT)
                .commit();
    }

    @Override
    public void addExploreSearchResultFragment(ColorPalette palette) {
        this.setNavVisibility(true);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentConstraintLayout, ExploreSearchResultFragment.newInstance(palette), EXPLORE_SEARCH_RESULT_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void addCreatePaletteOptionsFragment() {
        this.setNavVisibility(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentConstraintLayout, CreatePaletteOptionsFragment.newInstance(), CREATE_PALETTE_OPTIONS_FRAGMENT)
                .commit();
    }

    @Override
    public void addLogInFragment() {
        this.setNavVisibility(false);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentConstraintLayout, LogInFragment.newInstance(), LOG_IN_FRAGMENT)
                .commit();
    }

    @Override
    public void addRegisterFragment() {
        this.setNavVisibility(false);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentConstraintLayout, RegisterFragment.newInstance(), REGISTER_FRAGMENT)
                .commit();
    }

    @Override
    public void addOnboardingFragment() {
        this.setNavVisibility(false);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentConstraintLayout, RegisterLogInFragment.newInstance(), ONBOARDING_FRAGMENT)
                .commit();
    }

    @Override
    public void addEditPaletteFragment(ColorPalette palette) {
        this.setNavVisibility(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentConstraintLayout, EditPaletteFragment.newInstance(palette), EDIT_PALETTE_FRAGMENT)
                .commit();
    }

    private void setNavVisibility(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.INVISIBLE;
        imageViewFavorite.setVisibility(visibility);
        imageViewAddPalette.setVisibility(visibility);
        imageViewExplore.setVisibility(visibility);
        logout.setVisibility(visibility);
    }
}