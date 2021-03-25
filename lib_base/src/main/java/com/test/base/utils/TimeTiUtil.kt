package com.test.base.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * author : huazi
 * time   : 2020/9/19
 * desc   : 日期与实践工具类
 */

const val TIME_STYLE01 = "yyyy年MM月dd日 HH:mm:ss"
const val TIME_STYLE17 = "yyyy年MM月dd日 HH:mm"
const val TIME_STYLE02 = "MM月dd日 HH:mm"
const val TIME_STYLE03 = "MM月dd日"
const val TIME_STYLE04 = "HH:mm:ss"
const val TIME_STYLE05 = "HH:mm"
const val TIME_STYLE06 = "mm:ss"

const val TIME_STYLE07 = "yyyy-MM-dd HH:mm:ss"
const val TIME_STYLE08 = "MM-dd HH:mm"
const val TIME_STYLE09 = "MM-dd"
const val TIME_STYLE16 = "yyyy-MM-dd"

const val TIME_STYLE10 = "yyyy.MM.dd HH:mm:ss"
const val TIME_STYLE11 = "MM.dd HH:mm"
const val TIME_STYLE12 = "MM.dd"

const val TIME_STYLE13 = "yyyy/MM/dd HH:mm:ss"
const val TIME_STYLE14 = "MM/dd HH:mm"
const val TIME_STYLE15 = "MM/dd"

object TimeTiUtil {

