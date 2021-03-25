package com.test.base.utils


/**
 * author : huazi
 * time   : 2020/12/2
 * desc   : 版本工具类
 */

/**
 * 获取app版本号
 */
fun getAPPVersionCode(): Int {
    val manager = AppManager.getContext().packageManager
    val packageName = AppManager.getContext().packageName
    val info = manager.getPackageInfo(packageName, 0)
    info?.apply {
        return info.versionCode
    }
    return -1
}

/**
 * 获取app版本名称
 */
fun getAPPVersionName(): String {
    val manager = AppManager.getContext().packageManager
    val packageName = AppManager.getContext().packageName
    val info = manager.getPackageInfo(packageName, 0)
    info?.apply {
        return info.versionName
    }
    return ""
}