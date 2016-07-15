package com.eminem.geekgank.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eminem.geekgank.R;
import com.eminem.geekgank.app.App;
import com.eminem.geekgank.bean.Article;
import com.eminem.geekgank.utils.SharedPreferencesUtil;

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
    //点击事件接口
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(View.inflate(context,R.layout.item_article,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Article.ResultsBean result = list.get(position);

        //点击变色
        String ids = (String) SharedPreferencesUtil.get(App.getContext(), "read_ids", "");
        if (ids.contains(list.get(position).get_id())) {
            holder.tvTime.setTextColor(Color.GRAY);
            holder.tvTitle.setTextColor(Color.GRAY);
            holder.tvWho.setTextColor(Color.GRAY);
        } else {
            holder.tvTime.setTextColor(Color.BLACK);
            holder.tvTitle.setTextColor(Color.BLACK);
            holder.tvWho.setTextColor(Color.BLACK);
        }
        //setData
        holder.tvTitle.setText(result.getDesc());
        holder.tvWho.setText(result.getWho());
        holder.tvTime.setText(result.getPublishedAt().split("T")[0]);
        //点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }

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
