package com.eminem.geekgank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eminem.geekgank.R;
import com.eminem.geekgank.adapter.CommonAdapter;
import com.eminem.geekgank.app.App;
import com.eminem.geekgank.bean.Article;
import com.eminem.geekgank.constant.Constant;
import com.eminem.geekgank.utils.ToastUtil;
import com.github.captain_miao.recyclerviewutils.WrapperRecyclerView;
import com.github.captain_miao.recyclerviewutils.common.DefaultLoadMoreFooterView;
import com.github.captain_miao.recyclerviewutils.listener.RefreshRecyclerViewListener;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Eminem on 2016/6/24.
 */
public class JSFragment extends Fragment {


//    @Bind(R.id.swipe)
//    SwipeRefreshLayout mSwipe;

    App helper = App.getInstance();
    @Bind(R.id.recyclerView)
    WrapperRecyclerView mRecyclerView;

    private String url;
    private int curPage = 1;
    private int page = 1;

    private CommonAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    private List<Article.ResultsBean> mData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank_common, container, false);
        ButterKnife.bind(this, view);
        initView();
        LoadArticle(1);
        return view;
    }

    private void initView() {
        mLayoutManager = new LinearLayoutManager(App.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CommonAdapter(App.getContext(), mData);

        mAdapter.setLoadMoreFooterView(new DefaultLoadMoreFooterView(App.getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRecyclerViewListener(new RefreshRecyclerViewListener() {
            @Override
            public void onRefresh() {
                LoadArticle(1);
                Logger.e("下拉刷新");
//                mSwipe.setRefreshing(false);

            }

            @Override
            public void onLoadMore(int pagination, int pageSize) {

            }
        });
//        mRecyclerView.post(new Runnable() {
//            @Override
//            public void run() {
//                mRecyclerView.autoRefresh();
//            }
//        });


    }

    private void LoadArticle(final int page) {
        Logger.e("1111111111", page);
        url = Constant.ARTICLE_DATA + Constant.IOS + Constant.COUNT + page;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.e("22222", response);
                if (page == 1) {
                    mData.clear();
                }
                curPage = page;
                Article article = new Gson().fromJson(response, Article.class);
                Logger.e("文章", article);
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
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
