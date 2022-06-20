package com.example.nu_mad_sm2022_final_project_4;

import android.net.Uri;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Utils {
    private Utils() { }

    public interface IPhotoPicked {
        void photoPicked(Uri photoUri);
    }

    public static void getUserPalettes(String userId, Function<List<ColorPalette>, Void> onSuccess, Runnable onFail) {
        searchPalettes(userId, "", -1, onSuccess, onFail);
    }

    public static void getPaletteByName(String userId, String name, Function<ColorPalette, Void> onSuccess, Runnable onFail) {
        Function<List<ColorPalette>, Void> palettesReceived = palettes -> {
            if (palettes.size() > 0) {
                onSuccess.apply(palettes.get(0));
            } else {
                onSuccess.apply(null);
            }
            return null;
        };
        searchPalettes(userId, name, -1, palettesReceived, onFail);
    }

    private static void searchPalettes(String userId, String name, long limit, Function<List<ColorPalette>, Void> onSuccess, Runnable onFail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collection = db.collection("palettes");
        Query query = collection.whereNotEqualTo("name", "");
        if (!userId.equals("")) {
            query = query.whereEqualTo("userId", userId);
        }
        if (!name.equals("")) {
            query = query.whereEqualTo("name", name);
        }
        if (limit > 0) {
            query = query.limit(limit);
        }
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<ColorPalette> palettes = new ArrayList<>();
            for(DocumentSnapshot d : queryDocumentSnapshots) {
                Map<String, Object> paletteData = d.getData();
                ColorPalette palette = new ColorPalette(
                        (String)paletteData.get("name"),
                        (String)paletteData.get("userId"),
                        (List<Integer>)paletteData.get("colors")
                );
                palettes.add(palette);
            }
            onSuccess.apply(palettes);
        }).addOnFailureListener(e -> {
            e.printStackTrace();
            onFail.run();
        });
    }

    public static void uploadPalette(ColorPalette palette, Runnable onSuccess, Runnable onFail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("palettes")
                .add(palette)
                .addOnSuccessListener(documentReference -> onSuccess.run())
                .addOnFailureListener(e -> onFail.run());
    }
}