    /**
     * 获取时间戳的对应字符串 -- 秒
     * @param time 秒, *1000是毫秒
     * @return 当前格式的字符串
     */
    @SuppressWarnings("ALL")
    fun formatTime(time: Long, pattern: String): String {
        if (time <= 0) {
            return ""
        }
        return try {
            SimpleDateFormat(pattern).format(Date(time * 1000))
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 获取时间戳的对应字符串 -- 毫秒
     * @param time 秒, *1000是毫秒
     * @return 当前格式的字符串
     */
    @SuppressWarnings("ALL")
    fun formatTimeMilli(time: Long, pattern: String): String {
        if (time <= 0) {
            return ""
        }
        return try {
            SimpleDateFormat(pattern).format(Date(time))
        } catch (e: Exception) {
            ""
        }
    }

    @SuppressWarnings("ALL")
    fun formatTime(time: Date, pattern: String): String {
        return try {
            SimpleDateFormat(pattern).format(time)
        } catch (e: Exception) {
            ""
        }
    }

    fun getEverydayTime(time: Long): String {
        return getEverydayTime(time, TIME_STYLE03)
    }

    /**
     * 根据当前日期获取
     * @return finalTime 昨天、今天、明天, 09月21日 等等
     */
    @SuppressWarnings("ALL")
    fun getEverydayTime(time: Long, pattern: String): String {
        if (time <= 0) {
            return ""
        }
        //目标时间
        val targetCal = Calendar.getInstance()
        //秒-->毫秒
        targetCal.time = Date(time * 1000)
        //当前时间
        val currentCal = Calendar.getInstance()
        currentCal.time = Date(MmkvUtil.getServicesTime())
        //判断昨天、今天、明天等
        var finalTime = formatTime(time, pattern)
        if (targetCal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR) && targetCal.get(Calendar.MONTH)
            == currentCal.get(Calendar.MONTH)) {
            //根据计算结果赋值, 0今天, -1昨天, -2前天, -3明天
            when {
                targetCal.get(Calendar.DATE) - currentCal.get(Calendar.DATE) == -1 -> {
                    finalTime = spliceTimeText(time, pattern, "昨天")
                }
                targetCal.get(Calendar.DATE) - currentCal.get(Calendar.DATE) == -2 -> {
                    //finalTime = "前天"--暂时不用
                }
                targetCal.get(Calendar.DATE) - currentCal.get(Calendar.DATE) == 1 -> {
                    finalTime = spliceTimeText(time, pattern, "明天")
                }
                targetCal.get(Calendar.DATE) == currentCal.get(Calendar.DATE) -> {
                    finalTime = spliceTimeText(time, pattern, "今天")
                }
            }
        }
        return finalTime
    }

    /**
     * 新增日期或时间前缀
     */
    private fun spliceTimeText(time: Long, pattern: String, text: String): String {
        return when (pattern) {
            TIME_STYLE03 -> text
            else -> text + " " + formatTime(time, TIME_STYLE05)
        }
    }

//    /**
//     * 获取未来7天的日期, 拼接周几
//     * @return 09-22 周二
//     */
//    fun getNext7Days(): MutableList<MtTimeBo> {
//        val list = mutableListOf<MtTimeBo>()
//        var x = 0
//        while (x <= 6) {
//            when (x) {
//                0 -> {
//                    list.add(getSpecifiedDate(x))
//                }
//                1 -> {
//                    list.add(getSpecifiedDate(x))
//                }
//                2 -> {
//                    list.add(getSpecifiedDate(x))
//                }
//                3 -> {
//                    list.add(getSpecifiedDate(x))
//                }
//                4 -> {
//                    list.add(getSpecifiedDate(x))
//                }
//                5 -> {
//                    list.add(getSpecifiedDate(x))
//                }
//                6 -> {
//                    list.add(getSpecifiedDate(x))
//                }
//            }
//            x++
//        }
//        return list
//    }
//
//    /**
//     * 获取过去7天的日期, 拼接周几
//     * @return 09-22 周二
//     */
//    fun getPast7Days(): MutableList<MtTimeBo> {
//        val list = mutableListOf<MtTimeBo>()
//        var x = -6
//        while (x <= 0) {
//            when (x) {
//                0 -> {
//                    list.add(getSpecifiedDate(x))
//                }
//                -1 -> {
//                    list.add(getSpecifiedDate(x))
//                }
//                -2 -> {
//                    list.add(getSpecifiedDate(x))
//                }
//                -3 -> {
//                    list.add(getSpecifiedDate(x))
//                }
//                -4 -> {
//                    list.add(getSpecifiedDate(x))
//                }
//                -5 -> {
//                    list.add(getSpecifiedDate(x))
//                }
//                -6 -> {
//                    list.add(getSpecifiedDate(x))
//                }
//            }
//            x++
//        }
//        return list
//    }
//
//    /**
//     * 获取指定日期
//     * @param -1:昨天, 0:今天, 1:明天, 以此类推
//     * @return 09-22
//     */
//    @SuppressLint("NewApi")
//    fun getSpecifiedDate(index: Int): MtTimeBo {
//        val instance = Calendar.getInstance()
//        instance.time = Date(MmkvUtil.getServicesTime())
//        instance.add(Calendar.DATE, index)
//        //07-10
//        val style01 = formatTime(instance.time, TIME_STYLE09)
//        //2020-07-10
//        val style02 = formatTime(instance.time, TIME_STYLE16)
//        //周日, 周一...
//        val weekOfDate = getWeekOfDate(instance.time)
//        val timeBo = MtTimeBo()
//        //文字换行拼接\n
//        timeBo.showText = style01 + "\n" + weekOfDate
//        if (index == 0) {
//            timeBo.showText = "今天"
//        }
//        timeBo.startTime = style02
//        return timeBo
//    }

    /**
     * 获取指定日期的周几
     */
    private fun getWeekOfDate(time: Date): String {
        val weekArr = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")
        val instance = Calendar.getInstance()
        instance.time = time
        var index = instance.get(Calendar.DAY_OF_WEEK) - 1
        if (index < 0) {
            index = 0
        }
        return weekArr[index]
    }

    /**
     * 将未指定格式的字符串转换成日期类型
     * @param date - 20151123 或者 2015/11/23 或者2015-11-23
     * @return Mon Nov 23 00:00:00 GMT+08:00 2015
     */
    @Throws(ParseException::class)
    fun parseStringToDate(date: String): Date {
        var result: Date? = null
        var parse = date
        parse = parse.replaceFirst("^[0-9]{4}([^0-9]?)".toRegex(), "yyyy$1")
        parse = parse.replaceFirst("^[0-9]{2}([^0-9]?)".toRegex(), "yy$1")
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)".toRegex(), "$1MM$2")
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}( ?)".toRegex(), "$1dd$2")
        parse = parse.replaceFirst("( )[0-9]{1,2}([^0-9]?)".toRegex(), "$1HH$2")
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)".toRegex(), "$1mm$2")
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)".toRegex(), "$1ss$2")
        val format = SimpleDateFormat(parse, Locale.CHINA)
        result = format.parse(date)
        return result
    }

    /**
     * 判断是否同一天--秒(转换毫秒 *1000)
     */
    fun isSameDay(time01: Long, time02: Long): Boolean {
        if (time01 > 0 && time02 > 0) {
            val servicesTimeFormat = formatTimeMilli(time01 * 1000, TIME_STYLE16)
            val systemTime = formatTimeMilli(time02 * 1000, TIME_STYLE16)
            return servicesTimeFormat == systemTime
        }
        return false
    }

    /**
     * 判断是否同一天--秒(毫秒)
     */
    fun isSameDayMilli(time01: Long, time02: Long): Boolean {
        if (time01 > 0 && time02 > 0) {
            val servicesTimeFormat = formatTimeMilli(time01, TIME_STYLE16)
            val systemTime = formatTimeMilli(time02, TIME_STYLE16)
            return servicesTimeFormat == systemTime
        }
        return false
    }

    /**
     * 判断是否同一天--毫秒
     */
    fun isSameDayFromMillisecond(time01: Long, time02: Long): Boolean {
        if (time01 > 0 && time02 > 0) {
            val servicesTimeFormat = formatTimeMilli(time01, TIME_STYLE16)
            val systemTime = formatTimeMilli(time02, TIME_STYLE16)
            return servicesTimeFormat == systemTime
        }
        return false
    }

    /**
     * 转换时间格式为 04′27″
     */
    fun formatMinutesAndSeconds(time: Long): String {
        var min = (time / 60).toString()
        var second = (time % 60).toString()
        if (min.length < 2) {
            min = "0$min"
        }
        if (second.length < 2) {
            second = "0$second"
        }
        return "$min′$second″"
    }
}