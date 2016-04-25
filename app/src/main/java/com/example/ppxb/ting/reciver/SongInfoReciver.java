package com.example.ppxb.ting.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

/**
 * Created by ppxb on 2016/4/25.
 */
public class SongInfoReciver extends BroadcastReceiver {
    private Bitmap img;
    private String title;
    private String artist;
    private Handler handler;

    public SongInfoReciver(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        title = intent.getStringExtra("title");
        artist = intent.getStringExtra("artist");
        byte[] bis = intent.getByteArrayExtra("img");
        img = BitmapFactory.decodeByteArray(bis, 0, bis.length);
        handler.sendEmptyMessage(0);
    }

    public Bitmap getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }
}
