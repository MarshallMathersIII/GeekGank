package com.eminem.geekgank.fragment;

import com.eminem.geekgank.constant.Constant;

/**
 * Created by Eminem on 2016/6/24.
 */
public class VideoFragment extends BaseFragment {

    private int page = 1;
    @Override
    public String getUrl() {
        return  Constant.ARTICLE_DATA + Constant.VIDEO + Constant.COUNT + page;
    }
}
