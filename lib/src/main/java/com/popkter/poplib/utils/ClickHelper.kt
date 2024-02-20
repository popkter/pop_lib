package com.popkter.poplib.utils

import android.view.View


/**
 * 单击防抖，自定义延迟时间,默认200ms
 * [delayMills] 屏蔽时长
 * [onClick] 点击事件
 */
inline fun View.setOnSingleClickListener(delayMillis: Long = 200, crossinline onClick: () -> Unit) {
    this.setOnClickListener {
        isClickable = false
        onClick()
        postDelayed({ isClickable = true }, delayMillis)
    }
}

/**
 * 长按防抖，自定义延迟时间,默认200ms
 * [delayMills] 屏蔽时长
 * [onLongClick] 长按事件
 */
inline fun View.setOnLongSingleClickListener(delayMillis: Long = 200, crossinline onLongClick: () -> Boolean) {
    this.setOnLongClickListener {
        isClickable = false
        val result = onLongClick()
        postDelayed({ isClickable = true }, delayMillis)
        result
    }
}
