package com.popkter.poplib.dialog

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.viewbinding.ViewBinding
import com.popkter.poplib.utils.doMeasure
import com.popkter.poplib.utils.getBinding
import com.popkter.poplib.utils.screenHeight
import com.popkter.poplib.utils.screenWidth
import com.popkter.poplib.utils.topActivity

abstract class PopDialog<T : ViewBinding> : PopupWindow() {
    private val _binding: T by lazy {
        getBinding(
            LayoutInflater.from(topActivity),
            topActivity.window.decorView as ViewGroup
        )
    }
    val binding: T
        get() = _binding

    companion object {
        private val TAG = "BaseDialog"
    }

    init {
        binding.root.also {
            it.doMeasure()
            contentView = it
            width = it.layoutParams.width
            height = it.layoutParams.height
        }
        isFocusable = false
        isOutsideTouchable = true
    }

    fun show() {
        initView()
        if (isShowing.not()) {
            val offsetX = (screenWidth - width) / 2
            val offsetY = (screenHeight - height) / 2
            showAtLocation(topActivity.window.decorView, Gravity.NO_GRAVITY, offsetX, offsetY)
        }
    }

    abstract fun initView()

}