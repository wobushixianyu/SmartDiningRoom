package com.david.smartdiningroom.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;

public class SdrUtils {

    public static final int HTTP_PAGE_COUNT = 10;

    public static JsonObject readAssets(Context context,String fileName){
        try {
            InputStream inputStream = context.getAssets().open("storeSalesPage1.txt");
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            return new Gson().fromJson(new String(bytes,"utf-8"),JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
