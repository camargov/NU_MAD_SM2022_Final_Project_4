package com.example.nu_mad_sm2022_final_project_4;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.FormBody;
import okhttp3.HttpUrl;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

//////////
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class imaggaAPIWorker implements Runnable{

    private final OkHttpClient client;
    private Handler messageQueue;

    private Uri image_uri;
    public final String API_ENDPOINT = "https://api.imagga.com";
    public final String API_COLORS = "/v2/colors";
    public final String API_COLOR_URL = API_ENDPOINT + API_COLORS;

    //TODO: Yes I know this isn't supposed to be here, gotta protect privacy but for now it is here.
    private final String API_KEY = "acc_fad9ccc1fe3c984";
    private final String API_SECRET = "a0df3a1c9984b050805a3ca5572e5c57";
    private final String API_AUTHORIZATION = "Basic YWNjX2ZhZDljY2MxZmUzYzk4NDphMGRmM2ExYzk5ODRiMDUwODA1YTNjYTU1NzJlNWM1Nw==";
    private final String user = API_KEY + ":" + API_SECRET;

    public static final int STATUS_UNABLE_TO_EXECUTE = 0x404;
    public static final int STATUS_UNSUCESSFUL = 0x202;
    public static final int STATUS_SUCCESS_COLORS_RETRIEVED = 0x005;
    public static final int STATUS_REQUEST = 0x001;
    public static final String COLOR_ARRAY_KEY = "html_colors";

    public imaggaAPIWorker (Uri image, Handler handler){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
        this.messageQueue = handler;
        this.image_uri = image;
    }

//    public void run (){
//
//
//        //actual request
//        HttpUrl color_url = HttpUrl.parse(API_COLOR_URL);
//        RequestBody register_body = new FormBody.Builder()
//                .add("image",image_uri.toString())
//                .build();
//        Request register_request  = new Request.Builder()
//                .url(color_url)
//                .addHeader("Authorization",API_AUTHORIZATION)
//                .post(register_body)
//                .build();
//        Message request_msg = new Message();
//        request_msg.what = STATUS_REQUEST ;
//        Bundle request_bundle = new Bundle();
//        request_bundle.putString("request",register_request.toString());
//        request_msg.setData(request_bundle);
//
//        try(Response response = client.newCall(register_request).enqueue()){
//            if(response.isSuccessful()){
//                Gson gson = new Gson();
//                String responseMessage = response.body().string();
//                JsonObject result = gson.fromJson(responseMessage,JsonObject.class).get("result").getAsJsonObject();
//                JsonObject colors_key = result.get("colors").getAsJsonObject();
//                JsonArray colors = result.getAsJsonArray("background_colors");
//                List<String> html_colors = new ArrayList<>();
//                for(int i=0;i<colors.size();i++){
//                    JsonObject curr_color =  colors.get(i).getAsJsonObject();
//                    html_colors.add(curr_color.get("html_code").toString());
//                }
//                String[] array_colors = (String[]) html_colors.toArray();
//                Message message = new Message();
//                Bundle bundle = new Bundle();
//                bundle.putStringArray(COLOR_ARRAY_KEY,array_colors);
//                message.setData(bundle);
//                messageQueue.sendMessage(message);
//
//
//            } else {
//                Message msg_unsucessful  = new Message();
//                msg_unsucessful.what = STATUS_UNSUCESSFUL;
//                messageQueue.sendMessage(msg_unsucessful);
//            }
//
//        } catch (IOException e){
//            Message error_msg = new Message();
//            error_msg.what = STATUS_UNABLE_TO_EXECUTE;
//            Bundle errorBundle = new Bundle();
//            errorBundle.putString("error",e.getMessage());
//            messageQueue.sendMessage(error_msg);
//
//        }
//    }

    public void run(){
        try {

            File fileToUpload = new File(image_uri.toString());

            String endpoint = "/uploads";

            String crlf = "\r\n";
            String twoHyphens = "--";
            String boundary =  "Image Upload";

            URL urlObject = new URL("https://api.imagga.com/v2" + endpoint);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestProperty("Authorization", "Basic " + API_AUTHORIZATION);
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream request = new DataOutputStream(connection.getOutputStream());

            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + fileToUpload.getName() + "\"" + crlf);
            request.writeBytes(crlf);
            request.close();
            InputStream responseStream = new BufferedInputStream(connection.getInputStream());
            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            Gson gson = new Gson();
            JsonObject upload_result = gson.fromJson(stringBuilder.toString(),JsonObject.class).get("result").getAsJsonObject();
            String upload_id = upload_result.get("upload_id").getAsString();
//            InputStream inputStream = new FileInputStream(fileToUpload);
//            int bytesRead;
//            byte[] dataBuffer = new byte[1024];
//            while ((bytesRead = inputStream.read(dataBuffer)) != -1) {
//                request.write(dataBuffer, 0, bytesRead);
//            }
//
//            request.writeBytes(crlf);
//            request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
//            request.flush();
//            request.close();
//
//            InputStream responseStream = new BufferedInputStream(connection.getInputStream());
//
//            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
//
//            String line = "";
//            StringBuilder stringBuilder = new StringBuilder();
//
//            String upload_id = "";
//
//            while ((line = responseStreamReader.readLine()) != null) {
//                if(line.contains("upload_id")){
//                    upload_id = line.split(":")[1];
//                }
//                stringBuilder.append(line).append("\n");
//            }
//            responseStreamReader.close();;
//            responseStream.close();
            connection.disconnect();

            //////////////////////////
            String endpoint_url = "https://api.imagga.com/v2/colors";
            String image_url = "https://imagga.com/static/images/tagging/wind-farm-538576_640.jpg";

            String url = endpoint_url + "?image_upload_id=" + upload_id;
            URL urlObject_2 = new URL(url);
            HttpURLConnection connection_too = (HttpURLConnection) urlObject_2.openConnection();

            connection_too.setRequestProperty("Authorization", "Basic " + API_AUTHORIZATION);

            int responseCode = connection_too.getResponseCode();

            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            JsonObject result = gson.fromJson(connection_too.getResponseMessage(),JsonObject.class).get("result").getAsJsonObject();
            JsonArray colors = result.get("colors").getAsJsonObject().get("background_colors").getAsJsonArray();
            List<String>html_colors = new ArrayList<>();
            for(int i=0;i<colors.size();i++){
                JsonObject color = colors.get(i).getAsJsonObject();
                String html_code = color.get("html_code").getAsString();
                html_colors.add(html_code);
            }

            connection_too.disconnect();
            ////////////////

            Message msg = new Message();
            msg.what = STATUS_SUCCESS_COLORS_RETRIEVED;
            Bundle bundle = new Bundle();
            bundle.putStringArray(COLOR_ARRAY_KEY, (String[]) html_colors.toArray());
            bundle.putString("upload_id",upload_id);
            msg.setData(bundle);
            messageQueue.sendMessage(msg);

        } catch(IOException e){
            Message error_msg = new Message();
            error_msg.what = STATUS_UNABLE_TO_EXECUTE;
            Bundle errorBundle = new Bundle();
            errorBundle.putString("error",e.getMessage());
            messageQueue.sendMessage(error_msg);
        }
    }
}
