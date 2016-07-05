package com.eminem.geekgank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eminem.geekgank.R;
import com.eminem.geekgank.adapter.PullMoreRecyclerAdapter;
import com.eminem.geekgank.app.App;
import com.eminem.geekgank.bean.Article;
import com.eminem.geekgank.utils.ToastUtil;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Eminem on 2016/7/5.
 * Fragment
 */
public abstract class BaseFragment extends Fragment {
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
        /**
         * 下拉刷新
         */
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
        adapter.setOnItemClickLitener(new PullMoreRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(App.getContext(), "点击", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(App.getContext(), "长按", Toast.LENGTH_SHORT).show();

            }
        });

    }

    /**
     * loadData
     */
    private void LoadArticle(final int page) {
//        url = Constant.ARTICLE_DATA + Constant.VIDEO + Constant.COUNT + page;
        StringRequest request = new StringRequest(this.getUrl(), new Response.Listener<String>() {
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


    /**
     * 子类必须实现初始化布局的方法
     */
    public abstract String getUrl();
}
