package com.example.ppxb.ting.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by ppxb on 2016/4/15.
 */
public class newSongReciver extends BroadcastReceiver {
    private Handler handler;

    public newSongReciver(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        handler.sendEmptyMessage(1);
    }
}