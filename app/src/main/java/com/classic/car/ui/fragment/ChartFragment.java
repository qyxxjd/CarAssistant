package com.classic.car.ui.fragment;

import android.os.Bundle;
import android.view.View;
import com.classic.car.R;
import com.classic.core.fragment.BaseFragment;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.ui.fragment
 *
 * 文件描述：TODO
 * 创 建 人：续写经典
 * 创建时间：16/5/29 下午2:21
 */
public class ChartFragment extends BaseFragment {

    public static ChartFragment newInstance() {
        return new ChartFragment();
    }
    @Override public int getLayoutResId() {
        return R.layout.fragment_chart;
    }

    @Override public void initView(View parentView, Bundle savedInstanceState) {
        super.initView(parentView, savedInstanceState);
    }

}
