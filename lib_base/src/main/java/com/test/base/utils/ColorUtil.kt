package com.test.base.utils

import android.graphics.Color
import android.text.TextUtils
import com.orhanobut.logger.Logger

/**
 * author : huazi
 * time   : 2020/9/5
 * desc   :
 */
class ColorUtil {
    companion object {
        /**
         * 从argb返回颜色值
         * e.g  后台返回"rgba(255,255, 0, 0)" ,调用该方法可返回颜色值
         *
         * @param colorStr     e.g "rgba(255,255, 0, 0)"
         * @param defaultColor 默认的颜色
         * @return -1 ： 处理颜色失败，调用出需要判断
         */
        fun getColor(colorStr: String?, defaultColor: Int): Int {
            val colors: IntArray?
            try {
                colors = getColors(
                    colorStr)
                if (colors != null && colors.size == 4) {
                    return Color.argb(
                        colors[3] * 255,
                        colors[0],
                        colors[1],
                        colors[2]
                    )
                }
            } catch (e: Exception) {
                Logger.d("ColorUtils", "getColor error: " + e.message)
            }
            return AppManager.getApplication().resources.getColor(defaultColor)
        }

        /**
         * DES: 颜色rgba处理
         *
         * @param rgbas
         */
        fun getColors(rgbas: String?): IntArray? {
            // DES: 颜色数组
            var colors: IntArray? = null
            // DES: 过滤空串
            if (!TextUtils.isEmpty(rgbas)) {
                // DES: 初始化
                colors = intArrayOf(0, 0, 0, 0)
                // DES: 去除其它字符并按','号拆分
                val strs = rgbas!!.replace("rgba(", "")
                    .replace(")", "")
                    .replace(" ", "")
                    .split(",".toRegex()).toTypedArray()
                // DES: 替换颜色数组
                var i = 0
                while (i < colors.size && i < strs.size) {
                    colors[i] = strs[i].toInt()
                    i++
                }
            }
            return colors
        }
    }
}