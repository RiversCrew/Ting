package com.example.ppxb.ting.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.ppxb.ting.R;

/**
 * Created by ppxb on 2016/4/25.
 */
public class BlurImage {
    private Context context;
    private Bitmap blurimg;

    public BlurImage(Context context) {
        this.context = context;
    }

    public void doBlur(final Bitmap original) {
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
                blurimg = bitmap;
            }
        }.execute();
    }

    public Bitmap getBlurimg() {
        return blurimg;
    }
}
