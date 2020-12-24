package com.classic.assistant.car.ui.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import com.amap.api.location.AMapLocationListener
import com.classic.assistant.car.R
import com.classic.assistant.car.data.manager.BackupManager
import com.classic.assistant.car.data.manager.DataManager
import com.classic.assistant.car.data.manager.LocationManager
import com.classic.assistant.car.data.source.DataSource
import com.classic.assistant.car.data.source.local.db.ConsumptionInfo
import com.classic.assistant.car.databinding.ActivityAddBinding
import com.classic.assistant.car.extension.*
import com.classic.assistant.car.ui.base.AppViewBindingActivity
import com.classic.assistant.car.util.CarLog
import com.classic.assistant.car.util.CharFilter
import com.classic.assistant.car.util.DatePickerUtil

/**
 * - 加油、保养时，需要填写: 里程表
 * - 需要动态设置标签的类型: 汽车保险、汽车配件、汽车购买、装潢饰品
 * - 有定位权限、并且定位成功时，展示位置信息。添加数据后、关闭页面时销毁资源
 */
class AddActivity : AppViewBindingActivity<ActivityAddBinding>(), Toolbar.OnMenuItemClickListener {
    private val textChooseColumnCount = 2
    private var isModify = false
    private var target: ConsumptionInfo = ConsumptionInfo()
    private var tagHintResId = 0
    private var dataSource: DataSource? = null
    private var fm: FragmentManager? = null



    private val locationListener = AMapLocationListener {
        if (isFinishing) return@AMapLocationListener
        val address = it.address
        CarLog.d("定位成功：$address")
        uiTask {
            viewBinding?.includeAddContent?.addConsumptionLocation?.let { v ->
                target.location = address
                setupText(v, address)
                v.applyFocus()
                showToast(appContext, R.string.hint_location_success)
            }
        }
    }

    private val location: LocationManager by lazy { LocationManager.get() }

    override fun createViewBinding() {
        viewBinding = ActivityAddBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataSource = DataSource.get(appContext)
        toolbar?.setOnMenuItemClickListener(this)
        if (intent.hasExtra(KEY_CONSUMPTION)) {
            target = intent.getSerializableExtra(KEY_CONSUMPTION) as ConsumptionInfo
            isModify = true
            setTitle(R.string.modify)
        } else {
            isModify = false
            target.time = System.currentTimeMillis()
            setTitle(R.string.add)
        }

        viewBinding?.includeAddContent?.apply {
            addConsumptionTimeLayout.setOnClickListener { showDatePicker() }
            addConsumptionTypeLayout.setOnClickListener { showTypeChoose() }
            contentOilLayout.setOnClickListener { showOilTypeChoose() }
            applyFilter(addConsumptionTag, addConsumptionLocation, addConsumptionRemark)
        }

        setupUI(target)
        fm = supportFragmentManager
        location.init(appContext)
    }

