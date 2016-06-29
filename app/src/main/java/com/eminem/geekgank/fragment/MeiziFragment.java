package com.eminem.geekgank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eminem.geekgank.R;
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
public class MeiziFragment extends Fragment {

    @Bind(R.id.meizi_recycleview)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipe;
    private List<Meizi.ResultsBean> mData=new ArrayList<>();
    private MeiziAdapter adapter;
    App helper = App.getInstance();

    private String url;
    private int curPage=1;
    private int page=1;
    private StaggeredGridLayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meizi, container, false);
        ButterKnife.bind(this, view);
        initView();
        loadMeizi(1);
        return view;
    }

    private void initView() {

        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mLayoutManager.invalidateSpanAssignments(); //防止第一行到顶部有空白区域
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setHasFixedSize(true);
        //设置item之间的间隔
//        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
//        recyclerView.addItemDecoration(decoration);


        adapter=new MeiziAdapter(App.getContext(),mData);
        mRecyclerView.setAdapter(adapter);

        mSwipe.setColorSchemeColors(R.color.colorAccent, R.color.colorPrimaryDark);

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMeizi(1);
                mSwipe.setRefreshing(false);
            }
        });

    }

    private void loadMeizi(final int page) {
        url= Constant.ARTICLE_DATA + Constant.FULI + Constant.COUNT + page;
        StringRequest request=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (page == 1) {
                    mData.clear();
                }
                curPage = page;
                Meizi meizi=new Gson().fromJson(response,Meizi.class);
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

    private void addData(Meizi resBean) {
        for (Meizi.ResultsBean meizi : resBean.getResults()) {
            if (!mData.contains(meizi)) {
                mData.add(meizi);
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
