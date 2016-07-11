package com.classic.car.utils;

import android.app.Activity;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.classic.car.R;
import com.classic.core.utils.NetworkUtil;
import com.classic.core.utils.ToastUtil;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.utils
 *
 * 文件描述：TODO
 * 创 建 人：续写经典
 * 创建时间：16/7/9 下午4:00
 */
public final class PgyerUtil {

    public static void checkUpdate(final Activity activity, final boolean showHint){
        if(!NetworkUtil.isNetworkAvailable(activity)){
            if(showHint){
                ToastUtil.showToast(activity, R.string.network_error);
            }
            return;
        }

        PgyUpdateManager.register(activity, new UpdateManagerListener() {
            @Override public void onUpdateAvailable(final String result) {
                final AppBean appBean = getAppBeanFromString(result);

                new MaterialDialog.Builder(activity)
                        .title(R.string.update_dialog_title)
                        .titleColorRes(R.color.primary_text)
                        .backgroundColorRes(R.color.white)
                        .content(appBean.getReleaseNote())
                        .contentColorRes(R.color.primary_light)
                        .positiveText(R.string.update)
                        .negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override public void onClick(MaterialDialog dialog, DialogAction which) {
                                startDownloadTask(activity, appBean.getDownloadURL());
                            }
                        }).show();
            }

            @Override public void onNoUpdateAvailable() {
                if (showHint) {
                    ToastUtil.showToast(activity, R.string.no_update);
                }
            }
        });
    }
}
