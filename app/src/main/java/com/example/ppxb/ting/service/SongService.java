package com.example.ppxb.ting.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by ppxb on 2016/4/13.
 */
public class SongService extends Service {

    private MediaPlayer player;
    private String url;
    private boolean isPlay;
    private int isPause;

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            isPlay = intent.getBooleanExtra("isPlay", false);
            isPause = intent.getIntExtra("isPause", 3);
            url = intent.getStringExtra("url");
            if (isPlay) {
                try {
                    player.reset();
                    player.setDataSource(url);
                    player.prepareAsync();
                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            player.start();
                        }
                    });
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            Intent intent = new Intent();
                            intent.setAction("autonext");
                            sendBroadcast(intent);
                        }
                    });
                } catch (Exception e) {

                }
            }
            if (isPause == 1) {
                player.pause();
            } else if (isPause == 2) {
                player.start();
            }
        } else
            intent = new Intent();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
        player = null;
        stopSelf();
        super.onDestroy();
    }
}
