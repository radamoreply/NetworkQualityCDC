package com.example.android.networkqualitycdc.utils;

import android.content.res.AssetManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import kotlin.text.Charsets;

public class ServiceUtil {
    private ServiceUtil() {
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static <T> T loadJson(String fileName, Class<T> tClass, AssetManager assetManager, Gson gson) {
        T t = null;
        try (
                InputStream inputStream = assetManager.open(fileName);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charsets.UTF_8)
        ) {
            t = gson.fromJson(inputStreamReader, tClass);
        } catch (IOException e) {
            Log.e(ServiceUtil.class.getSimpleName(), "Unable to load file with name" + fileName, e);
        }
        return t;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String load(String fileName, AssetManager assetManager) {
        String s = null;
        try (
                InputStream inputStream = assetManager.open(fileName);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charsets.UTF_8)
        ) {
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String read;
            while ((read = bufferedReader.readLine()) != null) {
                stringBuilder.append(read);
            }
            s = stringBuilder.toString();
        } catch (Exception e) {
            Log.e(ServiceUtil.class.getSimpleName(), "Unable to load file with name" + fileName, e);
        }
        return s;
    }

}
