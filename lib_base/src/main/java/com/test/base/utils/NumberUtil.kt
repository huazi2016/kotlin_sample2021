package com.test.base.utils

import android.text.TextUtils
import com.orhanobut.logger.Logger
import java.lang.Exception
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * author : huazi
 * time   : 2020/9/21
 * desc   : 数字工具类
 */
object NumberUtil {

    /*** 1位 */
    const val NUM_FORMAT01 = "#.#"

    /*** 2位 */
    const val NUM_FORMAT02 = "#.##"

    /**
     * 大于1000的金额, 转成xxK
     */
    fun getNumUnit(value: Double): String {
        if (value >= 1000) {
            //只保留两位小数
            return clearNumLastZero(value / 1000, NUM_FORMAT01) + "K"
        }
        return clearNumLastZero(value, NUM_FORMAT01)
    }

    /**
     * 处理小数点的保留位数
     */
    fun clearNumLastZero(num: Double, pattern: String): String {
        val format = DecimalFormat(pattern)
        return format.format(num)
    }

    /**
     * 处理Float小数, 保留2位小数, 且去除后面的0
     */
    fun clearNumLastZero(num: Float): String {
        val format = DecimalFormat(NUM_FORMAT02)
        val result = format.format(num)
        return if (TextUtils.isEmpty(result)) {
            "0"
        } else {
            result
        }
    }

    /**
     * 保留1位小数, 若小数点后面为0, 直接去掉
     */
    fun oneDecimalPlaceClearZero(value: Float): String {
        try {
            var result = String.format("%.1f", value)
            if (result.indexOf(".") > 0) {
                //去掉多余的0
                result = result.replace("0+?$".toRegex(), "")
                //如最后一位是.则去掉
                result = result.replace("[.]$".toRegex(), "")
            }
            return result
        } catch (e: Exception) {
            Logger.e("ClearLastZero_error==" + e.message)
        }
        return ""
    }
}

/**
 * 保留小数类型, 返回float类型
 */
fun keepDecimalPlaces(num: Float, digits: Int) : Float {
    var result = num
    try {
        val bd = BigDecimal(num.toDouble())
        val value = bd.setScale(digits, BigDecimal.ROUND_HALF_UP)
        result = value.toFloat()
    } catch (e: Exception) {
        Logger.d("keepDecimalPlaces_error=" + e.message)
    }
    return result
}
