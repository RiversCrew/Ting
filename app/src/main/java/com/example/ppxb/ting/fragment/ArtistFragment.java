package com.example.ppxb.ting.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.ppxb.ting.R;
import com.example.ppxb.ting.adapter.AritistListAdapter;
import com.example.ppxb.ting.model.Song;
import com.example.ppxb.ting.tool.SongUtil;

import java.util.ArrayList;

/**
 * Created by ppxb on 2016/4/11.
 */
public class ArtistFragment extends Fragment {
    private View view, below_listview;
    private ArrayList<Song> mArtistList;
    private GridView mArtistGridView;
    private TextView mArtistNullText;
    private AritistListAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    adapter = new AritistListAdapter(getContext(), mArtistList);
                    mArtistGridView.setAdapter(adapter);
                    mArtistNullText.setVisibility(View.GONE);
                    break;
                case 1:
                    mArtistNullText.setText("没有找到存在的艺术家");
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.artist_fragment, container, false);
        initView();
        initData();
        return view;
    }

    public void initView() {
        mArtistGridView = (GridView) view.findViewById(R.id.artist_fragment_gridview);
        below_listview = LayoutInflater.from(getContext()).inflate(R.layout.below_song_listview, null);
        mArtistNullText = (TextView) view.findViewById(R.id.artist_fragment_null);
    }

    public void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mArtistList = (ArrayList<Song>) SongUtil.getSongList(getContext());
                if (mArtistList.size() > 0)
                    handler.sendEmptyMessage(0);
                else
                    handler.sendEmptyMessage(1);
            }
        }).start();
    }
}
