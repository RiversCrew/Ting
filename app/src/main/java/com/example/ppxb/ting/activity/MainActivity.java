package com.example.ppxb.ting.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ppxb.ting.R;
import com.example.ppxb.ting.adapter.MainTabAdapter;
import com.example.ppxb.ting.fragment.AlbumFragment;
import com.example.ppxb.ting.fragment.ArtistFragment;
import com.example.ppxb.ting.fragment.SongFragment;
import com.example.ppxb.ting.reciver.newSongReciver;
import com.example.ppxb.ting.tool.FastBlur;
import com.example.ppxb.ting.tool.ServiceUtil;
import com.example.ppxb.ting.tool.SimpleUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout mTitle_menu, mTitle_search;
    private LinearLayout mBar;
    private ImageView mBarimg, mPlay, mNext;
    private TextView mBartitle, mBarartist, mTitlte_text;
    public static String title, artist;
    private Bitmap img;
    public static Bitmap blur, album;
    private DrawerLayout mLeft_menu;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MainTabAdapter adapter;
    private FragmentManager fm;
    private ArrayList<Fragment> mFragmentList;
    private ArrayList<String> mTitleList;
    private barReciver reciver;
    private newSongReciver newSongReciver;
    public static int isPause;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mBar.setVisibility(View.VISIBLE);
                mBar.setOnClickListener(MainActivity.this);
                mBarimg.setImageBitmap(img);
                mBartitle.setText(title);
                mBarartist.setText(artist);
            } else if (msg.what == 1) {
                isPause = 2;
                mPlay.setImageResource(R.mipmap.playbar_btn_pause);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleUtil.closeToTop(this);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    public void initView() {
        reciver = new barReciver();
        newSongReciver = new newSongReciver(handler);
        SimpleUtil.regReciver(reciver, this, "playing");
        SimpleUtil.regReciver(newSongReciver, this, "newsong");
        mTitle_menu = (RelativeLayout) findViewById(R.id.main_title_menu);
        mTitle_search = (RelativeLayout) findViewById(R.id.main_title_search);
        mBar = (LinearLayout) findViewById(R.id.minibar);
        mTitlte_text = (TextView) findViewById(R.id.main_title_text);
        mBarartist = (TextView) findViewById(R.id.bar_artist);
        mBartitle = (TextView) findViewById(R.id.bar_title);
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        mLeft_menu = (DrawerLayout) findViewById(R.id.main_left_menu);
        mBarimg = (ImageView) findViewById(R.id.bar_img);
        mPlay = (ImageView) findViewById(R.id.song_play);
        mNext = (ImageView) findViewById(R.id.song_next);
        fm = getSupportFragmentManager();
        mFragmentList = new ArrayList<>();
        mTitleList = new ArrayList<>();
        mTitle_menu.setOnClickListener(this);
        mTitle_search.setOnClickListener(this);
        mPlay.setOnClickListener(this);
        mNext.setOnClickListener(this);
        isPause = 1;
    }

    public void initData() {
        AssetManager amg = this.getAssets();
        Typeface font = Typeface.createFromAsset(amg, "fonts/title.ttf");
        mTitlte_text.setTypeface(font);
        mFragmentList.add(new SongFragment());
        mFragmentList.add(new AlbumFragment());
        mFragmentList.add(new ArtistFragment());
        mTitleList.add("音乐");
        mTitleList.add("专辑");
        mTitleList.add("艺术家");
        adapter = new MainTabAdapter(fm, mFragmentList, mTitleList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_title_menu:
                mLeft_menu.openDrawer(GravityCompat.START);
                break;
            case R.id.main_title_search:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
            case R.id.song_play:
                if (isPause == 1) {
                    mPlay.setImageResource(R.mipmap.playbar_btn_play);
                    ServiceUtil.startBrodcastControl(MainActivity.this, isPause);
                    isPause = 2;
                } else {
                    mPlay.setImageResource(R.mipmap.playbar_btn_pause);
                    ServiceUtil.startBrodcastControl(MainActivity.this, isPause);
                    isPause = 1;
                }
                break;
            case R.id.song_next:
                ServiceUtil.startBroadcastNext(MainActivity.this);
                break;
            case R.id.minibar:
                startActivity(new Intent(MainActivity.this, SongDetailActivity.class));
                overridePendingTransition(R.anim.tran_left_in, R.anim.tran_left_out);
                break;
        }
    }

    class barReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            title = intent.getStringExtra("title");
            artist = intent.getStringExtra("artist");
            byte[] bis = intent.getByteArrayExtra("img");
            img = BitmapFactory.decodeByteArray(bis, 0, bis.length);
            album = img;
            doBlur(img);
            handler.sendEmptyMessage(0);
        }
    }

    public void doBlur(final Bitmap original) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap temp = null;
                Bitmap result = null;
                try {
                    if (original == null) {
                        temp = BitmapFactory.decodeResource(getResources(), R.mipmap.empty_music);
                        result = FastBlur.doBlur(temp, 10, false);
                        return result;
                    }
                    result = FastBlur.doBlur(original, 10, false);
                } catch (Error e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                blur = bitmap;
            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(reciver);
        unregisterReceiver(newSongReciver);
    }
}
