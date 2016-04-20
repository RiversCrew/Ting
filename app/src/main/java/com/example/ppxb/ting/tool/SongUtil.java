package com.example.ppxb.ting.tool;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import com.example.ppxb.ting.model.Song;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ppxb on 2016/4/12.
 */
public class SongUtil {

    private static final Uri ARTURI = Uri.parse("content://media/external/audio/albumart");
    public static final String[] SONG_KEYS = new String[]{
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.IS_MUSIC,
            MediaStore.Audio.Media.DATA
    };

    public static List<Song> getSongList(Context context) {
        List<Song> songList = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                SONG_KEYS,
                null,
                null,
                null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String mTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String mArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String mAlbum = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String mDisplayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String mUrl = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                int mId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                int mArtistid = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
                int mDuration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                long mSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                int mAlbumid = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
                Bitmap mImg = getSongImg(context, mId, mAlbumid);
                if (isMusic == 1 && mDuration > 120000) {
                    Song song = new Song(mTitle, mArtist, mAlbum,
                            mDisplayName, mUrl, mId, mArtistid,
                            mAlbumid, mDuration, mSize, mImg);
                    songList.add(song);
                } else {
                    continue;
                }
            }
        }
        cursor.close();
        return songList;
    }

    public static Bitmap getSongImg(Context context, long songId, long albumId) {
        Bitmap img = null;
        if (albumId < 0 && songId < 0) {
            Log.e("WRONG", "NO SONG!");
        }
        try {
            if (albumId < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/"
                        + songId + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver()
                        .openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    img = BitmapFactory.decodeFileDescriptor(fd);
                }
            } else {
                Uri uri = ContentUris.withAppendedId(ARTURI, albumId);
                ParcelFileDescriptor pfd = context.getContentResolver()
                        .openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    img = BitmapFactory.decodeFileDescriptor(fd);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {

        }
        return img;
    }
}
