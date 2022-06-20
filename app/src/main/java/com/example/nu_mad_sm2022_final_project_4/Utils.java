package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Utils {
    private static JsonArray localPaletteDataCache;
    private static Gson gsonCache;

    public static final String PALETTES_FILE_NAME = "patches_palettes.json";

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
    
    public static void storePaletteLocally(Context context, ColorPalette palette) throws IOException {
        JsonArray paletteData = loadPaletteData(context);
        JsonElement paletteElement = getGson().toJsonTree(palette, ColorPalette.class);
        paletteData.add(paletteElement);
        syncPaletteData(context, paletteData);
    }

    public static List<ColorPalette> readPalettesLocally(Context context) throws IOException {
        JsonArray paletteData = loadPaletteData(context);
        return Arrays.asList(getGson().fromJson(paletteData, ColorPalette[].class));
    }

    public static boolean paletteNameAvailable(Context context, String name) throws IOException {
        List<ColorPalette> palettes = readPalettesLocally(context);
        for(ColorPalette palette : palettes) {
            if (name.equals(palette.getName())) {
                return false;
            }
        }
        return true;
    }

    private static JsonArray loadPaletteData(Context context) throws IOException {
        if (localPaletteDataCache != null) {
            return localPaletteDataCache;
        }
        try {
            FileInputStream fis = context.openFileInput(PALETTES_FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            return getGson().fromJson(sb.toString(), JsonArray.class);
        } catch(FileNotFoundException err) {
            try (FileOutputStream fos = context.openFileOutput(PALETTES_FILE_NAME, Context.MODE_PRIVATE)) {
                JsonArray data = new JsonArray();
                fos.write(localPaletteDataCache.getAsString().getBytes());
                return data;
            }
        }
    }

    private static void syncPaletteData(Context context, JsonArray data) throws IOException {
        localPaletteDataCache = data;
        try (FileOutputStream fos = context.openFileOutput(PALETTES_FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(localPaletteDataCache.getAsString().getBytes());
        }
    }

    public static Gson getGson() {
        if (gsonCache != null) {
            return gsonCache;
        }
        return (gsonCache = new Gson());
    }
}
