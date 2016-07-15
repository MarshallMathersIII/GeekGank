package com.eminem.geekgank.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.eminem.geekgank.R;
import com.eminem.geekgank.adapter.ArticleRecycleAdapter;
import com.eminem.geekgank.app.App;
import com.eminem.geekgank.bean.Article;
import com.eminem.geekgank.constant.Constant;
import com.eminem.geekgank.utils.ToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Eminem on 2016/6/24.
 */
public class JSFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {


    App helper = App.getInstance();
    @Bind(R.id.ivRefresh)
    ImageView ivRefresh;
    @Bind(R.id.swipe_target)
    RecyclerView swipeTarget;
    @Bind(R.id.ivLoadMore)
    ImageView ivLoadMore;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    private String url;
    private int curPage = 1;
    private ArticleRecycleAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private List<Article.ResultsBean> mData = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank_common, container, false);
        ButterKnife.bind(this, view);
        initView();
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
        url = Constant.ARTICLE_DATA + Constant.VIDEO + Constant.COUNT + page;
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
