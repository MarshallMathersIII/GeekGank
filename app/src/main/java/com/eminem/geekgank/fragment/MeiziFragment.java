package com.eminem.geekgank.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.eminem.geekgank.activity.PreviewActivity;
import com.eminem.geekgank.adapter.MeiziAdapter;
import com.eminem.geekgank.app.App;
import com.eminem.geekgank.bean.Meizi;
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
public class MeiziFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {


    @Bind(R.id.ivRefresh)
    ImageView ivRefresh;
    @Bind(R.id.swipe_target)
    RecyclerView swipeTarget;
    @Bind(R.id.ivLoadMore)
    ImageView ivLoadMore;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;

    private List<Meizi.ResultsBean> mData = new ArrayList<>();
    private MeiziAdapter adapter;
    App helper = App.getInstance();

    private String url;
    private int curPage = 1;
    private int page = 1;
    private StaggeredGridLayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank_common, container, false);
        ButterKnife.bind(this, view);
        initView();

        return view;
    }

    private void initView() {
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        swipeTarget.setLayoutManager(mLayoutManager);

        adapter = new MeiziAdapter(App.getContext(), mData);
        swipeTarget.setAdapter(adapter);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setRefreshing(true);

        adapter.setOnItemClickLitener(new MeiziAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(App.getContext(), "点击", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(App.getContext(), PreviewActivity.class);
                intent.putExtra("url",mData.get(position).getUrl());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(App.getContext(), "长按", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        LoadArticle(1);


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

        url = Constant.ARTICLE_DATA + Constant.FULI + Constant.COUNT + page;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (page == 1) {
                    mData.clear();
                }
                curPage = page;
                Meizi meizi = new Gson().fromJson(response, Meizi.class);
                addData(meizi);

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
    private void addData(Meizi resBean) {
        for (Meizi.ResultsBean meizi : resBean.getResults()) {
            if (!mData.contains(meizi)) {
                mData.add(meizi);
            }
        }
        adapter.notifyDataSetChanged();
        swipeToLoadLayout.setRefreshing(false);

    }


}
