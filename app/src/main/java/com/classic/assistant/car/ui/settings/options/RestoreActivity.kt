package com.classic.assistant.car.ui.settings.options

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.classic.assistant.car.R
import com.classic.assistant.car.data.manager.BackupManager
import com.classic.assistant.car.data.manager.DataManager
import com.classic.assistant.car.data.manager.RestoreResult
import com.classic.assistant.car.databinding.ItemRestoreBinding
import com.classic.assistant.car.extension.applyLinearConfig
import com.classic.assistant.car.extension.async
import com.classic.assistant.car.extension.longToast
import com.classic.assistant.car.extension.showToast
import com.classic.assistant.car.ui.base.AppActivity
import com.classic.assistant.car.util.Util
import com.classic.file.choose.EasyFileChoose
import java.io.File
import java.io.FileFilter
import java.util.*

class RestoreActivity : AppActivity(), RestoreDataListener {

    private lateinit var restoreAdapter: RestoreAdapter

    override fun layout(): Int = R.layout.activity_restore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.setting_restore)
        val recyclerView = findViewById<RecyclerView>(R.id.restore_recycler_view)
        recyclerView.applyLinearConfig()
        restoreAdapter = RestoreAdapter(this)
        recyclerView.adapter = restoreAdapter
        loadFiles()
    }

    private fun loadFiles() {
        BackupManager.findDefaultBackupFiles().apply {
            if (this.isNotEmpty()) {
                Collections.sort(this, SEARCH_FILE_COMPARATOR)
                restoreAdapter.submitList(this)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (null != data && resultCode == Activity.RESULT_OK && requestCode == FILE_CHOOSER_CODE) {
            val path = EasyFileChoose.getPath(data)
            val file = File(path)
            if (path.isNotEmpty() && BackupManager.hasBackupFile(file)) {
                // ToastUtil.showToast(appContext, "select file："+path);
                restore(path)
            } else if (File(path).isDirectory) {
                showToast(appContext, R.string.choose_correct_backup_file)
            } else {
                showToast(appContext, R.string.data_restore_failure)
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRestore(file: File) {
        restore(file.absolutePath)
    }

    private fun restore(path: String) {
        async {
            val result: RestoreResult? = try {
                val file = File(path)
                BackupManager.restore(file)
            } catch (e: Exception) {
                Util.report(Exception("恢复数据异常：${e.message}"))
                null
            }
            result
        } ui {
            if (null == it) {
                showToast(appContext, R.string.data_restore_failure)
            } else {
                val insertRows = it.insert.size
                val updateRows = it.update.size
                val title = "新增${insertRows}条，更新${updateRows}条"
                appContext.longToast(title)
                if (insertRows > 0 || updateRows > 0) {
                    DataManager.get().dispatchRestore(it.insert, it.update)
                    finish()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.restore_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_manual_selection) {
            EasyFileChoose.setTitle(R.string.hint_select_backup_file)
                .setFileFilter(BackupFileFilter())
                .setComparator(SDCARD_FILE_COMPARATOR)
                .choose(this, FILE_CHOOSER_CODE)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 备份文件过滤器
     *
     * 仅显示：
     * - 非空的目录
     * - 备份文件
     */
    class BackupFileFilter : FileFilter {
        override fun accept(file: File): Boolean {
            // 非空的目录
            val isNotEmptyDir = file.isDirectory && null != file.listFiles() && file.listFiles()!!.isNotEmpty()
            // 不显示隐藏文件夹
            return !file.name.startsWith(".") &&
                    (isNotEmptyDir || BackupManager.hasBackupFile(file))
        }
    }

    companion object {
        private val TAG = RestoreActivity::class.java.simpleName
        private const val FILE_CHOOSER_CODE = 1001

        fun start(context: Context) {
            context.startActivity(Intent(context, RestoreActivity::class.java))
        }

        private val SEARCH_FILE_COMPARATOR = object : Comparator<File> {
            override fun compare(o1: File, o2: File): Int {
                if (BackupManager.hasAutoBackupFile(o1)) {
                    return -1
                }
                if (BackupManager.hasAutoBackupFile(o2)) {
                    return 1
                }
                if (o1.length() == o2.length()) {
                    return 0
                }
                return if (o1.length() > o2.length()) -1 else 1
            }
        }

        private val SDCARD_FILE_COMPARATOR = object : Comparator<File> {
            override fun compare(o1: File, o2: File): Int {
                if (Const.ROOT_DIR == o1.name) return -1
                if (Const.ROOT_DIR == o2.name) return 1
                if (o1.isDirectory && o2.isFile) return -1
                if (o1.isFile && o2.isDirectory) return 1
                return o1.name.toLowerCase(Locale.CHINA).compareTo(o2.name.toLowerCase(Locale.CHINA))
            }
        }
    }
}

/**
 * 恢复数据页面Item
 */
class RestoreItemViewModel(val file: File) : ViewModel() {
    private val backupFile = BackupManager.convert(file)

    val date = ObservableField<String>(backupFile.date)
    val size = ObservableField<String>(backupFile.size)
    val rows = ObservableField<String>(backupFile.rows)
    val path = ObservableField<String>(backupFile.path)
    val label = ObservableField<String>(backupFile.label)
}

/**
 * 恢复数据监听接口
 */
interface RestoreDataListener {
    /**
     * 恢复数据
     */
    fun onRestore(file: File)
}

/**
 * 恢复数据页面适配器
 */
class RestoreAdapter(
    private val listener: RestoreDataListener
) : ListAdapter<File, RestoreAdapter.ViewHolder>(FileDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_restore, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { entity ->
            with(holder) {
                itemView.tag = entity
                bind(createOnClickListener(entity), entity)
            }
        }
    }

    class ViewHolder(
        private val binding: ItemRestoreBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(click: View.OnClickListener, item: File) {
            with(binding) {
                clickListener = click
                model = RestoreItemViewModel(item)
                executePendingBindings()
            }
        }
    }

    private fun createOnClickListener(file: File): View.OnClickListener {
        return View.OnClickListener {
            listener.onRestore(file)
        }
    }

    class FileDiffCallback : DiffUtil.ItemCallback<File>() {
        override fun areItemsTheSame(old: File, new: File): Boolean {
            return old.absolutePath == new.absolutePath
        }
        override fun areContentsTheSame(old: File, new: File): Boolean {
            return old.length() == new.length()
        }
    }
}
