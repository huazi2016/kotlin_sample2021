package com.test.base.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.Toast

/**
 * 获得屏幕相关数据的辅助类
 */
class AbScreenUtils private constructor() {
    companion object {
        private var mainHandler: Handler? = null
        fun hideBottomUIMenu(activity: Activity) {
            try {
                var uiFlags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION //布局隐藏导航
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //布局全屏
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //隐藏导航
                        or View.SYSTEM_UI_FLAG_FULLSCREEN //全屏
                        or View.SYSTEM_UI_FLAG_IMMERSIVE) //沉浸式系统

                //兼容性判断
                uiFlags = if (Build.VERSION.SDK_INT >= 19) {
                    uiFlags or 0x00001000
                } else {
                    uiFlags or View.SYSTEM_UI_FLAG_LOW_PROFILE
                }
                activity.window.decorView.systemUiVisibility = uiFlags
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * 获得屏幕高度
         *
         * @param context
         * @return
         */
        fun getScreenHeight(context: Context, isDp: Boolean): Int {
            var screenHeight = 0
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            wm.defaultDisplay.getMetrics(dm)
            val height = dm.heightPixels // 屏幕高度（像素）
            if (!isDp) {
                return height
            }
            val density = dm.density // 屏幕密度（0.75 / 1.0 / 1.5）
            screenHeight = (height / density).toInt() // 屏幕高度(dp)
            return screenHeight
        }

        fun getScreenWidth(context: Context, isDp: Boolean): Int {
            var screenWidth = 0
            val winWidth: Int
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val defaultDisplay = wm.defaultDisplay
            val point = Point()
            defaultDisplay.getSize(point)
            winWidth = if (point.x > point.y) {
                point.y
            } else {
                point.x
            }
            val dm = DisplayMetrics()
            wm.defaultDisplay.getMetrics(dm)
            if (!isDp) {
                return winWidth
            }
            val density = dm.density // 屏幕密度（0.75 / 1.0 / 1.5）
            screenWidth = (winWidth / density).toInt() // 屏幕高度(dp)
            return screenWidth
        }

        /**
         * 获得状态栏的高度
         *
         * @param context
         * @return
         */
        fun getStatusHeight(context: Context): Int {
            var statusHeight = -1
            try {
                val clazz = Class.forName("com.android.internal.R\$dimen")
                val `object` = clazz.newInstance()
                val height = clazz.getField("status_bar_height")[`object`].toString().toInt()
                statusHeight = context.resources.getDimensionPixelSize(height)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return statusHeight
        }

        fun getStatusHeight(context: Context, isDp: Boolean): Int {
            var statusHeight = -1
            try {
                val clazz = Class.forName("com.android.internal.R\$dimen")
                val `object` = clazz.newInstance()
                val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val dm = DisplayMetrics()
                wm.defaultDisplay.getMetrics(dm)
                val height = clazz.getField("status_bar_height")[`object`].toString().toInt()
                statusHeight = context.resources.getDimensionPixelSize(height)
                if (!isDp) {
                    return statusHeight
                }
                val density = dm.density // 屏幕密度（0.75 / 1.0 / 1.5）
                statusHeight = (statusHeight / density).toInt() // 屏幕高度(dp)
                return statusHeight
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return statusHeight
        }

        /**
         * dp转换成px
         */
        fun dp2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        fun showToast(context: Context?, msg: String?) {
            mainHandler = Handler(Looper.getMainLooper())
            execute(Runnable { Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() })
        }

        private fun execute(runnable: Runnable) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                runnable.run()
            } else {
                mainHandler!!.post(runnable)
            }
        }
    }

    init {
        /* cannot be instantiated */
        throw UnsupportedOperationException("cannot be instantiated")
    }
}