package com.example.ppxb.ting.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ppxb.ting.R;
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
    private String title, artist;
    private Bitmap blur, album;
    private int isplay;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mSongBg.setImageBitmap(blur);
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
        isplay = getIntent().getIntExtra("isplay", 0);
        if (isplay == 1) {
            mDeskPlay.setImageResource(R.mipmap.play_btn_play);
        } else {
            mDeskPlay.setImageResource(R.mipmap.play_btn_pause);
        }
        title = getIntent().getStringExtra("title");
        artist = getIntent().getStringExtra("artist");
        blur = SimpleUtil.getBitmap(getIntent(), "blur");
        album = SimpleUtil.getBitmap(getIntent(), "img");
        mSongBg.setImageBitmap(blur);
        mAlbum.setImageBitmap(album);
        mDeskTitle.setText(title);
        mDeskArtist.setText(artist);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.song_detail_back:
                onBackPressed();
                break;
            case R.id.desk_btn_play:
                if (MainActivity.myservice.player.isPlaying()) {
                    MainActivity.myservice.pause();
                    mDeskPlay.setImageResource(R.mipmap.playbar_btn_play);
                    isplay = 1;
                } else {
                    MainActivity.myservice.going();
                    mDeskPlay.setImageResource(R.mipmap.playbar_btn_pause);
                    isplay = 2;
                }
                break;
            case R.id.desk_btn_next:
                MainActivity.myservice.next();
                if (isplay == 1) {
                    mDeskPlay.setImageResource(R.mipmap.playbar_btn_pause);
                }
                break;
            case R.id.desk_btn_prev:

                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("isplay", isplay);
        setResult(100, intent);
        finish();
    }
}
