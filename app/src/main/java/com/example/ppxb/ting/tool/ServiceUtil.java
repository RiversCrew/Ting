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
     * 开启播放音乐的服务
     */
    public static void startServiceWithUrl(Context context, Class<?> cls, String url) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("isPlay", true);
        intent.putExtra("url", url);
        context.startService(intent);
    }

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
        intent.setAction("playing");
        context.sendBroadcast(intent);
    }

    /**
     * minibar控制songservice的广播
     */

    public static void startBrodcastControl(Context context, int isPasue) {
        Intent intent = new Intent();
        intent.putExtra("isPause", isPasue);
        intent.setAction("pause");
        context.sendBroadcast(intent);
    }

    public static void startServicePause(Context context, Class<?> cls, int isPause) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("isPause", isPause);
        context.startService(intent);
    }

    public static void startBroadcastNext(Context context) {
        Intent intent = new Intent();
        intent.setAction("next");
        context.sendBroadcast(intent);
    }

    public static void startBroadcastPre(Context context) {
        Intent intent = new Intent();
        intent.setAction("pre");
        context.sendBroadcast(intent);
    }
    public static void startBroadcastNewSong(Context context) {
        Intent intent = new Intent();
        intent.setAction("newsong");
        context.sendBroadcast(intent);
    }

}
