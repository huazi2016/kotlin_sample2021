package com.test.base.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.FloatRange
import com.test.base.R

/**
 * author : huazi
 * time   : 2021/2/19
 * desc   : 状态栏工具类
 */
object StatusBarUtil {

    private const val MIN_API = 19

    /**
     * 沉浸式状态栏
     * @param window 当前窗口
     * @param color 色值
     * @param alpha 透明度
     */
    fun immersive(window: Window, color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = mixtureColor(color, alpha)
            var systemUiVisibility = window.decorView.systemUiVisibility
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.decorView.systemUiVisibility = systemUiVisibility
        } else if (Build.VERSION.SDK_INT >= MIN_API) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            setTranslucentView(window.decorView as ViewGroup, color, alpha)
        } else if (Build.VERSION.SDK_INT >= MIN_API && Build.VERSION.SDK_INT > 16) {
            var systemUiVisibility = window.decorView.systemUiVisibility
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.decorView.systemUiVisibility = systemUiVisibility
        }
    }

    /**
     * 创建假的透明栏
     */
    private fun setTranslucentView(container: ViewGroup, color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            val mixtureColor: Int = mixtureColor(color, alpha)
            var translucentView = container.findViewById<View>(R.id.custom)
            if (translucentView == null && mixtureColor != 0) {
                translucentView = View(container.context)
                translucentView.id = R.id.custom
                val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(container.context))
                container.addView(translucentView, lp)
            }
            translucentView?.setBackgroundColor(mixtureColor)
        }
    }

    /**
     * 获取色值
     */
    private fun mixtureColor(color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
        val a = if (color and -0x1000000 == 0) 0xff else color ushr 24
        return color and 0x00ffffff or ((a * alpha).toInt() shl 24)
    }

    /**
     * 获取状态栏高度
     */
    private fun getStatusBarHeight(context: Context): Int {
        var result = 24
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        result = if (resId > 0) {
            context.resources.getDimensionPixelSize(resId)
        } else {
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, result.toFloat(), Resources.getSystem().displayMetrics).toInt()
        }
        return result
    }
}