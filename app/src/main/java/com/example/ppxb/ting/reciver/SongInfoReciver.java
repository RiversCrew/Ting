package com.example.ppxb.ting.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;

import com.example.ppxb.ting.R;
import com.example.ppxb.ting.tool.FastBlur;

/**
 * Created by ppxb on 2016/4/25.
 */
public class SongInfoReciver extends BroadcastReceiver {
    private Bitmap img;
    private String title;
    private String artist;
    private Handler handler;
    private Bitmap blur;
    private Context context;

    public SongInfoReciver(Context context, Handler handler) {
        this.handler = handler;
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        title = intent.getStringExtra("title");
        artist = intent.getStringExtra("artist");
        byte[] bis = intent.getByteArrayExtra("img");
        img = BitmapFactory.decodeByteArray(bis, 0, bis.length);
        doBlur(img, context);
        handler.sendEmptyMessage(0);
    }

    public void doBlur(final Bitmap original, final Context context) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap temp = null;
                Bitmap result = null;
                try {
                    if (original == null) {
                        temp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.empty_music);
                        result = FastBlur.doBlur(temp, 10, false);
                        return result;
                    }
                    result = FastBlur.doBlur(original, 10, false);
                } catch (Error e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                blur = bitmap;
            }
        }.execute();
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

    public Bitmap getBlur() {
        return blur;
    }
}
