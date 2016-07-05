package com.eminem.geekgank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eminem.geekgank.R;
import com.eminem.geekgank.bean.Article;

import java.util.List;

/**
 * Created by Eminem on 2016/6/26.
 * 未使用
 */
public class ArticleRecycleAdapter extends RecyclerView.Adapter<ArticleRecycleAdapter.ViewHolder> {
    private Context context;
    private List<Article.ResultsBean> list;

    public ArticleRecycleAdapter(Context context, List<Article.ResultsBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(View.inflate(context,R.layout.item_article,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article.ResultsBean result = list.get(position);
        holder.tvTitle.setText(result.getDesc());
        holder.tvWho.setText(result.getWho());
        holder.tvTime.setText(result.getPublishedAt().split("T")[0]);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvWho, tvTime;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_art);
            tvWho = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
