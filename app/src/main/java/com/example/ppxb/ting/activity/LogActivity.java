package com.example.ppxb.ting.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ppxb.ting.R;
import com.example.ppxb.ting.tool.SimpleUtil;

public class LogActivity extends AppCompatActivity {
    private ImageView mAboutLogo;
    private TextView mTitle;
    private TextView mContent;
    private TextView mCopyRight;
    private Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleUtil.FullScreen(this);
        setContentView(R.layout.activity_log);
        initView();
        initData();
    }

    public void initView() {
        mAboutLogo = (ImageView) findViewById(R.id.logo_about_logo);
        mTitle = (TextView) findViewById(R.id.logo_title);
        mContent = (TextView) findViewById(R.id.logo_content);
        mCopyRight = (TextView) findViewById(R.id.logo_copyright);
    }

    public void initData() {
        AssetManager amg = this.getAssets();
        Typeface font = Typeface.createFromAsset(amg, "fonts/title.ttf");
        mTitle.setTypeface(font);
        SimpleUtil.logoAnimation(this, mAboutLogo, 100);
        SimpleUtil.logoAnimation(this, mTitle, 800);
        SimpleUtil.logoAnimation(this, mContent, 1300);
        SimpleUtil.logoAnimation(this, mCopyRight, 1800);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LogActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                finish();
            }
        }, 2600);
    }
}
