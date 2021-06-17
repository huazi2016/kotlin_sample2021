package com.test.base.view.loadview

import android.content.Context
import android.view.View

/**
 * author : huazi
 * time   : 2021/3/26
 * desc   : 动态loading视图接口类
 */
interface IVaryViewHelper {

    fun getCurrentLayout(): View?

    fun restoreView()

    fun showLayout(view: View?)

    fun showLayout(layoutId: Int)

    fun inflate(layoutId: Int): View?

    fun getContext(): Context?

    fun getView(): View?
}