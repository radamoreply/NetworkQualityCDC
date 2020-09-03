package com.example.android.networkqualitycdc.connectionSpeedClasses;

import android.util.Log;

import com.example.android.networkqualitycdc.myapplication.ChooseConnectivityCheckActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadSpeedCheckJava {

    private static final String TAG = "DOWNLOAD_SPEED_CHECK";
    long startTime;
    long endTime;
    long fileSize;
    double speed;
    int kilobytePerSec;

    // bandwidth in kbps
    private int POOR_BANDWIDTH = 150;
    private int AVERAGE_BANDWIDTH = 550;
    private int GOOD_BANDWIDTH = 2000;

//TODO- rendere la chiamata bloccante, perchè va subito al return
    public ChooseConnectivityCheckActivity.SPEED downloadSpeedCheck() {


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://helpx.adobe.com/content/dam/help/en/stock/how-to/visual-reverse-image-search/jcr_content/main-pars/image/visual-reverse-image-search-v2_intro.jpg")
                .build();

        startTime = System.currentTimeMillis();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    Log.d(TAG, responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                InputStream input = response.body().byteStream();

                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];

                    while (input.read(buffer) != -1) {
                        bos.write(buffer);
                    }
                    byte[] docBuffer = bos.toByteArray();
                    fileSize = bos.size();

                } finally {
                    input.close();
                }

                endTime = System.currentTimeMillis();


                // calculate how long it took by subtracting endtime from starttime

                double timeTakenMills = Math.floor(endTime - startTime);  // time taken in milliseconds
                double timeTakenSecs = timeTakenMills / 1000;  // divide by 1000 to get time in seconds
                kilobytePerSec = (int) Math.round(1024 / timeTakenSecs);

                if (kilobytePerSec <= POOR_BANDWIDTH) {
                    // slow connection
                }

                // get the download speed by dividing the file size by time taken to download
                speed = fileSize / timeTakenMills;

                Log.d(TAG, "Time taken in secs: " + timeTakenSecs);
                Log.d(TAG, "kilobyte per sec: " + kilobytePerSec);
                Log.d(TAG, "Download Speed: " + speed);
                Log.d(TAG, "File size: " + fileSize);


            }
        });

        if (kilobytePerSec <= POOR_BANDWIDTH) {
            return ChooseConnectivityCheckActivity.SPEED.POOR;
        }else {
            return ChooseConnectivityCheckActivity.SPEED.EXCELLENT;
        }

            }

}
