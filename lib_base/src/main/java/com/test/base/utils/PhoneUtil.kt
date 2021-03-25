package com.test.base.utils

import com.orhanobut.logger.Logger
import java.lang.Exception
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

/**
 * author : huazi
 * time   : 2020/12/24
 * desc   : 手机号处理工具类
 */
object PhoneUtil {

    /**
     * 检查手机号长度
     * @param phoneNum 手机号
     */
    fun checkLength(phoneNum: String): Boolean {
        return phoneNum.length < 11
    }

    /**
     * 大陆号码或香港号码均可
     */
    @Throws(PatternSyntaxException::class)
    fun isPhoneLegal(str: String?): Boolean {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str)
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 145,147,149
     * 15+除4的任意数(不要写^4，这样的话字母也会被认为是正确的)
     * 166
     * 17+3,5,6,7,8
     * 18+任意数
     * 198,199
     */
    @Throws(PatternSyntaxException::class)
    fun isChinaPhoneLegal(phoneNum: String?): Boolean {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        val regExp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" + "|(18[0-9])|(19[8,9]))\\d{8}$"
        val p: Pattern = Pattern.compile(regExp)
        val m: Matcher = p.matcher(phoneNum)
        val result = m.matches()
        return result
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    @Throws(PatternSyntaxException::class)
    fun isHKPhoneLegal(phoneNum: String?): Boolean {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        val regExp = "^(5|6|8|9)\\d{7}$"
        val p: Pattern = Pattern.compile(regExp)
        val m: Matcher = p.matcher(phoneNum)
        return m.matches()
    }
}

/**
 * 手机号中间4位打星号
 */
fun phoneMosaic(phone: String) : String {
    var resultPhone = phone
    try {
        if (phone.isNotEmpty()) {
            val asterisk = "****"
            resultPhone = phone.substring(0, 3) + asterisk + phone.substring(7, phone.length)
        }
    } catch (e: Exception) {
        Logger.d("phoneMosaic_error" + e.message)
    }
    return resultPhone
}