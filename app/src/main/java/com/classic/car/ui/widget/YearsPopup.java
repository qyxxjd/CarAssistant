package com.classic.car.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.classic.car.R;

import java.lang.ref.WeakReference;
import java.util.List;

public class YearsPopup extends RelativePopupWindow implements AdapterView.OnItemClickListener{

    private @VerticalPosition int   mVerticalPosition;
    private @HorizontalPosition int mHorizontalPosition;

    private WeakReference<Context> mContext;

    private List<Integer> mYears;
    private Listener      mListener;
    private boolean       mFitInScreen;

    private YearsPopup(Builder builder) {
        mVerticalPosition = builder.verticalPosition;
        mHorizontalPosition = builder.horizontalPosition;
        mYears = builder.years;
        mContext = builder.context;
        mFitInScreen = builder.fitInScreen;
        mListener = builder.listener;
        init();
    }

    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            mListener.onYearSelected(mYears.get(position));
            dismiss();
        }
    }

    public interface Listener {
        void onYearSelected(int year);
    }

    public void show(@NonNull View view) {
        if (null == mContext || null == mContext.get()) { return; }
        showOnAnchor(view, mVerticalPosition, mHorizontalPosition, mFitInScreen);
    }

    private void init() {
        if (null == mContext || null == mContext.get()) { return; }
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(mContext.get()).inflate(R.layout.popup_years, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ListView lv = view.findViewById(R.id.popup_lv);
        lv.setOnItemClickListener(this);
        lv.setAdapter(new CommonAdapter<Integer>(mContext.get(), R.layout.popup_years_item, mYears) {
            @Override public void onUpdate(BaseAdapterHelper helper, Integer item, int position) {
                helper.setText(R.id.year, String.valueOf(item));
            }
        });
    }

    public static final class Builder {
        private int           verticalPosition;
        private int           horizontalPosition;
        private List<Integer> years;
        private boolean       fitInScreen;
        private Listener      listener;

        private WeakReference<Context> context;

        public Builder() {}

        public Builder verticalPosition(int verticalPosition) {
            this.verticalPosition = verticalPosition;
            return this;
        }

        public Builder horizontalPosition(int horizontalPosition) {
            this.horizontalPosition = horizontalPosition;
            return this;
        }

        public Builder years(List<Integer> years) {
            this.years = years;
            return this;
        }

        public Builder context(Context context) {
            this.context = new WeakReference<>(context);
            return this;
        }

        @SuppressWarnings("SameParameterValue")
        public Builder fitInScreen(boolean fitInScreen) {
            this.fitInScreen = fitInScreen;
            return this;
        }

        public Builder listener(Listener listener) {
            this.listener = listener;
            return this;
        }

        public YearsPopup build() {
            return new YearsPopup(this);
        }

    }
}
