package com.eminem.geekgank.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.eminem.geekgank.R;
import com.eminem.geekgank.activity.WebViewActivity;
import com.eminem.geekgank.adapter.ArticleRecycleAdapter;
import com.eminem.geekgank.app.App;
import com.eminem.geekgank.bean.Article;
import com.eminem.geekgank.constant.Constant;
import com.eminem.geekgank.utils.SharedPreferencesUtil;
import com.eminem.geekgank.utils.ToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Eminem on 2016/6/24.
 */
public class AndroidFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {
    @Bind(R.id.ivRefresh)
    ImageView ivRefresh;
    @Bind(R.id.swipe_target)
    RecyclerView swipeTarget;
    @Bind(R.id.ivLoadMore)
    ImageView ivLoadMore;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;

    App helper = App.getInstance();
    private String url;
    private int curPage = 1;
    private ArticleRecycleAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private List<Article.ResultsBean> mData = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank_common, container, false);
        ButterKnife.bind(this, view);
        initView();
//        LoadArticle(1);
        return view;
    }

    private void initView() {
        mLayoutManager = new LinearLayoutManager(App.getContext());
        swipeTarget.setLayoutManager(mLayoutManager);

        adapter = new ArticleRecycleAdapter(App.getContext(), mData);
        swipeTarget.setAdapter(adapter);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setRefreshing(true);

        adapter.setOnItemClickLitener(new ArticleRecycleAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(App.getContext(), "点击", Toast.LENGTH_SHORT).show();
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
                intent.putExtra("desc", mData.get(position).getDesc());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(App.getContext(), "长按", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onRefresh() {
        LoadArticle(1);
        swipeToLoadLayout.setRefreshing(false);

    }

    @Override
    public void onLoadMore() {
        LoadArticle(curPage + 1);
        swipeToLoadLayout.setLoadingMore(false);
    }

    /**
     * loadData
     */
    private void LoadArticle(final int page) {

        url = Constant.ARTICLE_DATA + Constant.ANDROID + Constant.COUNT + page;
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



}
