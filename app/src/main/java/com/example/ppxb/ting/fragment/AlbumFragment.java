package com.example.ppxb.ting.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ppxb.ting.R;
import com.example.ppxb.ting.activity.MainActivity;
import com.example.ppxb.ting.adapter.AlbumListAdapter;
import com.example.ppxb.ting.model.Song;
import com.example.ppxb.ting.tool.SongUtil;

import java.util.ArrayList;

/**
 * Created by ppxb on 2016/4/11.
 */
public class AlbumFragment extends Fragment {
    private ArrayList<Song> mAlbumList;
    private ListView mAlbumListView;
    private TextView mAlbumNullText;
    private View below_listview, view;
    private AlbumListAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (mAlbumList.size() > 10 && MainActivity.mBar.getVisibility() == View.VISIBLE)
                        mAlbumListView.addFooterView(below_listview, null, false);
                    adapter = new AlbumListAdapter(getContext(), mAlbumList);
                    mAlbumListView.setAdapter(adapter);
                    mAlbumNullText.setVisibility(View.GONE);
                    break;
                case 1:
                    mAlbumNullText.setText("没有找到可播放的专辑");
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.album_fragment, container, false);
        initView();
        initData();
        return view;
    }

    public void initView() {
        below_listview = LayoutInflater.from(getContext()).inflate(R.layout.below_song_listview, null);
        mAlbumNullText = (TextView) view.findViewById(R.id.album_fragment_null);
        mAlbumListView = (ListView) view.findViewById(R.id.album_fragment_listview);
    }

    public void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAlbumList = (ArrayList<Song>) SongUtil.getSongList(getContext());
                if (mAlbumList.size() > 0)
                    handler.sendEmptyMessage(0);
                else
                    handler.sendEmptyMessage(1);
            }
        }).start();
    }
}
