package com.classic.car.ui.fragment;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.classic.car.R;
import com.classic.car.consts.Consts;
import com.classic.car.ui.activity.ThanksActivity;
import com.classic.car.ui.base.AppBaseFragment;
import com.classic.car.ui.dialog.AuthorDialog;
import com.classic.car.utils.PgyerUtil;
import com.classic.core.utils.AppInfoUtil;
import com.classic.core.utils.IntentUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.pgyersdk.feedback.PgyFeedback;
import com.pgyersdk.views.PgyerDialog;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.concurrent.TimeUnit;
import rx.functions.Action1;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.ui.fragment
 *
 * 文件描述：关于页面
 * 创 建 人：续写经典
 * 创建时间：16/5/29 下午2:21
 */
public class AboutFragment extends AppBaseFragment {

    @BindView(R.id.about_version)  TextView mVersion;
    @BindView(R.id.about_update)   TextView mUpdate;

    private AuthorDialog mAuthorDialog;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override public int getLayoutResId() {
        return R.layout.fragment_about;
    }

    @Override public void initView(View parentView, Bundle savedInstanceState) {
        super.initView(parentView, savedInstanceState);

        mVersion.setText(getString(R.string.about_version, AppInfoUtil.getVersionName(mAppContext)));
        PgyerDialog.setDialogTitleBackgroundColor("#3F51B5");
        PgyerDialog.setDialogTitleTextColor("#FFFFFF");
        addSubscription(
                RxView.clicks(mUpdate)
                      .throttleFirst(Consts.SHIELD_TIME, TimeUnit.SECONDS)
                      .subscribe(new Action1<Void>() {
                          @Override public void call(Void aVoid) {
                              PgyerUtil.checkUpdate(activity, true);
                          }
                      })
        );
    }

    @OnClick({ R.id.about_feedback, R.id.about_author, R.id.about_thanks, R.id.about_share })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_feedback:
                feedback();
                break;
            case R.id.about_author:
                if (null == mAuthorDialog) {
                    mAuthorDialog = new AuthorDialog(activity);
                }
                mAuthorDialog.show();
                break;
            case R.id.about_share:
                IntentUtil.shareText(activity, getString(R.string.share_title),
                        getString(R.string.share_subject),getString(R.string.share_content));
                break;
            case R.id.about_thanks:
                ThanksActivity.start(activity);
                break;
        }
    }

    private void feedback(){
        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            PgyFeedback.getInstance().showDialog(activity);
        } else {
            RxPermissions.getInstance(mAppContext)
                         .request(Manifest.permission.RECORD_AUDIO)
                         .subscribe(new Action1<Boolean>() {
                             @Override public void call(Boolean granted) {
                                 PgyFeedback.getInstance().showDialog(activity);
                             }
                         });
        }
    }

    @Override public void onPause() {
        super.onPause();
        if(null != mAuthorDialog && mAuthorDialog.isShowing()){
            mAuthorDialog.dismiss();
        }
    }
}
