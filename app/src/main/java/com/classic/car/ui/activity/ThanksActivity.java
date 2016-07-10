package com.classic.car.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.classic.car.R;
import com.classic.car.ui.base.AppBaseActivity;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.ui.activity
 *
 * 文件描述：TODO
 * 创 建 人：续写经典
 * 创建时间：16/7/9 下午2:07
 */
public class ThanksActivity extends AppBaseActivity {

    public static void start(Activity activity){
        activity.startActivity(new Intent(activity, ThanksActivity.class));
    }

    @Override public int getLayoutResId() {
        return R.layout.activity_thanks;
    }

    @Override public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setTitle(R.string.about_thanks);
    }

    @Override protected boolean canBack() {
        return true;
    }

}
