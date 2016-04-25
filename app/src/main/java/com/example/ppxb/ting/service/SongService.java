package com.example.ppxb.ting.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.ppxb.ting.fragment.SongFragment;
import com.example.ppxb.ting.tool.ServiceUtil;
import com.example.ppxb.ting.tool.SimpleUtil;

/**
 * Created by ppxb on 2016/4/13.
 */
public class SongService extends Service implements MediaPlayer.OnPreparedListener {
    private static final String TAG = "SongService";

    public MediaPlayer player;
    private songBinder songBinder;
    private int current;

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        songBinder = new songBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return songBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

    public class songBinder extends Binder {
        public SongService getService() {
            return SongService.this;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    public void play(String url, int position) {
        try {
            current = position;
            player.reset();
            player.setDataSource(url);
            player.prepareAsync();
            player.setOnPreparedListener(this);
        } catch (Exception e) {

        }
    }

    public void pause() {
        player.pause();
    }

    public void going() {
        player.start();
    }

    public void next() {
        if (current == SongFragment.mSongList.size() - 1) {
            current = 0;
            play(SongFragment.mSongList.get(current).getmUrl(), current);
            ServiceUtil.startServiceWithActivity(this,
                    SimpleUtil.getAlbumImage(this,
                            SongFragment.mSongList.get(current).getmImg()),
                    SongFragment.mSongList.get(current).getmTitle(),
                    SongFragment.mSongList.get(current).getmArtist());
        } else {
            current += 1;
            play(SongFragment.mSongList.get(current).getmUrl(), current);
            ServiceUtil.startServiceWithActivity(this,
                    SimpleUtil.getAlbumImage(this,
                            SongFragment.mSongList.get(current).getmImg()),
                    SongFragment.mSongList.get(current).getmTitle(),
                    SongFragment.mSongList.get(current).getmArtist());
        }
    }
}
