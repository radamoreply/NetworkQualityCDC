package com.example.android.networkqualitycdc.connectionSpeedClasses;

import android.util.Log;
import android.view.View;

import com.example.android.networkqualitycdc.customsampler.DeviceBandwidthSamplerCustom;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FacebookNetworkConnectionClass {

    private static final String TAG = "DOWNLOAD_SPEED_CHECK";
    private int mTries = 0;

    public void facebookNetworkConnection(final ConnectionQuality mConnectionClass, final ConnectionClassManager mConnectionClassManager, final DeviceBandwidthSamplerCustom mDeviceBandwidthSampler) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("IMAGE_URL_HERE")
                .build();

        mDeviceBandwidthSampler.startSampling();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                mDeviceBandwidthSampler.stopSampling();
                // Retry for up to 10 times until we find a ConnectionClass.
                if (mConnectionClass == ConnectionQuality.UNKNOWN && mTries < 10) {
                    mTries++;
                    //TODO-implementare check network quality
//                    checkNetworkQuality();
                }
                //TODO-qui è possibile inserire una barra di caricamento
//                if (!mDeviceBandwidthSampler.isSampling()) {
//                    mRunningBar.setVisibility(View.GONE);
//                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    Log.d(TAG, responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                Log.d(TAG, response.body().string());
                Log.d(TAG, mConnectionClassManager.getCurrentBandwidthQuality().toString());

                mDeviceBandwidthSampler.stopSampling();
            }
        });
    }

}
