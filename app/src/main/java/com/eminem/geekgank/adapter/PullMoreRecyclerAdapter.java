package com.eminem.geekgank.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eminem.geekgank.R;
import com.eminem.geekgank.app.App;
import com.eminem.geekgank.bean.Article;
import com.eminem.geekgank.utils.SharedPreferencesUtil;

import java.util.List;

/**
 * Created by Eminem on 2016/7/3.
 * 上拉加载的RecycleviewAdapter
 */
public class PullMoreRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_NORMAL_ITEM = 0;  //普通Item
    private static final int TYPE_FOOTER_ITEM = 1;  //底部FooterView

    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 1;
    //正在加载中
    public static final int LOADING_MORE = 2;
    //默认为0
    private int load_more_status = 1;

    private Context context;
    private List<Article.ResultsBean> list;

    public PullMoreRecyclerAdapter(Context context, List<Article.ResultsBean> list) {
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


    //创建新View，被LayoutManager所调用
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //如果viewType是普通item返回普通的布局，否则是底部布局并返回
        if (viewType == TYPE_NORMAL_ITEM) {
            View view = LayoutInflater.from(App.getContext()).inflate(R.layout.item_article, null, false);
            final NormalItmeViewHolder vh = new NormalItmeViewHolder(view);
            return vh;
        } else {
            View view = LayoutInflater.from(App.getContext()).inflate(R.layout.recycleview_footer_view, null, false);
            FooterViewHolder vh = new FooterViewHolder(view);
            return vh;
        }

    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
//        Article.ResultsBean result = list.get(position);数组下标越界

        if (viewHolder instanceof NormalItmeViewHolder) {
            //点击变色
            String ids = (String) SharedPreferencesUtil.get(App.getContext(), "read_ids", "");
            if (ids.contains(list.get(position).get_id())) {
                ( (NormalItmeViewHolder) viewHolder).tvTime.setTextColor(Color.GRAY);
                ( (NormalItmeViewHolder) viewHolder).tvTitle.setTextColor(Color.GRAY);
                ( (NormalItmeViewHolder) viewHolder).tvWho.setTextColor(Color.GRAY);
            } else {
                ( (NormalItmeViewHolder) viewHolder).tvTime.setTextColor(Color.BLACK);
                ( (NormalItmeViewHolder) viewHolder).tvTitle.setTextColor(Color.BLACK);
                ( (NormalItmeViewHolder) viewHolder).tvWho.setTextColor(Color.BLACK);
            }
            ((NormalItmeViewHolder) viewHolder).tvTitle.setText(list.get(position).getDesc());
            ((NormalItmeViewHolder) viewHolder).tvTime.setText(list.get(position).getPublishedAt().split("T")[0]);
            ((NormalItmeViewHolder) viewHolder).tvWho.setText(list.get(position).getWho());

            //点击事件
            if (mOnItemClickLitener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = viewHolder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(viewHolder.itemView, pos);
                    }
                });
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = viewHolder.getLayoutPosition();
                        mOnItemClickLitener.onItemLongClick(viewHolder.itemView, pos);
                        return false;
                    }
                });

            }
        } else if (viewHolder instanceof FooterViewHolder) {
            FooterViewHolder footViewHolder = (FooterViewHolder) viewHolder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.foot_view_item_tv.setVisibility(View.VISIBLE);
                    footViewHolder.foot_view_item_tv.setText("上拉加载更多");
                    footViewHolder.pb.setVisibility(View.GONE);
                    break;
                case LOADING_MORE:
                    footViewHolder.foot_view_item_tv.setVisibility(View.GONE);
                    footViewHolder.pb.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        //+1是加入底部的加载布局项
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // 如果position+1等于整个布局所有数总和就是底部布局
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER_ITEM;
        } else {
            return TYPE_NORMAL_ITEM;
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class NormalItmeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvWho, tvTime;

        public NormalItmeViewHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_art);
            tvWho = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    /**
     * 底部FooterView布局
     */
    private class FooterViewHolder extends RecyclerView.ViewHolder {
        public TextView foot_view_item_tv;
        public ProgressBar pb;

        public FooterViewHolder(View itemView) {
            super(itemView);
            pb = (ProgressBar) itemView.findViewById(R.id.progress_view);
            foot_view_item_tv = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    public void setMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }
}
