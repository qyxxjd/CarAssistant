@file:Suppress("SpellCheckingInspection")

package com.classic.assistant.car.util
/*
 ~ MIT License
 ~ Copyright (c) 2018 Valentin Hinov
 ~ Permission is hereby granted, free of charge, to any person obtaining a copy
 ~ of this software and associated documentation files (the "Software"), to deal
 ~ in the Software without restriction, including without limitation the rights
 ~ to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 ~ copies of the Software, and to permit persons to whom the Software is
 ~ furnished to do so, subject to the following conditions:
 ~ The above copyright notice and this permission notice shall be included in all
 ~ copies or substantial portions of the Software.
 ~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 ~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 ~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 ~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 ~ SOFTWARE.
 */

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar
import kotlin.math.max
import kotlin.math.min

/**
 * [BottomNavigationBehavior](https://android.jlelse.eu/scroll-your-bottom-navigation-view-away-with-10-lines-of-code-346f1ed40e9e)
 */
class BottomNavigationBehavior<V : View>(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<V>(context, attrs) {

    @ViewCompat.NestedScrollType
    private var lastStartedType: Int = 0

    private var offsetAnimator: ValueAnimator? = null

    private var isSnappingEnabled = false

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        if (dependency is Snackbar.SnackbarLayout) {
            updateSnackbar(child, dependency)
        }
        return super.layoutDependsOn(parent, child, dependency)
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        if (axes != ViewCompat.SCROLL_AXIS_VERTICAL) return false
        lastStartedType = type
        offsetAnimator?.cancel()
        return true
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        child.translationY = max(0f, min(child.height.toFloat(), child.translationY + dy))
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, type: Int) {
        if (!isSnappingEnabled)
            return

        // add snap behaviour
        // Logic here borrowed from AppBarLayout onStopNestedScroll code
        if (lastStartedType == ViewCompat.TYPE_TOUCH || type == ViewCompat.TYPE_NON_TOUCH) {
            // find nearest seam
            val currTranslation = child.translationY
            val childHalfHeight = child.height * 0.5f

            // translate down
            if (currTranslation >= childHalfHeight) {
                animateBarVisibility(child, isVisible = false)
            }
            // translate up
            else {
                animateBarVisibility(child, isVisible = true)
            }
        }
    }

    private fun animateBarVisibility(child: View, isVisible: Boolean) {
        if (offsetAnimator == null) {
            offsetAnimator = ValueAnimator().apply {
                interpolator = DecelerateInterpolator()
                duration = 150L
            }

            offsetAnimator?.addUpdateListener {
                child.translationY = it.animatedValue as Float
            }
        } else {
            offsetAnimator?.cancel()
        }

        val targetTranslation = if (isVisible) 0f else child.height.toFloat()
        offsetAnimator?.setFloatValues(child.translationY, targetTranslation)
        offsetAnimator?.start()
    }

    private fun updateSnackbar(child: View, snackbarLayout: Snackbar.SnackbarLayout) {
        if (snackbarLayout.layoutParams is CoordinatorLayout.LayoutParams) {
            val params = snackbarLayout.layoutParams as CoordinatorLayout.LayoutParams

            params.anchorId = child.id
            params.anchorGravity = Gravity.TOP
            params.gravity = Gravity.TOP
            snackbarLayout.layoutParams = params
        }
    }
}
