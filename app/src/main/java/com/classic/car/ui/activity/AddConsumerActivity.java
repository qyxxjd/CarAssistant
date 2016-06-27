package com.classic.car.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.classic.car.R;
import com.classic.car.app.CarApplication;
import com.classic.car.consts.Consts;
import com.classic.car.db.dao.ConsumerDao;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.ui.base.ToolbarActivity;
import com.classic.car.ui.fragment.DatePickerFragment;
import com.classic.core.utils.DateUtil;
import com.classic.core.utils.ToastUtil;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.util.Calendar;
import javax.inject.Inject;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.ui.activity
 *
 * 文件描述：添加消费信息
 * 创 建 人：续写经典
 * 创建时间：16/6/5 下午2:07
 */
public class AddConsumerActivity extends ToolbarActivity
        implements Toolbar.OnMenuItemClickListener, MaterialSpinner.OnItemSelectedListener,
                    DatePickerFragment.Callback{
    private static final String TAG   = "DatePicker";
    private static final String EMPTY = "";

    @BindView(R.id.add_consumer_fuel_layout)     View             mFuelLayout;
    @BindView(R.id.add_consumer_time)            TextView         mConsumerTime;
    @BindView(R.id.add_consumer_spinner)         MaterialSpinner  mSpinner;
    @BindView(R.id.add_consumer_fuel_spinner)    MaterialSpinner  mFuelSpinner;
    @BindView(R.id.add_consumer_money)           MaterialEditText mAddConsumerMoney;
    @BindView(R.id.add_consumer_unit_price)      MaterialEditText mAddConsumerUnitPrice;
    @BindView(R.id.add_consumer_current_mileage) MaterialEditText mAddConsumerCurrentMileage;
    @BindView(R.id.add_consumer_notes)           MaterialEditText mAddConsumerNotes;

    @Inject ConsumerDao        mConsumerDao;
    private DatePickerFragment mDatePickerFragment;
    private Calendar           mSelectCalendar;

    @Override public int getLayoutResId() {
        return R.layout.activity_add_consumer;
    }

    @Override protected boolean canBack() {
        return true;
    }

    @Override public void initView(Bundle savedInstanceState) {
        ((CarApplication)getApplicationContext()).getAppComponent().inject(this);
        super.initView(savedInstanceState);
        getToolbar().setOnMenuItemClickListener(this);
        setTitle(R.string.add_consumer_title);
        mSpinner.setItems(Consts.TYPE_MENUS);
        mSpinner.setSelectedIndex(Consts.TYPE_FUEL);
        mFuelSpinner.setItems(Consts.FUEL_MENUS);
        mFuelSpinner.setSelectedIndex(Consts.FUEL_GASOLINE_89);
        mSpinner.setOnItemSelectedListener(this);
    }

    private void reset() {
        mSpinner.setSelectedIndex(Consts.TYPE_FUEL);
        mFuelSpinner.setSelectedIndex(Consts.FUEL_GASOLINE_89);
        mAddConsumerMoney.setText(EMPTY);
        mAddConsumerUnitPrice.setText(EMPTY);
        mAddConsumerCurrentMileage.setText(EMPTY);
        mAddConsumerNotes.setText(EMPTY);
        mConsumerTime.setText(R.string.consumer_select_time_hint);
        setFuelViews(View.VISIBLE);
        setFocusable(mAddConsumerMoney);
        mSelectCalendar = null;
    }

    private void addConsumer() {
        final String money = mAddConsumerMoney.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            ToastUtil.showToast(activity, "消费金额不能为空");
            setFocusable(mAddConsumerMoney);
            return;
        }
        if (null == mSelectCalendar) {
            ToastUtil.showToast(activity, "请选择消费时间");
            return;
        }
        final ConsumerDetail consumerDetail = new ConsumerDetail(mSelectCalendar.getTimeInMillis(),
                Float.valueOf(money), mSpinner.getSelectedIndex());
        if (mSpinner.getSelectedIndex() == Consts.TYPE_FUEL) {
            final String unitPrice = mAddConsumerUnitPrice.getText().toString().trim();
            if (TextUtils.isEmpty(unitPrice)){
                ToastUtil.showToast(activity, "单价不能为空");
                setFocusable(mAddConsumerUnitPrice);
                return;
            }
            final String currentMileage = mAddConsumerCurrentMileage.getText().toString().trim();
            if(TextUtils.isEmpty(currentMileage)){
                ToastUtil.showToast(activity, "当前里程不能为空");
                setFocusable(mAddConsumerCurrentMileage);
                return;
            }
            consumerDetail.setOilType(mFuelSpinner.getSelectedIndex());
            consumerDetail.setUnitPrice(Float.valueOf(unitPrice));
            consumerDetail.setCurrentMileage(Long.valueOf(currentMileage));
        }
        final String notes = mAddConsumerNotes.getText().toString().trim();
        if(!TextUtils.isEmpty(notes)){
            consumerDetail.setNotes(notes);
        }
        if(mConsumerDao.insert(consumerDetail) > 0){
            ToastUtil.showToast(activity, "添加成功");
            reset();
        } else {
            ToastUtil.showToast(activity, "添加失败");
        }
    }

    @OnClick(R.id.add_consumer_time) void onTimeClick() {
        if (null == mDatePickerFragment) {
            mDatePickerFragment = DatePickerFragment.newInstance();
            mDatePickerFragment.setCallback(this);
        }
        mDatePickerFragment.show(getSupportFragmentManager(), TAG);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            addConsumer();
            return true;
        }
        return false;
    }

    @Override public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
        final int visibility = position == Consts.TYPE_FUEL ? View.VISIBLE : View.GONE;
        setFuelViews(visibility);
    }
    private void setFuelViews(int visibility){
        mAddConsumerUnitPrice.setVisibility(visibility);
        mAddConsumerCurrentMileage.setVisibility(visibility);
        mFuelLayout.setVisibility(visibility);
    }

    @Override public void onCancel() {
    }

    @Override public void onFinish(Calendar calendar) {
        this.mSelectCalendar = calendar;
        mConsumerTime.setText(DateUtil.formatDate(DateUtil.FORMAT_DATE, calendar.getTimeInMillis()));
    }

    private final void setFocusable(EditText editText){
        editText.setFocusable(true);
        editText.requestFocus();
    }
}
