@file:JvmName("SoftKeyboardTools")

package com.test.base.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * 判断是否需要隐藏软键盘
 *
 * @param v 对比 View 对象
 * @param ev 触摸事件
 */
fun shouldHideInput(v: View?, ev: MotionEvent): Boolean {
    if (v is EditText) {
        // 是输入框
        val leftTop = intArrayOf(0, 0)
        // 获取输入框当前的位置
        v.getLocationInWindow(leftTop)
        val top = leftTop[1]
        val bottom = top + v.height
        // 触摸位置不在输入框范围内，需要隐藏
        return !(ev.y > top && ev.y < bottom)
    }
    return false
}

/**
 * 显示软键盘
 */
fun EditText.showSoftKeyboard() {
    this.isFocusable = true
    this.isFocusableInTouchMode = true
    this.requestFocus()
    (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
        this,
        InputMethodManager.SHOW_FORCED
    )
}

/**
 * 设置获取焦点是否弹起软键盘
 *
 * @param show 是否弹起
 */
fun EditText.showSoftInputOnFocus(show: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.showSoftInputOnFocus = show
    } else {
        try {
            val cls = EditText::class.java
            val setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", Boolean::class.javaPrimitiveType)
            setShowSoftInputOnFocus.isAccessible = false
            setShowSoftInputOnFocus.invoke(this, show)
        } catch (e: Exception) {
            //Log.e("Common_EditText ---->>", e.localizedMessage.orEmpty())
        }
    }
}

/**
 * 复制文本
 */
fun copyClipboardText(context: Context, copyText: String) {
    try {
        val boardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", copyText)
        boardManager.setPrimaryClip(clipData)
        showToast("复制完成")
    } catch (e: Exception) {
        showToast("复制失败")
    }
}