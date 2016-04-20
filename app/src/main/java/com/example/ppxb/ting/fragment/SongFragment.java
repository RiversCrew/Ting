package com.example.ppxb.ting.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ppxb.ting.R;
import com.example.ppxb.ting.adapter.SongListAdapter;
import com.example.ppxb.ting.model.Song;
import com.example.ppxb.ting.service.SongService;
import com.example.ppxb.ting.tool.ServiceUtil;
import com.example.ppxb.ting.tool.SimpleUtil;
import com.example.ppxb.ting.tool.SongUtil;

import java.util.ArrayList;

/**
 * Created by ppxb on 2016/4/11.
 */
public class SongFragment extends Fragment {
    private View view, below_listview;
    private ListView mSongListView;
    private TextView mSongNullText;
    private SongListAdapter adapter;
    private ArrayList<Song> mSongList;
    private PauseReciver pauseReciver;
    private NextReciver nextReciver, autoNextReciver;
    private PreReciver preReciver;
    private int isPause, nowPlaying;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (mSongList.size() > 10)
                    mSongListView.addFooterView(below_listview, null, false);//使footer不可点击
                adapter = new SongListAdapter(getContext(), mSongList);
                mSongListView.setAdapter(adapter);
                mSongNullText.setVisibility(View.GONE);
                mSongListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ServiceUtil.startServiceWithUrl(getContext(),
                                SongService.class,
                                mSongList.get(position).getmUrl());
                        ServiceUtil.startServiceWithActivity(getContext(),
                                SimpleUtil.getAlbumImage(getContext(),
                                        mSongList.get(position).getmImg()),
                                mSongList.get(position).getmTitle(),
                                mSongList.get(position).getmArtist());
                        nowPlaying = position;
                        ServiceUtil.startBroadcastNewSong(getContext());
                    }
                });
            } else if (msg.what == 1)
                mSongNullText.setText("没有找到可播放的文件");
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.song_fragment, container, false);
        initView();
        initData();
        return view;
    }

    public void initView() {
        pauseReciver = new PauseReciver();
        nextReciver = new NextReciver();
        autoNextReciver = new NextReciver();
        preReciver = new PreReciver();
        SimpleUtil.regReciver(pauseReciver, getContext(), "pause");
        SimpleUtil.regReciver(nextReciver, getContext(), "next");
        SimpleUtil.regReciver(autoNextReciver, getContext(), "autonext");
        SimpleUtil.regReciver(preReciver, getContext(), "pre");
        mSongListView = (ListView) view.findViewById(R.id.song_fragment_listview);
        mSongNullText = (TextView) view.findViewById(R.id.song_fragment_null);
        below_listview = LayoutInflater.from(getContext()).inflate(R.layout.below_song_listview, null);
    }

    public void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSongList = (ArrayList<Song>) SongUtil.getSongList(getContext());
                if (mSongList.size() > 0)
                    handler.sendEmptyMessage(0);
                else
                    handler.sendEmptyMessage(1);
            }
        }).start();
    }

    public void nextSong() {
        if (nowPlaying != mSongList.size()) {
            String url = mSongList.get(nowPlaying).getmUrl();
            ServiceUtil.startServiceWithUrl(getContext(), SongService.class, url);
            ServiceUtil.startServiceWithActivity(getContext(),
                    SimpleUtil.getAlbumImage(getContext(),
                            mSongList.get(nowPlaying).getmImg()),
                    mSongList.get(nowPlaying).getmTitle(),
                    mSongList.get(nowPlaying).getmArtist());
        } else {
            nowPlaying = 0;
            String url = mSongList.get(nowPlaying).getmUrl();
            ServiceUtil.startServiceWithUrl(getContext(), SongService.class, url);
            ServiceUtil.startServiceWithActivity(getContext(),
                    SimpleUtil.getAlbumImage(getContext(),
                            mSongList.get(nowPlaying).getmImg()),
                    mSongList.get(nowPlaying).getmTitle(),
                    mSongList.get(nowPlaying).getmArtist());
        }
    }

    public void preSong() {
        if (nowPlaying >= 0) {
            String url = mSongList.get(nowPlaying).getmUrl();
            ServiceUtil.startServiceWithUrl(getContext(), SongService.class, url);
            ServiceUtil.startServiceWithActivity(getContext(),
                    SimpleUtil.getAlbumImage(getContext(),
                            mSongList.get(nowPlaying).getmImg()),
                    mSongList.get(nowPlaying).getmTitle(),
                    mSongList.get(nowPlaying).getmArtist());
        } else {
            nowPlaying = mSongList.size() - 1;
            String url = mSongList.get(nowPlaying).getmUrl();
            ServiceUtil.startServiceWithUrl(getContext(), SongService.class, url);
            ServiceUtil.startServiceWithActivity(getContext(),
                    SimpleUtil.getAlbumImage(getContext(),
                            mSongList.get(nowPlaying).getmImg()),
                    mSongList.get(nowPlaying).getmTitle(),
                    mSongList.get(nowPlaying).getmArtist());
        }
    }

    class PauseReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            isPause = intent.getIntExtra("isPause", 3);
            ServiceUtil.startServicePause(getContext(), SongService.class, isPause);
        }
    }

    class NextReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            nowPlaying++;
            nextSong();
        }
    }

    class PreReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            nowPlaying--;
            preSong();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(pauseReciver);
        getContext().unregisterReceiver(nextReciver);
        getContext().unregisterReceiver(autoNextReciver);
        getContext().unregisterReceiver(preReciver);
    }
}
