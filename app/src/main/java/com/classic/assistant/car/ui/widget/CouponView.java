package com.classic.assistant.car.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import com.classic.assistant.car.R;

import java.util.Locale;

/**
 * <a href="https://github.com/dongjunkun/CouponView">https://github.com/dongjunkun/CouponView</a>
 */
@SuppressWarnings("unused")
public class CouponView extends FrameLayout {

    private static final int DEFAULT_SEMICIRCLE_GAP = 4;
    private static final int DEFAULT_SEMICIRCLE_RADIUS = 4;
    private static final int DEFAULT_SEMICIRCLE_COLOR = 0xFFFFFFFF;

    //半圆画笔
    private final Paint semicirclePaint;

    //半圆之间间距
    private float semicircleGap;

    //半圆半径
    private float semicircleRadius;

    //半圆颜色
    private int semicircleColor;

    //半圆数量X
    private int semicircleNumX;

    //半圆数量Y
    private int semicircleNumY;

    //绘制半圆曲线后X轴剩余距离
    private int remindSemicircleX;

    //绘制半圆曲线后Y轴剩余距离
    private int remindSemicircleY;

    //开启顶部半圆曲线
    private boolean isSemicircleTop = true;

    //开启底部半圆曲线
    private boolean isSemicircleBottom = true;

    //开启左边半圆曲线
    private boolean isSemicircleLeft = false;

    //开启右边半圆曲线
    private boolean isSemicircleRight = false;

    //view宽度
    private int viewWidth;

    //view的高度
    private int viewHeight;


    public CouponView(Context context) {
        this(context, null);
    }

    public CouponView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CouponView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CouponView, defStyle, 0);
        semicircleRadius = a.getDimensionPixelSize(R.styleable.CouponView_cv_semicircle_radius, dp2Px(DEFAULT_SEMICIRCLE_RADIUS));
        semicircleGap = a.getDimensionPixelSize(R.styleable.CouponView_cv_semicircle_gap, dp2Px(DEFAULT_SEMICIRCLE_GAP));
        semicircleColor = a.getColor(R.styleable.CouponView_cv_semicircle_color, DEFAULT_SEMICIRCLE_COLOR);
        isSemicircleTop = a.getBoolean(R.styleable.CouponView_cv_is_semicircle_top, isSemicircleTop);
        isSemicircleBottom = a.getBoolean(R.styleable.CouponView_cv_is_semicircle_bottom, isSemicircleBottom);
        isSemicircleLeft = a.getBoolean(R.styleable.CouponView_cv_is_semicircle_left, isSemicircleLeft);
        isSemicircleRight = a.getBoolean(R.styleable.CouponView_cv_is_semicircle_right, isSemicircleRight);
        a.recycle();

        semicirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        semicirclePaint.setDither(true);
        semicirclePaint.setColor(semicircleColor);
        semicirclePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);
        Log.d("CouponView", String.format(Locale.CHINA, "onSizeChanged --> width: %d / %d, height: %d / %d, oldWidth: %d, oldHeight: %d", w, getWidth(), h, getHeight(), oldWidth, oldHeight));
        viewWidth = w;
        viewHeight = h;
        calculate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final float offsetX = remindSemicircleX / 2F;
        final float offsetY = remindSemicircleY / 2F;
        if (isSemicircleTop)
            for (int i = 0; i < semicircleNumX; i++) {
                float x = semicircleGap + semicircleRadius + offsetX + (semicircleGap + semicircleRadius * 2) * i;
                canvas.drawCircle(x, 0, semicircleRadius, semicirclePaint);
            }
        if (isSemicircleBottom)
            for (int i = 0; i < semicircleNumX; i++) {
                float x = semicircleGap + semicircleRadius + offsetX + (semicircleGap + semicircleRadius * 2) * i;
                canvas.drawCircle(x, viewHeight, semicircleRadius, semicirclePaint);
            }
        if (isSemicircleLeft)
            for (int i = 0; i < semicircleNumY; i++) {
                float y = semicircleGap + semicircleRadius + offsetY + (semicircleGap + semicircleRadius * 2) * i;
                canvas.drawCircle(0, y, semicircleRadius, semicirclePaint);
            }
        if (isSemicircleRight)
            for (int i = 0; i < semicircleNumY; i++) {
                float y = semicircleGap + semicircleRadius + offsetY + (semicircleGap + semicircleRadius * 2) * i;
                canvas.drawCircle(viewWidth, y, semicircleRadius, semicirclePaint);
            }
    }

    private void calculate() {
        if (isSemicircleTop || isSemicircleBottom) {
            remindSemicircleX = (int) ((viewWidth - semicircleGap) % (2 * semicircleRadius + semicircleGap));
            semicircleNumX = (int) ((viewWidth - semicircleGap) / (2 * semicircleRadius + semicircleGap));
        }

        if (isSemicircleLeft || isSemicircleRight) {
            remindSemicircleY = (int) ((viewHeight - semicircleGap) % (2 * semicircleRadius + semicircleGap));
            semicircleNumY = (int) ((viewHeight - semicircleGap) / (2 * semicircleRadius + semicircleGap));
        }
    }

    private int dp2Px(float dp) {
        return (int) (dp * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    private int px2Dp(float px) {
        return (int) (px / getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    public float getSemicircleGap() {
        return px2Dp(semicircleGap);
    }

    public void setSemicircleGap(float semicircleGap) {
        if (this.semicircleGap != semicircleGap) {
            this.semicircleGap = semicircleGap;
            calculate();
            invalidate();
        }
    }

    public float getSemicircleRadius() {
        return px2Dp(semicircleRadius);
    }

    public void setSemicircleRadius(float semicircleRadius) {
        if (this.semicircleRadius != semicircleRadius) {
            this.semicircleRadius = semicircleRadius;
            calculate();
            invalidate();
        }
    }

    public int getSemicircleColor() {
        return semicircleColor;
    }

    public void setSemicircleColor(int semicircleColor) {
        if (this.semicircleColor != semicircleColor) {
            this.semicircleColor = semicircleColor;
            calculate();
            invalidate();
        }
    }

    public boolean isSemicircleTop() {
        return isSemicircleTop;
    }

    public void setSemicircleTop(boolean semicircleTop) {
        if (this.isSemicircleTop != semicircleTop) {
            isSemicircleTop = semicircleTop;
            calculate();
            invalidate();
        }
    }

    public boolean isSemicircleBottom() {
        return isSemicircleBottom;
    }

    public void setSemicircleBottom(boolean semicircleBottom) {
        if (isSemicircleBottom != semicircleBottom) {
            isSemicircleBottom = semicircleBottom;
            calculate();
            invalidate();
        }
    }

    public boolean isSemicircleLeft() {
        return isSemicircleLeft;
    }

    public void setSemicircleLeft(boolean semicircleLeft) {
        if (isSemicircleLeft != semicircleLeft) {
            isSemicircleLeft = semicircleLeft;
            calculate();
            invalidate();
        }
    }

    public boolean isSemicircleRight() {
        return isSemicircleRight;
    }

    public void setSemicircleRight(boolean semicircleRight) {
        if (isSemicircleRight != semicircleRight) {
            isSemicircleRight = semicircleRight;
            calculate();
            invalidate();
        }
    }
}
