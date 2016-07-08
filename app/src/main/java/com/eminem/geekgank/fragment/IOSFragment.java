package com.eminem.geekgank.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eminem.geekgank.R;
import com.eminem.geekgank.activity.WebViewActivity;
import com.eminem.geekgank.adapter.PullMoreRecyclerAdapter;
import com.eminem.geekgank.app.App;
import com.eminem.geekgank.bean.Article;
import com.eminem.geekgank.constant.Constant;
import com.eminem.geekgank.utils.SharedPreferencesUtil;
import com.eminem.geekgank.utils.ToastUtil;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Eminem on 2016/6/24.
 *
 */
public class IOSFragment extends Fragment {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe)
    SwipeRefreshLayout mSwipe;

    App helper = App.getInstance();

    private String url;
    private int curPage = 1;
    private int page = 1;


    private PullMoreRecyclerAdapter adapter;
    LinearLayoutManager mLayoutManager;
    private List<Article.ResultsBean> mData = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank_recycle, container, false);
        ButterKnife.bind(this, view);
        initView();
        LoadArticle(1);
        return view;
    }

    private void initView() {
        mLayoutManager = new LinearLayoutManager(App.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        adapter = new PullMoreRecyclerAdapter(App.getContext(), mData);
        mRecyclerView.setAdapter(adapter);

        mSwipe.setColorSchemeColors(R.color.colorAccent, R.color.colorPrimaryDark);

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadArticle(1);
                Logger.e("下拉刷新");
                mSwipe.setRefreshing(false);
            }
        });

        /**
         * 上拉加载
         */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!mSwipe.isRefreshing()) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 ==
                            adapter.getItemCount()) {
                        mSwipe.setEnabled(false);
                        adapter.setMoreStatus(PullMoreRecyclerAdapter.LOADING_MORE);
                        LoadArticle(curPage + 1);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        /**
         * 点击事件
         */
        adapter.setOnItemClickLitener(new PullMoreRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                //在本地记录已经读的数据，
                //点击变色
                String ids = (String) SharedPreferencesUtil.get(App.getContext(), "read_ids", "");
                String readId = mData.get(position).get_id();
                if (!ids.contains(readId)) {
                    ids = ids + readId + ",";
                    SharedPreferencesUtil.put(App.getContext(), "read_ids", ids);
                }
                // mNewsAdapter.notifyDataSetChanged();
                changeReadState(view);// 实现局部界面刷新, 这个view就,提高效率局部刷新

                Toast.makeText(App.getContext(), "点击", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(App.getContext(), WebViewActivity.class);
                intent.putExtra("url", mData.get(position).getUrl());
                startActivity(intent);

            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(App.getContext(), "长按", Toast.LENGTH_SHORT).show();

            }
        });
    }
    /**
     * 改变已读新闻的颜色
     */
    private void changeReadState(View view) {
        TextView tvart = (TextView) view.findViewById(R.id.tv_art);
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
        tvart.setTextColor(Color.GRAY);
        tvName.setTextColor(Color.GRAY);
        tvTime.setTextColor(Color.GRAY);
    }

    /**
     * loadData
     */
    private void LoadArticle(final int page) {
        url = Constant.ARTICLE_DATA + Constant.IOS + Constant.COUNT + page;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (page == 1) {
                    mData.clear();
                }
                curPage = page;
                Article article = new Gson().fromJson(response, Article.class);
                addData(article);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.show(App.getContext(), "网络错误", Toast.LENGTH_LONG);
            }
        });

        helper.add(request);

    }

    /**
     * 数据处理
     */
    private void addData(Article resBean) {
        for (Article.ResultsBean article : resBean.getResults()) {
            if (!mData.contains(article)) {
                mData.add(article);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
