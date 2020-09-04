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

    private DownloadSpeedCheckEventListener callback;
    private static final String TAG = "DOWNLOAD_SPEED_CHECK";
    long startTime;
    long endTime;
    long fileSize;
    double speed;
    int kilobytePerSec;

    // Speed Standards Used
//    o POOR: larghezza di banda sotto 150 kbps
//    o MODERATE: larghezza di banda compresa tra 120 e 550 kbps
//    o GOOD: larghezza di banda compresa tra 550 e 2000 kbps
//    o EXCELLENT: larghezza di banda sopra i 2000 kbps

    // bandwidth standard limits in kbps
    private int POOR_BANDWIDTH = 120;
    private int AVERAGE_BANDWIDTH = 550;
    private int GOOD_BANDWIDTH = 2000;


    public void downloadSpeedCheck(DownloadSpeedCheckEventListener cb) {
        callback = cb;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://i.pinimg.com/originals/c7/93/35/c79335a0f2c30d5da05de7d0dd356092.jpg")
                .build();

        startTime = System.currentTimeMillis();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onEventFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

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
//                    byte[] docBuffer = bos.toByteArray();
                    fileSize = bos.size();

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
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

                callback.onEventCompleted(downloadSpeedValue(kilobytePerSec * 8));
            }
        });


    }

    private ChooseConnectivityCheckActivity.SPEED downloadSpeedValue(int kilobitPerSec) {
        if (kilobitPerSec == 0) {
            return ChooseConnectivityCheckActivity.SPEED.NOT_AVAILABLE;
        }
        if (kilobitPerSec <= POOR_BANDWIDTH) {
            return ChooseConnectivityCheckActivity.SPEED.POOR;
        } else if (kilobitPerSec <= AVERAGE_BANDWIDTH) {
            return ChooseConnectivityCheckActivity.SPEED.MODERATE;
        } else if (kilobitPerSec <= GOOD_BANDWIDTH) {
            return ChooseConnectivityCheckActivity.SPEED.GOOD;
        } else {
            return ChooseConnectivityCheckActivity.SPEED.EXCELLENT;
        }

    }


}