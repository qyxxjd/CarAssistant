package com.classic.assistant.car.ui.settings

import android.app.Activity
import android.view.View
import androidx.lifecycle.ViewModel
import com.classic.assistant.car.R
import com.classic.assistant.car.app.App
import com.classic.assistant.car.data.manager.BackupManager
import com.classic.assistant.car.extension.ioTask
import com.classic.assistant.car.extension.showToast
import com.classic.assistant.car.extension.string
import com.classic.assistant.car.extension.withUI
import com.classic.assistant.car.ui.settings.options.RestoreActivity
import com.pgyer.pgyersdk.PgyerSDKManager

class SettingsViewModel : ViewModel() {

    fun backup(view: View) {
        ioTask {
            val size = BackupManager.backupAll()
            withUI {
                showToast(view.context, App.get().context.string(R.string.format_backup_success, size))
            }
        }
    }

    fun openRestore(view: View) {
        RestoreActivity.start(view.context)
    }

    fun openDonation(view: View) {
        // DonationActivity.start(view.context)
        showToast(view.context, "功能开发中...")
    }

    fun showAuthorDialog(view: View) {
        // OpenSourceLicensesActivity.start(view.context)
        showToast(view.context, "功能开发中...")
    }

    fun openLicenses(view: View) {
        // OpenSourceLicensesActivity.start(view.context)
        showToast(view.context, "功能开发中...")
    }

    fun checkUpgrade(view: View) {
        PgyerSDKManager.checkSoftwareUpdate(view.context as Activity)
    }
}
