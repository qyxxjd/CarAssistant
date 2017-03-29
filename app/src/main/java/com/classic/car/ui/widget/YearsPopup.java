package com.classic.car.ui.widget;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
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

        // Disable default animation for circular reveal
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setAnimationStyle(0);
        }
        ListView lv = (ListView)view.findViewById(R.id.popup_lv);
        lv.setOnItemClickListener(this);
        lv.setAdapter(new CommonAdapter<Integer>(mContext.get(), R.layout.popup_years_item, mYears) {
            @Override public void onUpdate(BaseAdapterHelper helper, Integer item, int position) {
                helper.setText(R.id.year, String.valueOf(item));
            }
        });
    }

    @Override
    public void showOnAnchor(@NonNull View anchor, int vPos, int hPos, int x, int y, boolean fitInScreen) {
        super.showOnAnchor(anchor, vPos, hPos, x, y, fitInScreen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            circularReveal(anchor);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void circularReveal(@NonNull final View anchor) {
        final View contentView = getContentView();
        contentView.post(new Runnable() {
            @Override
            public void run() {
                final int[] myLocation = new int[2];
                final int[] anchorLocation = new int[2];
                contentView.getLocationOnScreen(myLocation);
                anchor.getLocationOnScreen(anchorLocation);
                final int cx = anchorLocation[0] - myLocation[0] + anchor.getWidth()/2;
                final int cy = anchorLocation[1] - myLocation[1] + anchor.getHeight()/2;

                contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                final int dx = Math.max(cx, contentView.getMeasuredWidth() - cx);
                final int dy = Math.max(cy, contentView.getMeasuredHeight() - cy);
                final float finalRadius = (float) Math.hypot(dx, dy);
                Animator animator = ViewAnimationUtils.createCircularReveal(contentView, cx, cy, 0f, finalRadius);
                animator.setDuration(1000);
                animator.start();
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
