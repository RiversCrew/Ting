package com.example.ppxb.ting.tool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by ppxb on 2016/4/14.
 */
public class ServiceUtil {

    /**
     * 开启传递参数到activity的广播
     */
    public static void startServiceWithActivity(Context context, Bitmap img, String title, String artist) {
        Intent intent = new Intent();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bitmapByte = baos.toByteArray();
        intent.putExtra("title", title);
        intent.putExtra("artist", artist);
        intent.putExtra("img", bitmapByte);
        intent.setAction("play");
        context.sendBroadcast(intent);
    }
}
