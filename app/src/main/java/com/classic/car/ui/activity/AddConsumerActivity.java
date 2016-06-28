package com.classic.car.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.classic.car.utils.Util;
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
    public static final int TYPE_ADD    = 0;
    public static final int TYPE_MODIFY = 1;

    private static final String TAG             = "datePicker";
    private static final String PARAMS_TYPE     = "type";
    private static final String PARAMS_CONSUMER = "consumerDetail";
    private static final String EMPTY           = "";

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
    private Integer            mType;
    private ConsumerDetail     mConsumerDetail;

    public static void start(Activity activity, int type, ConsumerDetail consumerDetail){
        Intent intent = new Intent(activity, AddConsumerActivity.class);
        intent.putExtra(PARAMS_TYPE, type);
        if(null != consumerDetail){
            intent.putExtra(PARAMS_CONSUMER, consumerDetail);
        }
        activity.startActivity(intent);
    }

    @Override public int getLayoutResId() {
        return R.layout.activity_add_consumer;
    }

    @Override protected boolean canBack() {
        return true;
    }

    @Override public void initView(Bundle savedInstanceState) {
        ((CarApplication)getApplicationContext()).getAppComponent().inject(this);
        super.initView(savedInstanceState);

        if(getIntent().hasExtra(PARAMS_TYPE)){
            mType = getIntent().getIntExtra(PARAMS_TYPE, TYPE_ADD);
        }
        if(getIntent().hasExtra(PARAMS_CONSUMER)){
            mConsumerDetail = (ConsumerDetail) getIntent().getSerializableExtra(PARAMS_CONSUMER);
        }
        if(null == mType || (mType == TYPE_MODIFY && null == mConsumerDetail)){
            finish();
            return;
        }

        getToolbar().setOnMenuItemClickListener(this);
        setTitle(mType == TYPE_ADD ? R.string.add_consumer_title : R.string.modify_consumer_title);
        mSpinner.setItems(Consts.TYPE_MENUS);
        mFuelSpinner.setItems(Consts.FUEL_MENUS);
        mSpinner.setOnItemSelectedListener(this);
        initValues();
    }

    private void initValues(){
        if(mType == TYPE_ADD){
            mSpinner.setSelectedIndex(Consts.TYPE_FUEL);
            mFuelSpinner.setSelectedIndex(Consts.FUEL_GASOLINE_92);
        }else if(mType == TYPE_MODIFY){
            mSelectCalendar = Calendar.getInstance();
            mSelectCalendar.setTimeInMillis(mConsumerDetail.getConsumptionTime());
            mSpinner.setSelectedIndex(mConsumerDetail.getType());
            mConsumerTime.setText(DateUtil.formatDate(DateUtil.FORMAT_DATE, mConsumerDetail.getConsumptionTime()));
            Util.setText(mAddConsumerMoney, mConsumerDetail.getMoney());
            mAddConsumerNotes.setText(TextUtils.isEmpty(mConsumerDetail.getNotes()) ? EMPTY : mConsumerDetail.getNotes());
            setFuelViews(mConsumerDetail.getType() == Consts.TYPE_FUEL ? View.VISIBLE : View.GONE);
            if(mConsumerDetail.getType() == Consts.TYPE_FUEL){
                mFuelSpinner.setSelectedIndex(mConsumerDetail.getOilType());
                Util.setText(mAddConsumerUnitPrice, mConsumerDetail.getUnitPrice());
                Util.setText(mAddConsumerCurrentMileage, mConsumerDetail.getCurrentMileage());
            }
        }
    }

    private boolean verifyData(){
        if(mType == TYPE_ADD){
            mConsumerDetail = new ConsumerDetail();
        }
        mConsumerDetail.setType(mSpinner.getSelectedIndex());
        final String money = mAddConsumerMoney.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            ToastUtil.showToast(activity, R.string.consumer_money_hint);
            Util.setFocusable(mAddConsumerMoney);
            return false;
        }
        mConsumerDetail.setMoney(Float.valueOf(money));
        if (null == mSelectCalendar) {
            ToastUtil.showToast(activity, R.string.consumer_select_time_hint);
            return false;
        }
        mConsumerDetail.setConsumptionTime(mSelectCalendar.getTimeInMillis());
        if (mSpinner.getSelectedIndex() == Consts.TYPE_FUEL) {
            final String unitPrice = mAddConsumerUnitPrice.getText().toString().trim();
            if (TextUtils.isEmpty(unitPrice)){
                ToastUtil.showToast(activity, R.string.consumer_unit_price_hint);
                Util.setFocusable(mAddConsumerUnitPrice);
                return false;
            }
            final String currentMileage = mAddConsumerCurrentMileage.getText().toString().trim();
            if(TextUtils.isEmpty(currentMileage)){
                ToastUtil.showToast(activity, R.string.consumer_current_mileage_hint);
                Util.setFocusable(mAddConsumerCurrentMileage);
                return false;
            }
            mConsumerDetail.setOilType(mFuelSpinner.getSelectedIndex());
            mConsumerDetail.setUnitPrice(Float.valueOf(unitPrice));
            mConsumerDetail.setCurrentMileage(Long.valueOf(currentMileage));
        }
        final String notes = mAddConsumerNotes.getText().toString().trim();
        if(!TextUtils.isEmpty(notes)){
            mConsumerDetail.setNotes(notes);
        }
        return true;
    }

    private void addConsumer() {
        if(!verifyData()) {
            return;
        }
        if(mConsumerDao.insert(mConsumerDetail) > 0){
            ToastUtil.showToast(activity, R.string.add_consumer_success);
            reset();
        } else {
            ToastUtil.showToast(activity, R.string.add_consumer_fail);
        }
    }
    private void modifyConsumer() {
        if(!verifyData()) {
            return;
        }
        if (mConsumerDao.update(mConsumerDetail) > 0){
            ToastUtil.showToast(activity, R.string.modify_consumer_success);
            finish();
        } else {
            ToastUtil.showToast(activity, R.string.modify_consumer_fail);
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
        getMenuInflater().inflate(mType == TYPE_ADD ? R.menu.add_menu : R.menu.modify_menu, menu);
        return true;
    }

    @Override public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            addConsumer();
            return true;
        }else if(item.getItemId() == R.id.action_modify){
            modifyConsumer();
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

    private void reset() {
        mSpinner.setSelectedIndex(Consts.TYPE_FUEL);
        mFuelSpinner.setSelectedIndex(Consts.FUEL_GASOLINE_92);
        mAddConsumerMoney.setText(EMPTY);
        mAddConsumerUnitPrice.setText(EMPTY);
        mAddConsumerCurrentMileage.setText(EMPTY);
        mAddConsumerNotes.setText(EMPTY);
        mConsumerTime.setText(R.string.consumer_select_time_hint);
        setFuelViews(View.VISIBLE);
        Util.setFocusable(mAddConsumerMoney);
        mSelectCalendar = null;
    }

    @Override public void onCancel() {
    }

    @Override public void onFinish(Calendar calendar) {
        this.mSelectCalendar = calendar;
        mConsumerTime.setText(DateUtil.formatDate(DateUtil.FORMAT_DATE, calendar.getTimeInMillis()));
    }

}
