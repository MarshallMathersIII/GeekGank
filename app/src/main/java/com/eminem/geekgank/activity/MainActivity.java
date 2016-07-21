package com.eminem.geekgank.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.eminem.geekgank.R;
import com.eminem.geekgank.adapter.MyFragmentAdapter;
import com.eminem.geekgank.app.App;
import com.eminem.geekgank.fragment.AndroidFragment;
import com.eminem.geekgank.fragment.IOSFragment;
import com.eminem.geekgank.fragment.JSFragment;
import com.eminem.geekgank.fragment.MeiziFragment;
import com.eminem.geekgank.fragment.VideoFragment;
import com.eminem.geekgank.utils.NightModeHelper;
import com.eminem.geekgank.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.vp)
    ViewPager vp;
    @Bind(R.id.fabBtn)
    FloatingActionButton fabBtn;
    @Bind(R.id.rootLayout)
    FrameLayout rootLayout;


    private NightModeHelper mNightModeHelper;
    private SharedPreferences config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNightModeHelper = new NightModeHelper(this, R.style.AppTheme_Light);

        config = getSharedPreferences("config", MODE_PRIVATE);

        int themeId = getThemeId();
        if (themeId != 0) {
            setTheme(themeId);
        }

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
        MyFragmentAdapter fragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager()
                , getFragments());
        vp = (ViewPager) findViewById(R.id.vp);

        vp.setAdapter(fragmentAdapter);
        /**
         * TabLayout的设置
         */
        String[] list_title = new String[]{"Android", "IOS", "小视频", "前端", "福利秀"};
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
        vp.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
//            changeTheme();
            mNightModeHelper.toggle();
            return true;
        } else if (id == R.id.action_exit) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new AndroidFragment());
        fragments.add(new IOSFragment());
        fragments.add(new VideoFragment());
        fragments.add(new JSFragment());
        fragments.add(new MeiziFragment());
        return fragments;
    }

    /**
     * 回到顶部
     * Activity与Fragment交互
     */
    @OnClick(R.id.fabBtn)
    public void onClick() {



        ToastUtil.show(App.getContext(), "回到顶部", Toast.LENGTH_LONG);

    }

    /**
     * 夜间模式
     */

//    public void changeTheme() {
//        if (isNightMode) {
//            setTheme(R.style.AppTheme2);
//            isNightMode = false;
//        } else {
//            setTheme(R.style.ThemeNight);
//            isNightMode = true;
//        }
//        setContentView(R.layout.activity_main);
//    }
    private int getThemeId() {
        return config.getInt("theme_id", 0);
    }
}
