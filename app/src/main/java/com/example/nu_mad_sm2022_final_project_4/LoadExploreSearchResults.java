package com.example.nu_mad_sm2022_final_project_4;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoadExploreSearchResults implements Runnable {
    public final static String TAG = "demo";
    public final static int STATUS_SUCCESS = 0x001;
    public final static int STATUS_FAILURE = 0x002;
    public final static String TOAST_KEY = "TOAST_KEY";
    public final static String PALETTE_ARRAY = "PALETTE_ARRAY";
    private final OkHttpClient client = new OkHttpClient();
    private String keyword;
    private Handler messageQueue;

    public LoadExploreSearchResults(String keyword, Handler messageQueue) {
        this.keyword = keyword;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        Bundle bundle = new Bundle();
        // Building the URL
        HttpUrl url = HttpUrl.parse("https://www.colourlovers.com/api/palettes")
                .newBuilder()
                .addQueryParameter("keywords", "search+" + keyword)
                .addQueryParameter("format", "json")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Message failureMessage = new Message();
                failureMessage.what = STATUS_FAILURE;
                bundle.putString(TOAST_KEY, "Failed to complete search. Please try again.");
                failureMessage.setData(bundle);
                messageQueue.sendMessage(failureMessage);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONArray rootJsonArray = new JSONArray(responseBody);
                        ArrayList<String> paletteListStr = new ArrayList<>();

                        // Adding the palettes to the bundle as an ArrayList of String
                        for (int i = 0; i < rootJsonArray.length(); i++) {
                            // Creating a string for each palette and adding the palette's title
                            String paletteStr = "";
                            JSONObject object = rootJsonArray.getJSONObject(i);
                            String paletteTitle = object.getString("title");
                            if (paletteTitle.contains(",")) {
                                int index = object.getString("title").indexOf(",");
                                paletteTitle = paletteTitle.substring(0, index) + paletteTitle.substring(index + 1);
                            }
                            paletteStr += paletteTitle + ",";

                            // Getting the colors of the JSON object
                            JSONArray colorJsonArray = object.getJSONArray("colors");
                            for (int j = 0; j < colorJsonArray.length(); j++) {
                                paletteStr += colorJsonArray.getString(j) + ",";
                            }
                            // Adding the palette represented as a string to the array list
                            paletteListStr.add(paletteStr);
                        }
                        // Putting the array list on the bundle
                        bundle.putStringArrayList(PALETTE_ARRAY, paletteListStr);
                        Message successMessage = new Message();
                        successMessage.what = STATUS_SUCCESS;
                        successMessage.setData(bundle);
                        messageQueue.sendMessage(successMessage);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Message failureMessage = new Message();
                    failureMessage.what = STATUS_FAILURE;
                    bundle.putString(TOAST_KEY, "Failed to complete search. Please try again.");
                    failureMessage.setData(bundle);
                    messageQueue.sendMessage(failureMessage);
                }
            }
        });
    }
}
