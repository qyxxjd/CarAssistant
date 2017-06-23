package com.classic.car.ui.base;

import android.os.Bundle;
import android.view.View;

import com.classic.android.base.RxFragment;

import butterknife.ButterKnife;

public abstract class AppBaseFragment extends RxFragment {

    @Override public void initView(View parentView, Bundle savedInstanceState) {
        super.initView(parentView, savedInstanceState);
        ButterKnife.bind(this, parentView);
    }
}
