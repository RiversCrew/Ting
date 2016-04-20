package com.example.ppxb.ting.model;

import android.graphics.Bitmap;

/**
 * Created by ppxb on 2016/4/12.
 */
public class Song {
    private String mTitle,
            mArtist,
            mAlbum,
            mDisplayName,
            mUrl;

    private int mId,
            mArtistId,
            mAlbumId;

    private int mDuration = 0;
    private long mSize = 0;
    private Bitmap mImg;


    public Song(String mTitle, String mArtist, String mAlbum,
                String mDisplayName, String mUrl, int mId, int mArtistId, int mAlbumId,
                int mDuration, long mSize, Bitmap mImg) {
        this.mTitle = mTitle;
        this.mArtist = mArtist;
        this.mAlbum = mAlbum;
        this.mDisplayName = mDisplayName;
        this.mUrl = mUrl;
        this.mId = mId;
        this.mArtistId = mArtistId;
        this.mDuration = mDuration;
        this.mSize = mSize;
        this.mAlbumId = mAlbumId;
        this.mImg = mImg;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmArtist() {
        return mArtist;
    }

    public String getmAlbum() {
        return mAlbum;
    }

    public String getmDisplayName() {
        return mDisplayName;
    }

    public String getmUrl() {
        return mUrl;
    }

    public int getmId() {
        return mId;
    }

    public int getmArtistId() {
        return mArtistId;
    }

    public int getmDuration() {
        return mDuration;
    }

    public long getmSize() {
        return mSize;
    }

    public int getmAlbumId() {
        return mAlbumId;
    }

    public Bitmap getmImg() {
        return mImg;
    }
}
