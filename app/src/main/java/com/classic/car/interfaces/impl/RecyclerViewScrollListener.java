package com.classic.car.interfaces.impl;

import android.support.v7.widget.RecyclerView;
import com.classic.car.interfaces.IScrollListener;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.interfaces.impl
 *
 * 文件描述: TODO
 * 创 建 人: 刘宾
 * 创建时间: 2016/6/23 15:33
 */
public abstract class RecyclerViewScrollListener extends RecyclerView.OnScrollListener implements IScrollListener {
    private int mScrollThreshold;

    @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if(Math.abs(dy) > mScrollThreshold){
            if(dy > 0){
                onScrollUp();
            } else {
                onScrollDown();
            }
        }
    }

    /**
     * 设置滚动阈值，超过这个值隐藏toolbar
     * @param scrollThreshold
     */
    public void setScrollThreshold(int scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }
}
