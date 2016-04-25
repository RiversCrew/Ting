package com.example.ppxb.ting.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
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
import com.example.ppxb.ting.reciver.SongInfoReciver;
import com.example.ppxb.ting.service.SongService;
import com.example.ppxb.ting.tool.SimpleUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout mTitle_menu;
    private RelativeLayout mTitle_search;
    private LinearLayout mBar;
    private ImageView mBarimg, mPlay, mNext;
    private TextView mBartitle, mBarartist, mTitlte_text;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ServiceConnection serviceConnection;
    private MainTabAdapter adapter;
    private FragmentManager fm;
    private ArrayList<Fragment> mFragmentList;
    private ArrayList<String> mTitleList;
    public static SongService myservice;
    private SongInfoReciver reciver;
    private int isplay;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mPlay.setImageResource(R.mipmap.playbar_btn_pause);
                mBar.setVisibility(View.VISIBLE);
                mBartitle.setText(reciver.getTitle());
                mBarartist.setText(reciver.getArtist());
                mBarimg.setImageBitmap(reciver.getImg());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleUtil.closeToTop(this);
        setContentView(R.layout.activity_main);
        initView();
        initBindService();
        initData();
    }

    public void initView() {
        reciver = new SongInfoReciver(this, handler);
        SimpleUtil.regReciver(reciver, this, "play");
        mTitle_menu = (RelativeLayout) findViewById(R.id.main_title_menu);
        mTitle_search = (RelativeLayout) findViewById(R.id.main_title_search);
        mBar = (LinearLayout) findViewById(R.id.minibar);
        mTitlte_text = (TextView) findViewById(R.id.main_title_text);
        mBarartist = (TextView) findViewById(R.id.bar_artist);
        mBartitle = (TextView) findViewById(R.id.bar_title);
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.main_tablayout);
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
        mBar.setOnClickListener(this);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                myservice = ((SongService.songBinder) service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
    }

    public void initBindService() {
        Intent intent = new Intent(this, SongService.class);
        startService(intent);
        intent = new Intent(this, SongService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
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
        if (v == mPlay) {
            if (MainActivity.myservice.player.isPlaying()) {
                MainActivity.myservice.pause();
                mPlay.setImageResource(R.mipmap.playbar_btn_play);
                isplay = 1;
            } else {
                MainActivity.myservice.going();
                mPlay.setImageResource(R.mipmap.playbar_btn_pause);
                isplay = 2;
            }
        }
        if (v == mNext) {
            MainActivity.myservice.next();
            if (isplay == 1) {
                mPlay.setImageResource(R.mipmap.playbar_btn_pause);
            }
        }
        if (v == mBar) {
            Intent intent = new Intent(MainActivity.this, SongDetailActivity.class);
            intent.putExtra("isplay", isplay);
            intent.putExtra("blur", SimpleUtil.sendBitmap(reciver.getBlur()));
            intent.putExtra("img", SimpleUtil.sendBitmap(reciver.getImg()));
            intent.putExtra("title", reciver.getTitle());
            intent.putExtra("artist", reciver.getArtist());
            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(reciver);
        Intent intent = new Intent(this, SongService.class);
        stopService(intent);
        unbindService(serviceConnection);
    }
}