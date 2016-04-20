package com.example.ppxb.ting.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.ppxb.ting.R;
import com.example.ppxb.ting.tool.SimpleUtil;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout mSearch_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleUtil.closeToTop(this);
        setContentView(R.layout.activity_search);
        initView();
        initData();
    }

    public void initView() {
        mSearch_back = (RelativeLayout) findViewById(R.id.search_back);
    }

    public void initData() {
        mSearch_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_back:
                finish();
                break;
        }
    }
}
