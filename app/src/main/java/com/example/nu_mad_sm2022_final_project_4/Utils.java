package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
                palettes.add(paletteFromData(d.getData()));
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
    
    public static boolean storePaletteLocally(Context context, ColorPalette palette) throws IOException {
        if (!paletteNameAvailable(context, palette.getName())) {
            // Palette name is taken :(
            return false;
        }
        List<ColorPalette> paletteData = readPalettesLocally(context);
        paletteData.add(palette);
        overwritePaletteData(context, paletteData);
        return true;
    }

    public static boolean replacePaletteLocally(Context context, ColorPalette oldPalette, ColorPalette palette) throws IOException {
        if (paletteNameAvailable(context, oldPalette.getName())) {
            // Palette doesn't exist
            return false;
        }
        if (!paletteNameAvailable(context, palette.getName()) && !oldPalette.getName().equals(palette.getName())) {
            // New palette name is bad
            return false;
        }

        List<ColorPalette> paletteData = readPalettesLocally(context);
        for(int i = 0; i < paletteData.size(); i++) {
            if (paletteData.get(i).getName().equals(oldPalette.getName())) {
                paletteData.remove(i);
                break;
            }
        }

        List<ColorPalette> newData = new ArrayList<>();
        newData.add(palette);
        newData.addAll(paletteData);

        overwritePaletteData(context, newData);

        return true;
    }

    public static List<ColorPalette> readPalettesLocally(Context context) throws IOException {
        String userId = getCurrentUserId();
        return paletteListFromJson(loadPaletteData(context)).stream().filter(palette -> palette.getUserId().equals(userId)).collect(Collectors.toList());
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
            // If something is going wrong with this part of the code, then
            // the local data is probably corrupt.
            //
            // Uncomment this line to reset everything:

            // context.deleteFile(PALETTES_FILE_NAME);
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
                fos.write(getGson().toJson(data).getBytes());
                return data;
            }
        }
    }

    private static void syncPaletteData(Context context, JsonArray data) throws IOException {
        localPaletteDataCache = data;
        try (FileOutputStream fos = context.openFileOutput(PALETTES_FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(getGson().toJson(localPaletteDataCache).getBytes());
        }
    }

    public static Gson getGson() {
        if (gsonCache != null) {
            return gsonCache;
        }
        return (gsonCache = new Gson());
    }

    public static void syncLocalPaletteDataToCloud(Context context, Runnable onSuccess, Runnable onFail) throws IOException {
        syncLocalPaletteDataToCloud(context, onSuccess, onFail, false);
    }

    // All palettes that are stored on the cloud should be stored locally,
    // and all palettes that are stored locally and marked as "cloudPalette"
    // should be on the cloud.
    public static void syncLocalPaletteDataToCloud(Context context, Runnable onSuccess, Runnable onFail, boolean forceCloudResync) throws IOException {
        final List<ColorPalette> localPalettes = readPalettesLocally(context);
        localPalettes.sort(Comparator.comparing(ColorPalette::getName));
        final List<ColorPalette> markedCloudPalettes = localPalettes.stream().filter(ColorPalette::getCloudPalette).collect(Collectors.toList());
        String userId = getCurrentUserId();
        Function<List<ColorPalette>, Void> palettesReceived = cloudPalettes -> {
            boolean updateCloud = false;
            boolean updateLocal = false;
            List<ColorPalette> localCopy = new ArrayList<>(localPalettes);
            List<ColorPalette> markedCopy = new ArrayList<>(markedCloudPalettes);
            cloudPalettes.sort(Comparator.comparing(ColorPalette::getName));
            if (checkForUnmarkedData(cloudPalettes)) {
                updateCloud = true;
                cloudPalettes = cloudPalettes.stream().filter(ColorPalette::getCloudPalette).collect(Collectors.toList());
            }
            if (!forceCloudResync && !checkSynced(localCopy, cloudPalettes)) {
                updateLocal = true;
                localCopy = combineData(cloudPalettes, localCopy);
                markedCopy = localCopy.stream().filter(ColorPalette::getCloudPalette).collect(Collectors.toList());
            }
            if (!checkSynced(cloudPalettes, markedCopy)) {
                updateCloud =  true;
                cloudPalettes = combineData(markedCopy, cloudPalettes);
            }

            if (updateLocal) {
                try {
                    overwritePaletteData(context, localCopy);
                } catch(IOException e) {
                    e.printStackTrace();
                    onFail.run();
                    return null;
                }
            }
            if (updateCloud || forceCloudResync) {
                overwriteCloudData(cloudPalettes, onSuccess, onFail);
                return null;
            }

            onSuccess.run();
            return null;
        };

        getUserPalettes(userId, palettesReceived, onFail);
    }

    // Are all the palettes in the latter list contained within the former list?
    private static boolean checkSynced(List<ColorPalette> palettesA, List<ColorPalette> palettesB) {
        int l = 0;
        int c = 0;
        while(l < palettesA.size() && c < palettesB.size()) {
            ColorPalette localPalette = palettesA.get(l);
            ColorPalette cloudPalette = palettesB.get(c);
            if (cloudPalette.equals(localPalette)) {
                c++;
            }
            l++;
        }
        return c >= palettesB.size();
    }

    private static boolean checkForUnmarkedData(List<ColorPalette> cloudPalettes) {
        for(ColorPalette palette : cloudPalettes) {
            if (!palette.getCloudPalette()) {
                return true;
            }
        }
        return false;
    }

    // Sync the source to its destination
    // If there are any name collisions, the source palette will
    // replace the destination palette.
    private static List<ColorPalette> combineData(List<ColorPalette> source, List<ColorPalette> destination) {
        List<ColorPalette> resultSet = new ArrayList<>(source);
        for(ColorPalette palette : destination) {
            boolean collisionFound = false;
            for(ColorPalette other : resultSet) {
                if (other.getName().equals(palette.getName())) {
                    collisionFound = true;
                    break;
                }
            }
            if (!collisionFound) {
                resultSet.add(palette);
            }
        }
        resultSet.sort(Comparator.comparing(ColorPalette::getName));
        return resultSet;
    }

    // Given palettes are palettes for CURRENT USER
    // Sync this with other local palettes that could be from other users!
    private static void overwritePaletteData(Context context, List<ColorPalette> palettes) throws IOException {
        String userId = getCurrentUserId();
        List<ColorPalette> otherPalettes = paletteListFromJson(loadPaletteData(context)).stream().filter(palette -> !palette.getUserId().equals(userId)).collect(Collectors.toList());
        otherPalettes.addAll(palettes);
        JsonArray newData = (JsonArray) getGson().toJsonTree(otherPalettes.toArray(), ColorPalette[].class);
        syncPaletteData(context, newData);
    }

    private static void overwriteCloudData(List<ColorPalette> palettes, Runnable onSuccess, Runnable onFail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("palettes")
                .whereEqualTo("userId", getCurrentUserId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentReference> refs = new ArrayList<>();
                    List<ColorPalette> oldPalettes = new ArrayList<>();
                    for(DocumentSnapshot document : queryDocumentSnapshots) {
                        refs.add(document.getReference());
                        oldPalettes.add(paletteFromData(document.getData()));
                    }
                    db.runTransaction((Transaction.Function<Void>) transaction -> {
                        for(int i = 0; i < refs.size(); i++) {
                            DocumentReference oldRef = refs.get(i);
                            ColorPalette oldPalette = oldPalettes.get(i);
                            if (listContainsPalette(palettes, oldPalette)) {
                                transaction.set(oldRef, getMatchingPalette(palettes, oldPalette));
                            } else {
                                transaction.delete(oldRef);
                            }
                        }
                        for(ColorPalette palette : palettes) {
                            if (!listContainsPalette(oldPalettes, palette)) {
                                palette.setUserId(getCurrentUserId());
                                transaction.set(db.collection("palettes").document(), palette);
                            }
                        }
                        return null;
                    })
                            .addOnSuccessListener(unused -> onSuccess.run())
                            .addOnFailureListener(e -> {
                                e.printStackTrace();
                                onFail.run();
                            });
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    onFail.run();
                });
    }

                        // TODO: Make this return the actual user ID once we have user authentication set up.
    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static String getCurrentUserId() {
        FirebaseUser user = getCurrentUser();
        return user == null ? null : user.getUid();
    }

    private static List<ColorPalette> paletteListFromJson(JsonArray paletteData) {
        return Arrays.asList(getGson().fromJson(paletteData, ColorPalette[].class));
    }

    private static ColorPalette paletteFromData(Map<String, Object> data) {
        return new ColorPalette(
                (String)data.get("name"),
                (String)data.get("userId"),
                data.containsKey("cloudPalette") ? (Boolean)data.get("cloudPalette") : false,
                (List<Integer>)data.get("colors")
        );
    }

    private static Map<String, Object> dataFromPalette(ColorPalette palette) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", palette.getName());
        data.put("userId", palette.getUserId());
        data.put("cloudPalette", palette.getCloudPalette());
        data.put("color", palette.getColors());
        return data;
    }

    private static boolean listContainsPalette(List<ColorPalette> list, ColorPalette palette) {
        for(ColorPalette other : list) {
            if (palette.getName().equals(other.getName())) {
                return true;
            }
        }
        return false;
    }

    private static ColorPalette getMatchingPalette(List<ColorPalette> list, ColorPalette palette) {
        for(ColorPalette other : list) {
            if (palette.getName().equals(other.getName())) {
                return other;
            }
        }
        return null;
    }
}
