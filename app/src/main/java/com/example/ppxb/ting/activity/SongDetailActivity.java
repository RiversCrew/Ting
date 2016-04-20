package com.example.ppxb.ting.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ppxb.ting.R;
import com.example.ppxb.ting.tool.FastBlur;
import com.example.ppxb.ting.tool.ServiceUtil;
import com.example.ppxb.ting.tool.SimpleUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class SongDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mSongDetailBack;
    private ImageView mSongBg;
    private CircleImageView mAlbum;
    private TextView mDeskTitle, mDeskArtist;
    private ImageView mDeskPre, mDeskPlay, mDeskNext;
    private Animation operatingAnim;
    private LinearInterpolator lin;
    private int isPause;
    private String title, artist;
    private Bitmap img, blur;
    private deskReciver deskReciver;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mSongBg.setImageBitmap(blur);
                mAlbum.setImageBitmap(img);
                mDeskTitle.setText(title);
                mDeskArtist.setText(artist);
            }
            if (msg.what == 1)
                mDeskPlay.setImageResource(R.mipmap.play_btn_pause);
            if (msg.what == 2)
                mDeskPlay.setImageResource(R.mipmap.play_btn_play);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleUtil.closeToTop(this);
        setContentView(R.layout.activity_song_detail);
        initView();
        initData();
    }

    public void initView() {
        deskReciver = new deskReciver();
        SimpleUtil.regReciver(deskReciver, this, "playing");
        mSongDetailBack = (ImageView) findViewById(R.id.song_detail_back);
        mSongBg = (ImageView) findViewById(R.id.backgroud);
        mAlbum = (CircleImageView) findViewById(R.id.album_img);
        mDeskTitle = (TextView) findViewById(R.id.play_desk_title);
        mDeskArtist = (TextView) findViewById(R.id.play_desk_artist);
        mDeskNext = (ImageView) findViewById(R.id.desk_btn_next);
        mDeskPlay = (ImageView) findViewById(R.id.desk_btn_play);
        mDeskPre = (ImageView) findViewById(R.id.desk_btn_prev);
    }

    public void initData() {
        mSongDetailBack.setOnClickListener(this);
        mDeskNext.setOnClickListener(this);
        mDeskPlay.setOnClickListener(this);
        mDeskPre.setOnClickListener(this);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.album_rotate);
        lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        mAlbum.startAnimation(operatingAnim);
        mSongBg.setImageBitmap(MainActivity.blur);
        mAlbum.setImageBitmap(MainActivity.album);
        mDeskTitle.setText(MainActivity.title);
        mDeskArtist.setText(MainActivity.artist);
        Log.e("tag", MainActivity.isPause+"");
        if (MainActivity.isPause == 2) {
            isPause = 2;
            handler.sendEmptyMessage(1);
        } else {
            isPause = 1;
            handler.sendEmptyMessage(2);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.song_detail_back:
                finish();
                break;
            case R.id.desk_btn_next:
                if (isPause == 1)
                    handler.sendEmptyMessage(1);
                if (isPause == 2)
                    handler.sendEmptyMessage(1);
                ServiceUtil.startBroadcastNext(SongDetailActivity.this);
                break;
            case R.id.desk_btn_prev:
                if (isPause == 1)
                    handler.sendEmptyMessage(1);
                if (isPause == 2)
                    handler.sendEmptyMessage(1);
                ServiceUtil.startBroadcastPre(SongDetailActivity.this);
                break;
            case R.id.desk_btn_play:
                if (isPause == 1) {
                    mDeskPlay.setImageResource(R.mipmap.play_btn_play);
                    ServiceUtil.startBrodcastControl(SongDetailActivity.this, isPause);
                    isPause = 2;
                } else {
                    mDeskPlay.setImageResource(R.mipmap.play_btn_pause);
                    ServiceUtil.startBrodcastControl(SongDetailActivity.this, isPause);
                    isPause = 1;
                }
                break;
        }
    }

    class deskReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            title = intent.getStringExtra("title");
            artist = intent.getStringExtra("artist");
            byte[] bis = intent.getByteArrayExtra("img");
            img = BitmapFactory.decodeByteArray(bis, 0, bis.length);
            doBlur(img);
        }
    }

    public void doBlur(final Bitmap original) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap temp = null;
                Bitmap result = null;
                try {
                    if (original == null) {
                        temp = BitmapFactory.decodeResource(getResources(), R.mipmap.empty_music);
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
                handler.sendEmptyMessage(0);
            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(deskReciver);
    }
}
