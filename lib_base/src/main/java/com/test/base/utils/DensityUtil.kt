@file:Suppress("unused") @file:JvmName("DensityTools")

package com.test.base.utils

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.test.base.utils.ResourceUtil.getColorById
import com.test.base.utils.ResourceUtil.getStringById
import kotlin.math.roundToInt

/* ----------------------------------------------------------------------------------------- */
/* |                                        密度相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 获取设备屏幕宽度
 *
 * @param context [Context] 对象
 *
 * @return 屏幕宽度，单位：px
 */
@JvmOverloads
fun getDeviceScreenWidth(context: Context = AppManager.getContext()): Int {
    return context.resources.displayMetrics.widthPixels
}

/**
 * 获取设备屏幕高度
 *
 * @param context [Context] 对象
 *
 * @return 屏幕高度，单位：px
 */
@JvmOverloads
fun getDeviceScreenHeight(context: Context = AppManager.getContext()): Int {
    return context.resources.displayMetrics.heightPixels
}

/**
 * 将 dp 单位的值转换为 px 单位的值
 *
 * @param dpValue dp 值
 * @param context [Context] 对象
 *
 * @return dp 对应的 px 值
 */
@JvmOverloads
fun <N : Number> dip2px(dpValue: N, context: Context = AppManager.getContext()): Float {
    val density = context.resources.displayMetrics.density
    return dpValue.toFloat() * density
}

/**
 * 将 dp 单位的值转换为 px 单位的值
 *
 * @param dpValue dp 值
 * @param context [Context] 对象
 *
 * @return dp 对应的 px 值
 */
@JvmOverloads
fun <N : Number> dip2pxToInt(dpValue: N, context: Context = AppManager.getContext()): Int {
    val density = context.resources.displayMetrics.density
    return (dpValue.toFloat() * density).toInt()
}

/**
 * 将 px 单位的值转换为 dp 单位的值
 *
 * @param pxValue px 值
 * @param context [Context] 对象
 *
 * @return px 对应的 dp 值
 */
@JvmOverloads
fun <N : Number> px2dp(pxValue: N, context: Context = AppManager.getContext()): Float {
    val density = context.resources.displayMetrics.density
    return pxValue.toFloat() / density
}

/**
 * 将 px 单位的值转换为 dp 单位的值
 *
 * @param pxValue px 值
 * @param context [Context] 对象
 *
 * @return px 对应的 dp 值
 */
@JvmOverloads
fun <N : Number> px2dpi(pxValue: N, context: Context = AppManager.getContext()): Int {
    return px2dp(pxValue, context).roundToInt()
}

/**
 * 将 sp 单位的值转换为 px 单位的值
 *
 * @param spValue sp 值
 * @param context [Context] 对象
 *
 * @return 对应的 px 值
 */
@JvmOverloads
fun <N : Number> sp2px(spValue: N, context: Context = AppManager.getContext()): Float {
    val density = context.resources.displayMetrics.scaledDensity
    return spValue.toFloat() * density
}

/**
 * 将 px 单位的值转换为 sp 单位的值
 *
 * @param pxValue px 值
 * @param context [Context] 对象
 *
 * @return 对应的 sp 值
 */
@JvmOverloads
fun <N : Number> px2sp(pxValue: N, context: Context = AppManager.getContext()): Float {
    val density = context.resources.displayMetrics.scaledDensity
    return pxValue.toFloat() / density
}

/** 单位标记 - DP 单位 */
val Number.dp: Float
    get() = dip2px(this)

/** 单位标记 - SP */
val Number.sp: Float
    get() = sp2px(this)

/** 单位标记 - DP 单位 */
val Number.dpi: Int
    get() = dip2px(this).roundToInt()

/** String 字符串 */
val Int.string: String
    get() = getStringById(this)

/** 颜色值 */
val Int.color: Int
    get() = getColorById(this)

/**
 * 设置item均分
 * @param itemView 控件
 * @param headView 头部控件
 * @param size item个数
 */
fun setItemEqualShareDisplay(itemView: ConstraintLayout, headView: View, size: Int) {
    val layoutParams = itemView.layoutParams
    if (layoutParams != null) {
        //屏幕宽度-边距-头部宽度
        val screenWidth = getDeviceScreenWidth() - dip2pxToInt(50f) - headView.width
        //均分
        layoutParams.width = screenWidth / size
        itemView.layoutParams = layoutParams
    }
}

/**
 * 设置item均分--不要addHead
 * @param itemView 控件
 * @param size item个数
 */
fun setItemEqualShareDisplay(itemView: ConstraintLayout, size: Int) {
    val layoutParams = itemView.layoutParams
    if (layoutParams != null) {
        //屏幕宽度-边距-头部宽度
        val screenWidth = getDeviceScreenWidth() - dip2pxToInt(90f)
        //均分
        layoutParams.width = screenWidth / size
        itemView.layoutParams = layoutParams
    }
}
