package com.eminem.geekgank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eminem.geekgank.R;
import com.eminem.geekgank.adapter.ArticleAdapter;
import com.eminem.geekgank.app.App;
import com.eminem.geekgank.bean.Article;
import com.eminem.geekgank.constant.Constant;
import com.eminem.geekgank.utils.ToastUtil;
import com.eminem.geekgank.widget.RefreshLayout;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Eminem on 2016/6/24.
 */
public class AndroidFragment extends Fragment {
    App helper = App.getInstance();

    @Bind(R.id.lv_atricle)
    ListView lvAtricle;
    @Bind(R.id.swipe)
    RefreshLayout mSwipe;
    private  String url;
    private int page=1;
    private int curPage=1;
    private View footerLayout;
    private TextView text_more;
    private ProgressBar load_progress_bar;
    private ArticleAdapter adapter;
    private List<Article.ResultsBean> mData=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        ButterKnife.bind(this, view);
        initView();
        LoadArticle(1);
        return view;
    }

    /**
     * 初始化UI相关
     */
    private void initView() {

        footerLayout=View.inflate(App.getContext(),R.layout.listview_footer,null);
        text_more = (TextView) footerLayout.findViewById(R.id.text_more);
        load_progress_bar = (ProgressBar) footerLayout.findViewById(R.id.load_progress_bar);


        adapter=new ArticleAdapter(App.getContext(),mData);
        lvAtricle.addFooterView(footerLayout);
        mSwipe.setChildView(lvAtricle);
        lvAtricle.setAdapter(adapter);
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
         * 加载更多
         */
        mSwipe.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                LoadArticle(curPage + 1);
                Logger.e("加载更多");
                text_more.setVisibility(View.VISIBLE);
                load_progress_bar.setVisibility(View.VISIBLE);
                mSwipe.setLoading(false);
                LoadArticle(curPage + 1);
            }
        });

    }

    /**
     *  请求网络数据
     */

    private void LoadArticle(final int page) {
        url= Constant.ARTICLE_DATA + Constant.ANDROID + Constant.COUNT + page;
        StringRequest request=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (page == 1) {
                    mData.clear();
                }
                curPage = page;
                Article article=new Gson().fromJson(response,Article.class);
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
