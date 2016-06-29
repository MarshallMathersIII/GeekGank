package com.eminem.geekgank;

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

import com.eminem.geekgank.adapter.MyFragmentAdapter;
import com.eminem.geekgank.app.App;
import com.eminem.geekgank.fragment.AndroidFragment;
import com.eminem.geekgank.fragment.MeiziFragment;
import com.eminem.geekgank.fragment.IOSFragment;
import com.eminem.geekgank.fragment.JSFragment;
import com.eminem.geekgank.fragment.VideoFragment;
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
        MyFragmentAdapter fragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager()
                , getFragments());
        vp = (ViewPager) findViewById(R.id.vp);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_exit){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new AndroidFragment());
        fragments.add(new IOSFragment());
        fragments.add(new MeiziFragment());
        fragments.add(new JSFragment());
        fragments.add(new VideoFragment());
        return fragments;
    }

    /**
     * 回到顶部
     * Activity与Fragment交互
     */
    @OnClick(R.id.fabBtn)
    public void onClick() {
//      ListView lvArticle= (ListView) getFragmentManager().findFragmentById(R.id.ll_android).getView().findViewById(R.id.lv_atricle);
//        if (!lvArticle.isStackFromBottom()){
//            lvArticle.setStackFromBottom(true);
//        }
//        lvArticle.setStackFromBottom(false);
        ToastUtil.show(App.getContext(),"回到顶部", Toast.LENGTH_LONG);

    }
}
