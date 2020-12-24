// package com.classic.assistant.car.util
//
// import android.content.Context
// import android.util.AttributeSet
// import android.view.View
// import android.view.animation.LinearInterpolator
// import androidx.coordinatorlayout.widget.CoordinatorLayout
// import androidx.core.view.ViewCompat
// import com.google.android.material.floatingactionbutton.FloatingActionButton
//
// /**
//  * TODO
//  *
//  * - [Link1](https://www.jianshu.com/p/04bd1d2223a7)
//  * - [Link2](https://newfivefour.com/android-coordinatorlayout-scrolling-hide-fab-behavior.html)
//  *
//  * @author Classic
//  * @version v1.0, 2019-05-23 15:53
//  */
//
// class FabScrollBehavior(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior() {
//
//     override fun onStartNestedScroll(
//         coordinatorLayout: CoordinatorLayout, child: FloatingActionButton,
//         directTargetChild: View, target: View, nestedScrollAxes: Int
//     ): Boolean {
//         // 确保滚动方向为垂直方向
//         return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
//     }
//
//     override fun onNestedScroll(
//         coordinatorLayout: CoordinatorLayout, child: FloatingActionButton,
//         target: View, dxConsumed: Int, dyConsumed: Int,
//         dxUnconsumed: Int, dyUnconsumed: Int
//     ) {
//         super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
//         if (dyConsumed > 0) { // 向下滑动
//             animateOut(child)
//         } else if (dyConsumed < 0) { // 向上滑动
//             animateIn(child)
//         }
//     }
//
//     // FAB移出屏幕动画（隐藏动画）
//     private fun animateOut(fab: FloatingActionButton) {
//         val layoutParams = fab.layoutParams as CoordinatorLayout.LayoutParams
//         val bottomMargin = layoutParams.bottomMargin
//         fab.animate().translationY((fab.height + bottomMargin).toFloat()).setInterpolator(LinearInterpolator()).start()
//     }
//
//     // FAB移入屏幕动画（显示动画）
//     private fun animateIn(fab: FloatingActionButton) {
//         fab.animate().translationY(0f).setInterpolator(LinearInterpolator()).start()
//     }
// }
