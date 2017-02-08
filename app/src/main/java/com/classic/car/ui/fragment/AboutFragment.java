package com.classic.car.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.classic.android.consts.MIME;
import com.classic.android.permissions.AfterPermissionGranted;
import com.classic.android.permissions.EasyPermissions;
import com.classic.car.R;
import com.classic.car.consts.Consts;
import com.classic.car.ui.activity.ThanksActivity;
import com.classic.car.ui.base.AppBaseFragment;
import com.classic.car.ui.dialog.AuthorDialog;
import com.classic.car.utils.PgyUtil;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
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
    private static final int    REQUEST_CODE_FEEDBACK = 201;
    private static final String FEEDBACK_PERMISSION   = Manifest.permission.RECORD_AUDIO;

    @BindView(R.id.about_version) TextView mVersion;
    @BindView(R.id.about_update)  TextView mUpdate;

    private AuthorDialog mAuthorDialog;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override public int getLayoutResId() {
        return R.layout.fragment_about;
    }

    @Override public void initView(View parentView, Bundle savedInstanceState) {
        super.initView(parentView, savedInstanceState);

        mVersion.setText(getString(R.string.about_version, getVersionName(mAppContext)));
        PgyUtil.setDialogStyle("#3F51B5", "#FFFFFF");
        addSubscription(RxView.clicks(mUpdate)
                              .throttleFirst(Consts.SHIELD_TIME, TimeUnit.SECONDS)
                              .subscribe(new Action1<Void>() {
                                  @Override public void call(Void aVoid) {
                                      PgyUtil.checkUpdate(mActivity, true);
                                  }
                              }));
    }

    @OnClick({R.id.about_feedback, R.id.about_author, R.id.about_thanks, R.id.about_share}) public void onClick(
            View view) {
        switch (view.getId()) {
            case R.id.about_feedback:
                checkRecordAudioPermissions();
                break;
            case R.id.about_author:
                if (null == mAuthorDialog) {
                    mAuthorDialog = new AuthorDialog(mActivity);
                }
                mAuthorDialog.show();
                break;
            case R.id.about_share:
                shareText(mActivity, getString(R.string.share_title), getString(R.string.share_subject),
                          getString(R.string.share_content));
                break;
            case R.id.about_thanks:
                ThanksActivity.start(mActivity);
                break;
        }
    }

    @Override public void onPause() {
        super.onPause();
        if (null != mAuthorDialog && mAuthorDialog.isShowing()) {
            mAuthorDialog.dismiss();
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_FEEDBACK) private void checkRecordAudioPermissions() {
        if (EasyPermissions.hasPermissions(mAppContext, FEEDBACK_PERMISSION)) {
            PgyUtil.feedback(mActivity);
        } else {
            EasyPermissions.requestPermissions(this, Consts.FEEDBACK_PERMISSIONS_DESCRIBE, REQUEST_CODE_FEEDBACK,
                                               FEEDBACK_PERMISSION);
        }
    }

    @Override public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        if (requestCode == REQUEST_CODE_FEEDBACK) {
            PgyUtil.feedback(mActivity);
        }
    }

    @Override public void onPermissionsDenied(int requestCode, List<String> perms) {
        super.onPermissionsDenied(requestCode, perms);
        if (requestCode == REQUEST_CODE_FEEDBACK) {
            PgyUtil.feedback(mActivity);
        }
    }

    private void shareText(@NonNull Context context, @NonNull String title, @NonNull String subject,
                           @NonNull String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(MIME.TEXT);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, title));
    }

    private String getVersionName(@NonNull Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            if (null != info) {
                return info.versionName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
