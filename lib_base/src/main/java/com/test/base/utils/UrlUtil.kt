package com.test.base.utils

import android.text.TextUtils
import java.lang.Exception

/**
 * author : huazi
 * time   : 2020/9/24
 * desc   : url工具类
 */
object UrlUtil {

    /**
     * 解析url 参数, 并做升序处理
     * @param url 地址
     * @param currentTimeMillis 当前时间戳
     * @return MD5加密串
     */
    fun urlParsing(url: String, currentTimeMillis: String): String? {
        var md5Text = ""
        try {
            val mapRequest = mutableMapOf("t" to currentTimeMillis)
            //val mapRequest = HashMap<String, String>()
            var arrSplit: Array<String>? = null
            //以?为分割点, 截取之后的参数串
            val strUrlParam = splitParam(url)
            //若未获取到参数, 直接返回t
            if (TextUtils.isEmpty(strUrlParam)) {
                return MD5Util.encryptSplicingHeader(currentTimeMillis)
            }
            //获取value放入map
            arrSplit = strUrlParam!!.split("[&]".toRegex()).toTypedArray()
            for (strSplit in arrSplit) {
                var arrSplitEqual: Array<String>? = null
                arrSplitEqual = strSplit.split("[=]".toRegex()).toTypedArray()
                //解析出键值
                if (arrSplitEqual.size > 1) {
                    //正确解析
                    mapRequest[arrSplitEqual[0]] = arrSplitEqual[1]
                } else {
                    if (arrSplitEqual[0] !== "") {
                        //只有参数没有值，不加入
                        mapRequest[arrSplitEqual[0]] = ""
                    }
                }
            }
            //按照ASCII码升序处理
            val sortMap = mapRequest.entries.sortedBy { it.key }.associateBy({ it.key }, { it.value })
            //拼接参数的value
            val splicingText = splicingMapValue(sortMap)
            //拼接加密key, 再次加密
            md5Text = MD5Util.encryptSplicingHeader(splicingText)!!
        } catch (e: Exception) {
            //Logger.e("urlParsing" + e.message)
        }
        return md5Text
    }

    /**
     * 分割?之后的参数
     */
    private fun splitParam(strURL: String): String? {
        if (!TextUtils.isEmpty(strURL)) {
            val arrSplit = strURL.trim { it <= ' ' }.toLowerCase().split("[?]".toRegex()).toTypedArray()
            if (arrSplit.size > 1) {
                return arrSplit[1]
            }
        }
        return null
    }
}