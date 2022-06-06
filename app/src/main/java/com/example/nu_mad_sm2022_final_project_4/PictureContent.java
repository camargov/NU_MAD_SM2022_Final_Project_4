package com.example.nu_mad_sm2022_final_project_4;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PictureContent {
    static final List<Uri> PICTURES = new ArrayList<>();

    public static void loadImage(File file) {
        Uri pic_uri = Uri.fromFile(file);


        PICTURES.add(0,pic_uri);
    }

    public static void loadSavedImages(File dir){
        PICTURES.clear();
        if(dir.exists()){
            File[] files = dir.listFiles();
            for (int i=0;i<files.length;i++){
                String path = files[i].getAbsolutePath();
                String extension = path.substring(path.lastIndexOf("."));
                if(extension.equals(".jpg")||extension.equals(".png")){
                    loadImage(files[i]);
                }

            }
        }
    }
}
