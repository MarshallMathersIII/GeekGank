package com.eminem.geekgank.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eminem.geekgank.R;
import com.eminem.geekgank.app.App;
import com.eminem.geekgank.bean.Meizi;
import com.eminem.geekgank.utils.ScreenUtil;
import com.eminem.geekgank.widget.RatioImageView;

import java.util.List;

/**
 * Created by Eminem on 2016/6/27.
 */
public class MeiziAdapter extends RecyclerView.Adapter<MeiziAdapter.MyViewHolder> {
    private Context context;
    private List<Meizi.ResultsBean> lists;
    private List<Integer> heights;

    public MeiziAdapter(Context context, List<Meizi.ResultsBean> list) {
        this.context = context;
        this.lists = list;
//       getRandomHeight(this.lists);
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

//    private void getRandomHeight(List<Meizi.ResultsBean> lists){//得到随机item的高度
//        heights = new ArrayList<>();
//        for (int i = 0; i < lists.size(); i++) {
//            heights.add((int)(200+Math.random()*400));
//        }
//    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder=new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_meizi, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        int screenHeight = ScreenUtil.getScreenHeight(App.getContext());
        int screenWidth = ScreenUtil.getScreenWidth(App.getContext());
        ViewGroup.LayoutParams params = holder.cardView.getLayoutParams();
        params.width=screenWidth/2;
        params.height=screenHeight/2;
        holder.cardView.setLayoutParams(params);
        Meizi.ResultsBean result = lists.get(position);

//        holder.imageView.setOriginalSize(image.getWidth(), image.getHeight());
        Glide.with(App.getContext())
                .load(result.getUrl())
//                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        ViewCompat.setTransitionName(holder.imageView, result.getUrl());

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RatioImageView imageView;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            imageView = (RatioImageView) itemView.findViewById(R.id.iv_meizi);
        }
    }
}
