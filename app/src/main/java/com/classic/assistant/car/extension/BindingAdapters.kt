@file:Suppress("unused")

package com.classic.assistant.car.extension

import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("civ_border_color")
fun setBorderColor(view: CircleImageView, @ColorInt borderColor: Int) {
    view.borderColor = borderColor
}

@BindingAdapter("civ_circle_background_color")
fun setCircleBackgroundColor(view: CircleImageView, @ColorInt color: Int) {
    view.circleBackgroundColor = color
}
