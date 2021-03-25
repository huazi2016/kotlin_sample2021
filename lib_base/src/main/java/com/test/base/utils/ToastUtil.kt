package com.test.base.utils

import android.text.TextUtils
import android.widget.Toast.*

/**
 * author : huazi
 * time   : 2021/3/12
 * desc   :
 */
object ToastUtil {
}

/**
 * 显示Toast-Short
 * @param message 展示内容
 */
fun showToast(message: String?) {
    if (TextUtils.isEmpty(message)) {
        return
    }
    makeText(AppManager.getApplication(), message, LENGTH_SHORT).show()
}

/**
 * 显示Toast-Long
 * @param message 展示内容
 */
fun showLongToast(message: String?) {
    if (TextUtils.isEmpty(message)) {
        return
    }
    makeText(AppManager.getApplication(), message, LENGTH_LONG).show()
}

/**
 * 显示Toast-Short
 * @param stringId 展示内容
 */
fun showToast(stringId: Int) {
    makeText(AppManager.getApplication(), stringId, LENGTH_SHORT).show()
}

/**
 * 显示Toast-Long
 * @param stringId 展示内容
 */
fun showLongToast(stringId: Int) {
    makeText(AppManager.getApplication(), stringId, LENGTH_SHORT).show()
}

