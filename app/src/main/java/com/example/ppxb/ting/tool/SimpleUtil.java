package com.example.ppxb.ting.tool;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.ppxb.ting.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by ppxb on 2016/4/11.
 */
public class SimpleUtil {

    /**
     * 全屏
     */
    public static void FullScreen(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    /**
     * 启动页面的加载动画
     */
    public static void logoAnimation(final Activity activity, final View view, int duration) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim = AnimationUtils.loadAnimation(activity, R.anim.logo_fade_in);
                view.setVisibility(View.VISIBLE);
                view.startAnimation(anim);
            }
        }, duration);
    }

    /**
     * 沉浸状态栏
     */
    public static void closeToTop(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    /**
     * 压缩bitmap
     */
    public static Bitmap small(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(0.3f, 0.3f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    public static void regReciver(BroadcastReceiver receiver, Context context, String action) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(action);
        context.registerReceiver(receiver, intentFilter);
    }

    public static Bitmap getAlbumImage(Context context, Bitmap getImg) {
        if (getImg == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.empty_music);
            return SimpleUtil.small(bitmap);
        } else
            return SimpleUtil.small(getImg);
    }

    public static byte[] sendBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bitmapByte = baos.toByteArray();
        return bitmapByte;
    }

    public static Bitmap getBitmap(Intent intent, String code) {
        byte[] bis = intent.getByteArrayExtra(code);
        Bitmap img = BitmapFactory.decodeByteArray(bis, 0, bis.length);
        return img;
    }
}