    override fun onDestroy() {
        location.stop()
        location.destroy()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(if (isModify) R.menu.modify_menu else R.menu.add_menu, menu)
        return true
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_location -> {
                task { location.start(locationListener) }
                return true
            }
            R.id.action_add -> {
                if (checkParams()) {
                    onAdd()
                    return true
                }
            }
            R.id.action_modify -> {
                if (checkParams()) {
                    onModify()
                    return true
                }
            }
        }
        return false
    }

    private fun showDatePicker() {
        DatePickerUtil.create(this, target.time, { date, _ ->
                val time = date.time
                target.time = time
                setupTime(time)
            }).show()
    }
    private fun showTypeChoose() {
        if (null != fm) {
            TextChooseFragment()
                .title(string(R.string.hint_add_consumption_type))
                .columnCount(textChooseColumnCount)
                .items(ConsumptionInfo.TYPE_LABELS)
                .listener(object : TextChooseListener {
                    override fun onChoose(index: Int, text: String) {
                        CarLog.d("消费类型选择：index=$index, text=$text")
                        onTypeChanged(index, text)
                    }
                })
                .show(fm!!)
        }
    }
    private fun showOilTypeChoose() {
        fm?.apply {
            TextChooseFragment()
                .title(string(R.string.hint_add_consumption_oil_type))
                .columnCount(textChooseColumnCount)
                .items(ConsumptionInfo.FUEL_LABELS)
                .listener(object : TextChooseListener {
                    override fun onChoose(index: Int, text: String) {
                        CarLog.d("加油类型选择：index=$index, text=$text")
                        onOilTypeChanged(index, text)
                    }
                })
                .show(fm!!)
        }
    }

    private fun onTypeChanged(newType: Int, value: String) {
        target.type = newType
        setupTypeValue(value)
        visibleTagLayout(newType)
        visibleOilLayout(target)
        visibleMileageLayout(newType)
    }
    private fun onOilTypeChanged(newType: Int, value: String) {
        target.oilType = newType
        viewBinding?.includeAddContent?.contentOilTypeValue?.text = value
    }

    private fun checkParams(): Boolean {
        viewBinding?.includeAddContent?.apply {
            if (addConsumptionAmount.text().isEmpty()) {
                hintToast(R.string.hint_add_consumption_amount)
                addConsumptionAmount.applyFocus()
                return false
            }
            if (tagHintResId != 0 && addConsumptionTag.text().isEmpty()) {
                hintToast(tagHintResId)
                addConsumptionTag.applyFocus()
                return false
            }
            if (useFuel(target.type) && contentOilPrice.text().isEmpty()) {
                hintToast(R.string.hint_add_consumption_oil_price)
                contentOilPrice.applyFocus()
                return false
            }
            if (useMileage(target.type) && addConsumptionMileage.text().isEmpty()) {
                hintToast(R.string.hint_add_consumption_mileage)
                addConsumptionMileage.applyFocus()
                return false
            }
        }
        return true
    }
    private fun hintToast(resId: Int) {
        toast(string(resId) + string(R.string.hint_empty_suffix))
    }

    private fun setupUI(info: ConsumptionInfo) {
        val type = info.type
        setupTime(info.time)
        setupTypeValue(UIManager.label(info.type))

        viewBinding?.includeAddContent?.apply {
            setupNumber(addConsumptionAmount, info.amount)
            if (useTag(type)) setupText(addConsumptionTag, info.tag)
            if (info.mileage > 0L) setupText(addConsumptionMileage, info.mileage.toString())
            if (info.location.isNotEmpty()) setupText(addConsumptionLocation, info.location)
            if (info.remark.isNotEmpty()) setupText(addConsumptionRemark, info.remark)

            visibleTagLayout(type)
            visibleOilLayout(info)
            visibleMileageLayout(type)
            addConsumptionAmount.applyFocus()
        }
    }
    private fun setupTypeValue(text: String) {
        viewBinding?.includeAddContent?.addConsumptionTypeValue?.text = text
    }
    private fun setupText(view: EditText, text: String) {
        view.setText(text)
    }
    private fun setupNumber(view: EditText, number: Float) {
        view.setText(if (number > 0F) number.replace() else "")
    }
    private fun setupTime(time: Long) {
        viewBinding?.includeAddContent?.addConsumptionTime?.text = time.format()
    }

    /**
     * 加油信息
     */
    private fun visibleOilLayout(info: ConsumptionInfo) {
        viewBinding?.includeAddContent?.apply {
            if (useFuel(info.type)) {
                contentOilLayout.visible()
                contentOilTypeValue.text = ConsumptionInfo.FUEL_LABELS[info.oilType]
                setupNumber(contentOilPrice, info.oilPrice)
            } else {
                contentOilLayout.gone()
            }
        }
    }

    /**
     * 加油、保养时，显示里程信息
     */
    private fun visibleMileageLayout(type: Int) {
        viewBinding?.includeAddContent?.addConsumptionMileage?.apply {
            if (useMileage(type)) visible() else gone()
        }
    }

    /**
     * 汽车保险、汽车配件、汽车购买、装潢饰品时，显示标签信息
     */
    private fun visibleTagLayout(type: Int) {
        tagHintResId = when (type) {
            // 汽车保险
            ConsumptionInfo.TYPE_PREMIUM -> R.string.hint_tag_premium
            // 汽车配件
            ConsumptionInfo.TYPE_ACCESSORIES -> R.string.hint_tag_accessories
            // 汽车购买
            ConsumptionInfo.TYPE_CAR_PURCHASE -> R.string.hint_tag_car_purchase
            // 装潢饰品
            ConsumptionInfo.TYPE_DECORATION -> R.string.hint_tag_decoration
            // 分期付款
            ConsumptionInfo.TYPE_INSTALLMENT -> R.string.hint_tag_installment
            else -> 0
        }
        viewBinding?.includeAddContent?.addConsumptionTag?.apply {
            if (tagHintResId != 0) {
                visible()
                hint = string(tagHintResId)
            } else gone()
        }
    }
    private fun useTag(type: Int): Boolean {
        return when (type) {
            ConsumptionInfo.TYPE_PREMIUM, ConsumptionInfo.TYPE_ACCESSORIES,
            ConsumptionInfo.TYPE_CAR_PURCHASE, ConsumptionInfo.TYPE_DECORATION,
            ConsumptionInfo.TYPE_INSTALLMENT -> true
            else -> false
        }
    }
    private fun useMileage(type: Int): Boolean {
        return when (type) {
            ConsumptionInfo.TYPE_FUEL, ConsumptionInfo.TYPE_MAINTENANCE -> true
            else -> false
        }
    }
    private fun useFuel(type: Int): Boolean {
        return when (type) {
            ConsumptionInfo.TYPE_FUEL -> true
            else -> false
        }
    }

    private fun onSaveBefore() {
        viewBinding?.includeAddContent?.apply {
            target.amount = addConsumptionAmount.float()
            if (tagHintResId != 0) target.tag = addConsumptionTag.text()
            if (useFuel(target.type)) target.oilPrice = contentOilPrice.float()
            if (useMileage(target.type)) target.mileage = addConsumptionMileage.long()
            target.location = addConsumptionLocation.text()
            target.remark = addConsumptionRemark.text()
        }
    }

    private fun onAdd() {
        onSaveBefore()
        val time = System.currentTimeMillis()
        target.createTime = time
        target.lastUpdateTime = time
        async {
            try {
                dataSource?.add(target)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } ui {
            if (null != it && it > 0L) {
                target.id = it
                // 数据备份
                onBackupItem(target)
                DataManager.get().dispatchAdd(target)
                toast(R.string.hint_add_success)
                finish()
            } else {
                toast(R.string.hint_add_failure)
            }
        }
    }

    private fun onModify() {
        onSaveBefore()
        target.lastUpdateTime = System.currentTimeMillis()

        async {
            try {
                dataSource?.modify(target)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } ui {
            if (null != it && it > 0) {
                // 数据备份
                onBackupItem(target)
                DataManager.get().dispatchUpdate(target)
                toast(R.string.hint_modify_success)
                finish()
            } else {
                toast(R.string.hint_modify_failure)
            }
        }
    }

    private fun onBackupItem(item: ConsumptionInfo) {
        ioTask { BackupManager.append(item) }
    }

    private fun applyFilter(vararg views: EditText) {
        val filters = arrayOf(CharFilter.defaultFilter())
        views.forEach { it.filters = filters }
    }

    companion object {
        private const val KEY_CONSUMPTION = "consumption"

        fun start(context: Context, data: ConsumptionInfo? = null) {
            val intent = Intent(context, AddActivity::class.java)
            if (null != data) intent.putExtra(KEY_CONSUMPTION, data)
            context.startActivity(intent)
        }
    }


}
