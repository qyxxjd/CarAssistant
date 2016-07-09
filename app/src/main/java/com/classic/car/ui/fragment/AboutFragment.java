package com.classic.car.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.classic.car.R;
import com.classic.car.ui.activity.ThanksActivity;
import com.classic.car.ui.dialog.AuthorDialog;
import com.classic.core.fragment.BaseFragment;
import com.classic.core.utils.AppInfoUtil;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.ui.fragment
 *
 * 文件描述：关于页面
 * 创 建 人：续写经典
 * 创建时间：16/5/29 下午2:21
 */
public class AboutFragment extends BaseFragment {

    @BindView(R.id.about_version)  TextView mVersion;

    private AuthorDialog mAuthorDialog;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override public int getLayoutResId() {
        return R.layout.fragment_about;
    }

    @Override public void initView(View parentView, Bundle savedInstanceState) {
        super.initView(parentView, savedInstanceState);
        ButterKnife.bind(this, parentView);

        mVersion.setText(getString(R.string.about_version, AppInfoUtil.getVersionName(activity)));
    }

    @OnClick({ R.id.about_update, R.id.about_feedback, R.id.about_author, R.id.about_thanks })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_update:
                //PgyerUtil.checkUpdate(activity, true);
                break;
            case R.id.about_feedback:
                //PgyFeedback.getInstance().showDialog(activity);
                break;
            case R.id.about_author:
                if (null == mAuthorDialog) {
                    mAuthorDialog = new AuthorDialog(activity);
                }
                mAuthorDialog.show();
                break;
            case R.id.about_thanks:
                ThanksActivity.start(activity);
                break;
        }
    }

    @Override public void onPause() {
        super.onPause();
        if(null != mAuthorDialog && mAuthorDialog.isShowing()){
            mAuthorDialog.dismiss();
        }
    }
}
