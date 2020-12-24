package com.classic.assistant.car.ui.widget.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public final class BottomNavigationBehavior
    extends CoordinatorLayout.Behavior<BottomNavigationView> {

    public BottomNavigationBehavior(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean layoutDependsOn(
        @Nullable CoordinatorLayout parent,
        @NonNull BottomNavigationView child,
        @Nullable View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            this.updateSnackBar(child, (Snackbar.SnackbarLayout) dependency);
        }

        assert parent != null;
        assert dependency != null;
        return super.layoutDependsOn(parent, child, dependency);
    }

    public boolean onStartNestedScroll(
        @NonNull CoordinatorLayout coordinatorLayout,
        @NonNull BottomNavigationView child,
        @NonNull View directTargetChild,
        @NonNull View target,
        int axes,
        int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    public void onNestedPreScroll(
        @NonNull CoordinatorLayout coordinatorLayout,
        @NonNull BottomNavigationView child,
        @NonNull View target,
        int dx,
        int dy,
        @NonNull int[] consumed,
        int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        child.setTranslationY(
            Math.max(0.0f, Math.min(child.getHeight(), child.getTranslationY() + dy)));
    }

    private void updateSnackBar(BottomNavigationView child, Snackbar.SnackbarLayout layout) {
        if (layout.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
            android.view.ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
            if (layoutParams == null) {
                throw new RuntimeException(
                    "null cannot be cast to non-null type android.support.design.widget.CoordinatorLayout.LayoutParams");
            }

            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) layoutParams;
            params.setAnchorId(child.getId());
            params.anchorGravity = Gravity.TOP;
            params.gravity = Gravity.TOP;
            layout.setLayoutParams(params);
        }
    }
}
