@file:Suppress("unused")
@file:JvmName("StringTools")

package com.test.base.utils

import android.graphics.Color
import android.text.Html
import android.text.Spanned
import com.orhanobut.logger.Logger

/* ----------------------------------------------------------------------------------------- */
/* |                                      字符串相关                                        | */
/* ----------------------------------------------------------------------------------------- */

/**
 * 解析 Html
 *
 * @param str html 文本
 *
 * @return 从 Html 解析出的 Spanned 对象
 */
fun parseHtmlFromString(str: String): Spanned? {
    @Suppress("DEPRECATION")
    return Html.fromHtml(str)
}

/**
 * 解析颜色
 *
 * @param str 颜色 文本
 *
 * @return 颜色值
 */
fun parseColorFromString(str: String): Int? {
    return try {
        Color.parseColor(str)
    } catch (e: Exception) {
        Logger.e("StringUtil.kt", e, "parseColor")
        null
    }
}

/**
 *  转换成整型,或者0
 */
fun String?.toIntOrZero():Int=this?.toIntOrNull() ?: 0

/**
 * 空字符串则返回默认值
 */
fun String?.or(s:String):String{
    return if(this.isNullOrEmpty()){
        s
    }else{
        this
    }
}