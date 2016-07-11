package com.eminem.geekgank.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eminem.geekgank.R;
import com.eminem.geekgank.bean.Article;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Eminem on 2016/6/25.
 * ListView未使用
 */
public class ArticleListAdapter extends BaseAdapter {
    private Context context;
    private List<Article.ResultsBean> list;

    public ArticleListAdapter(Context context, List<Article.ResultsBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        /**
         * butterknife在adapter的用法
         */
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = View.inflate(context, R.layout.item_article, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        /**
         * set data
         */
        Article.ResultsBean mData = list.get(position);
        holder.tvArt.setText(mData.getDesc());
        holder.tvTime.setText(mData.getPublishedAt().split("T")[0]);
        holder.tvName.setText(mData.getWho());
        return view;
    }

    static class ViewHolder {
        @Bind(R.id.tv_art)
        TextView tvArt;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_time)
        TextView tvTime;
//      @Bind(R.id.iv_blank)
//      ImageView iv_blank;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
