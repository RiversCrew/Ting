package com.example.ppxb.ting.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ppxb.ting.R;
import com.example.ppxb.ting.model.Song;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by ppxb on 2016/4/12.
 */
public class SongListAdapter extends BaseAdapter {
    private ArrayList<Song> mSongList;
    private Context mContext;
    private ViewHolder holder;
    private LayoutInflater inflater;

    public SongListAdapter(Context context, ArrayList<Song> list) {
        this.mContext = context;
        this.mSongList = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mSongList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSongList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //需要进行图片的异步加载
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.song_list_item, null);
            holder.mSongTitle = (TextView) convertView.findViewById(R.id.song_title);
            holder.mSongArtist = (TextView) convertView.findViewById(R.id.song_artist);
            holder.mSongDuration = (TextView) convertView.findViewById(R.id.song_duration);
            holder.mSongImg = (ImageView) convertView.findViewById(R.id.song_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mSongTitle.setText(mSongList.get(position).getmTitle());
        holder.mSongArtist.setText(mSongList.get(position).getmArtist());
        holder.mSongDuration.setText(formatDuration(mSongList.get(position).getmDuration()));
        Bitmap mImg = mSongList.get(position).getmImg();
        if (mImg != null) {
            holder.mSongImg.setImageBitmap(mImg);
        } else {
            holder.mSongImg.setImageResource(R.mipmap.empty_music);
        }
        return convertView;
    }

    class ViewHolder {
        TextView mSongTitle;
        TextView mSongArtist;
        TextView mSongDuration;
        ImageView mSongImg;
    }

    /**
     * 对获取到的时间进行格式化
     */
    public String formatDuration(int duration) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(duration);
    }
}
