package com.example.nu_mad_sm2022_final_project_4;

public interface IAddFragment {
    void addCameraFragment();
    void addDisplayPhotoGalleryFragment();
    void addCreatePaletteManuallyFragment(boolean editPalette, ColorPalette palette);
    void addExploreSearchResultFragment(ColorPalette palette, boolean isExplore);
    void addCreatePaletteOptionsFragment();
    void addCreatePaletteFromImageSeeMorePalettesFragment();
}
