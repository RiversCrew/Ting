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

import java.util.ArrayList;

/**
 * Created by ppxb on 2016/4/12.
 */
public class AritistListAdapter extends BaseAdapter {
    private ArrayList<Song> mSongList;
    private Context mContext;
    private ViewHolder holder;
    private LayoutInflater inflater;

    public AritistListAdapter(Context context, ArrayList<Song> list) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.artist_gridview_item, null);
            holder.mSongImg = (ImageView) convertView.findViewById(R.id.atrist_img);
            holder.mSongArtist = (TextView) convertView.findViewById(R.id.atrist_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Bitmap mImg = mSongList.get(position).getmImg();
        holder.mSongArtist.setText(mSongList.get(position).getmArtist());
        if (mImg != null) {
            holder.mSongImg.setImageBitmap(mImg);
        } else {
            holder.mSongImg.setImageResource(R.mipmap.empty_music);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView mSongImg;
        TextView mSongArtist;
    }
}
