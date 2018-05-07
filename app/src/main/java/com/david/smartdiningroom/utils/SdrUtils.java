package com.david.smartdiningroom.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.david.smartdiningroom.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SdrUtils {

    public static final int HTTP_PAGE_COUNT = 10;

    public static JsonObject readAssets(Context context,String fileName){
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            return new Gson().fromJson(new String(bytes,"utf-8"),JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatDate(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date(time));
    }

    public static void showToast(Context mContext,String msg){
        Toast mToast = new Toast(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_toast_layout, null);
        TextView tv_msg = view.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        mToast.setView(view);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }
}
