package com.classic.car.activity;

import android.os.Bundle;
import butterknife.BindView;
import com.classic.car.R;
import com.classic.car.base.ToolbarActivity;
import com.classic.car.consts.Consts;
import com.classic.core.utils.ToastUtil;
import com.jaredrummler.materialspinner.MaterialSpinner;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.activity
 *
 * 文件描述：添加消费信息
 * 创 建 人：续写经典
 * 创建时间：16/6/5 下午2:07
 */
public class AddConsumerActivity extends ToolbarActivity {
    @BindView(R.id.add_consumer_spinner) MaterialSpinner mSpinner;

    @Override public int getLayoutResId() {
        return R.layout.activity_add_consumer;
    }

    @Override protected boolean canBack() {
        return true;
    }

    @Override public void initInstanceState(Bundle savedInstanceState) {
        super.initInstanceState(savedInstanceState);
        mSpinner.setItems(Consts.TYPE_MENUS);
        mSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                ToastUtil.showToast(activity, "click:"+position);
            }
        });
    }
}
