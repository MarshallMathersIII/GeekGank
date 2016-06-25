package com.eminem.geekgank;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.eminem.geekgank.adapter.MyFragmentAdapter;
import com.eminem.geekgank.fragment.AndroidFragment;
import com.eminem.geekgank.fragment.FuliFragment;
import com.eminem.geekgank.fragment.IOSFragment;
import com.eminem.geekgank.fragment.JSFragment;
import com.eminem.geekgank.fragment.VideoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.vp)
    ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        /**
         * Toolbar的设置
         */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /**
         *
         */
        MyFragmentAdapter fragmentAdapter=new MyFragmentAdapter(getSupportFragmentManager()
                ,getFragments());
        vp= (ViewPager) findViewById(R.id.vp);
        vp.setAdapter(fragmentAdapter);
        /**
         * TabLayout的设置
         */
        String[] list_title = new String[]{"Android", "iOS", "福利", "前端", "休息视频"};
        //设置TabLayout的模式
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tablayout.addTab(tablayout.newTab().setText(list_title[0]));
        tablayout.addTab(tablayout.newTab().setText(list_title[1]));
        tablayout.addTab(tablayout.newTab().setText(list_title[2]));
        tablayout.addTab(tablayout.newTab().setText(list_title[3]));
        tablayout.addTab(tablayout.newTab().setText(list_title[4]));
        /**
         * viewpager的设置
         */
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //切换到指定的item
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private List<Fragment> getFragments() {
        List<Fragment>fragments=new ArrayList<>();
        fragments.add(new AndroidFragment());
        fragments.add(new IOSFragment());
        fragments.add(new FuliFragment());
        fragments.add(new JSFragment());
        fragments.add(new VideoFragment());
        return fragments;
    }


}
