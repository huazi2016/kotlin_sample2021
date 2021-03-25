@file:Suppress("unused")
@file:JvmName("BarTools")

package com.test.base.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.view.*
import android.widget.ProgressBar
import com.orhanobut.logger.Logger

/* ----------------------------------------------------------------------------------------- */
/* |                                   状态栏 导航栏相关                                     | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 获取状态栏高度
 *
 * @return 状态栏高度，单位：px
 */
fun getStatusBarHeight(): Int {
    val resources = Resources.getSystem()
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(resourceId)
}

/**
 * 判断是否显示状态栏
 *
 * @param activity [Activity] 对象
 * @return 是否显示状态栏
 */
fun isStatusBarVisible(activity: Activity): Boolean {
    return activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == 0
}

/**
 * 设置状态栏是否为浅色模式
 * - API 23 及其以上有效
 *
 * @param window [Window] 对象
 * @param isLightMode 是否是浅色模式
 */
fun setStatusBarLightMode(window: Window, isLightMode: Boolean) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        return
    }
    var vis = window.decorView.systemUiVisibility
    vis = if (isLightMode) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
        vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
    window.decorView.systemUiVisibility = vis
}

/**
 * 设置状态栏图标为深色和魅族特定的文字风格
 * - 魅族手机使用
 *
 * @param window [Window] 对象
 * @param isLightMode 是否是浅色模式
 */
fun setFlymeStatusBarLightMode(window: Window, isLightMode: Boolean) {
    try {
        val lp = window.attributes
        val darkFlag = WindowManager.LayoutParams::class.java
                .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
        val meizuFlags = WindowManager.LayoutParams::class.java
                .getDeclaredField("meizuFlags")
        darkFlag.isAccessible = true
        meizuFlags.isAccessible = true
        val bit = darkFlag.getInt(null)
        var value = meizuFlags.getInt(lp)
        value = if (isLightMode) {
            value or bit
        } else {
            value and bit.inv()
        }
        meizuFlags.setInt(lp, value)
        window.attributes = lp
    } catch (e: Exception) {
        Logger.e("Bar", "setFlymeStatusBarLightMode", e)
    }
}

/**
 * 获取导航栏高度
 *
 * @return 导航栏高度，单位：px
 */
fun getNavigationBarHeight(): Int {
    val res = Resources.getSystem()
    val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId != 0) {
        res.getDimensionPixelSize(resourceId)
    } else {
        0
    }
}

/**
 * 是否支持导航栏
 *
 * @param context [Context] 对象
 *
 * @return 是否支持
 */
@SuppressLint("ObsoleteSdkInt")
@JvmOverloads
fun isSupportNavigationBar(context: Context = AppManager.getContext()): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        
        val display = wm.defaultDisplay
        val size = Point()
        val realSize = Point()
        display.getSize(size)
        display.getRealSize(realSize)
        return realSize.y != size.y || realSize.x != size.x
    }
    val menu = ViewConfiguration.get(AppManager.getApplication()).hasPermanentMenuKey()
    val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
    return !menu && !back
}

/**
 * 判断导航栏是否显示
 *
 * @param activity [Activity] 对象
 *
 * @return 导航栏是否显示
 */
fun isNavigationBarVisible(activity: Activity): Boolean {
    val show: Boolean
    val display: Display = activity.window.windowManager.defaultDisplay
    val point = Point()
    display.getRealSize(point)
    
    val decorView: View = activity.window.decorView
    val conf: Configuration = activity.resources.configuration
    show = if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) {
        val contentView: View = decorView.findViewById(android.R.id.content)
        point.x != contentView.width
    } else {
        val rect = Rect()
        decorView.getWindowVisibleDisplayFrame(rect)
        rect.bottom != point.y
    }
    return show
}

/**
 * 隐藏导航栏
 *
 * @param window [Window] 对象
 */
fun hideNavigationBar(window: Window) {
    if (Build.VERSION.SDK_INT in Build.VERSION_CODES.HONEYCOMB_MR1..Build.VERSION_CODES.JELLY_BEAN_MR2) {
        // API 12 .. 18
        window.decorView.systemUiVisibility = View.GONE
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        // API 19 ..
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}

/**
 * 展示webView加载进度
 */
fun showWebViewProgress(progressBar: ProgressBar?, newProgress: Int) {
    if (progressBar == null) {
        return
    }
    if (newProgress == 100) {
        setViewGone(progressBar)
    } else {
        setViewVisible(progressBar)
        progressBar.progress = newProgress
    }
}