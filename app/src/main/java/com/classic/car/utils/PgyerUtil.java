package com.classic.car.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.classic.car.R;
import com.classic.core.utils.NetworkUtil;
import com.classic.core.utils.ToastUtil;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.feedback.PgyFeedback;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.views.PgyerDialog;
import java.lang.ref.WeakReference;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.utils
 *
 * 文件描述：版本更新
 * 创 建 人：续写经典
 * 创建时间：16/7/9 下午4:00
 */
public final class PgyerUtil {

    public static void checkUpdate(final Activity activity, final boolean showHint){
        if(!NetworkUtil.isNetworkAvailable(activity.getApplicationContext())){
            if(showHint){
                ToastUtil.showToast(activity.getApplicationContext(), R.string.network_error);
            }
            return;
        }
        final WeakReference<Activity> reference = new WeakReference<>(activity);
        PgyUpdateManager.register(activity, new UpdateManagerListener() {
            @Override public void onUpdateAvailable(final String result) {
                final Activity act = reference.get();
                if(null == act) { return; }
                final AppBean appBean = getAppBeanFromString(result);

                new MaterialDialog.Builder(act)
                        .title(R.string.update_dialog_title)
                        .titleColorRes(R.color.primary_text)
                        .backgroundColorRes(R.color.white)
                        .content(appBean.getReleaseNote())
                        .contentColorRes(R.color.primary_light)
                        .positiveText(R.string.update)
                        .negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override public void onClick(MaterialDialog dialog, DialogAction which) {
                                startDownloadTask(act, appBean.getDownloadURL());
                            }
                        }).show();
            }

            @Override public void onNoUpdateAvailable() {
                final Activity act = reference.get();
                if (null != act && showHint) {
                    ToastUtil.showToast(act.getApplicationContext(), R.string.no_update);
                }
            }
        });
    }

    public static void setDialogStyle(@NonNull String backgroundColor, @NonNull String textColor){
        PgyerDialog.setDialogTitleBackgroundColor(backgroundColor);
        PgyerDialog.setDialogTitleTextColor(textColor);
    }

    public static void feedback(Activity activity){
        WeakReference<Activity> reference = new WeakReference<>(activity);
        Activity act = reference.get();
        if(null == act) { return; }
        try {
            PgyFeedback.getInstance().showDialog(act);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void register(Context context){
        PgyCrashManager.register(context);
    }

    public static void destroy(){
        try{
            PgyUpdateManager.unregister();
            PgyCrashManager.unregister();
            PgyFeedbackShakeManager.unregister();
            PgyFeedback.getInstance().destroy();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
