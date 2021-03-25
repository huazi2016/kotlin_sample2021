package com.test.base.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

/**
 * author : huazi
 * time   : 2020/9/7
 * desc   :处理view相关工具类
 */
object ViewUtil {

    /**
     * 设置指定控件的margin值
     */
    fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = view.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

    fun dp2px(dpValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}

/**
 * 当前 View 是否被覆盖
 */
fun View.isViewCovered(): Boolean {

    /**
     * 获取 View 在父布局中的位置
     */
    fun indexOfViewInParent(view: View, parent: ViewGroup): Int {
        var index = 0
        while (index < parent.childCount) {
            if (parent.getChildAt(index) === view)
                break
            index++
        }
        return index
    }

    var currentView = this

    val currentViewRect = Rect()
    val partVisible = currentView.getGlobalVisibleRect(currentViewRect)
    val totalHeightVisible = currentViewRect.bottom - currentViewRect.top >= this.measuredHeight
    val totalWidthVisible = currentViewRect.right - currentViewRect.left >= this.measuredWidth
    val totalViewVisible = partVisible && totalHeightVisible && totalWidthVisible
    if (!totalViewVisible) {
        // 如果视图的任何部分被其父视图的任何部分剪切，返回true
        return true
    }

    while (currentView.parent is ViewGroup) {
        val currentParent = currentView.parent as ViewGroup
        if (currentParent.visibility != View.VISIBLE) {
            // 如果视图的父视图不可见，则返回true
            return true
        }

        val start = indexOfViewInParent(currentView, currentParent)
        for (i in start + 1 until currentParent.childCount) {
            val viewRect = Rect()
            this.getGlobalVisibleRect(viewRect)
            val otherView = currentParent.getChildAt(i)
            val otherViewRect = Rect()
            otherView.getGlobalVisibleRect(otherViewRect)
            if (Rect.intersects(viewRect, otherViewRect)) {
                // 如果视图与其哥哥相交(已覆盖)，返回true
                return true
            }
        }
        currentView = currentParent
    }
    return false
}

/**
 * 设置视图可见
 */
fun setViewVisible(view: View?) {
    view?.apply {
        this.visibility = View.VISIBLE
    }
}

/**
 * 设置视图不可见
 */
fun setViewGone(view: View?) {
    view?.apply {
        this.visibility = View.GONE
    }
}

/**
 * 设置视图不可见--占位
 */
fun setViewInvisible(view: View?) {
    view?.apply {
        this.visibility = View.INVISIBLE
    }
}

/**
 * 比赛详情--dota饼图
 */
fun setTextViewDrawable(textView: TextView, drawableId: Int) {
    //顶部加图片
    setTextViewDrawable(textView, drawableId, 0, 0, 2)
}

/**
 * 比赛详情--装备列表
 */
fun setTextViewDrawable02(textView: TextView, drawableId: Int) {
    //左边加图片
    setTextViewDrawable(textView, drawableId, 0, 2, 1)
}

/**
 * 比赛详情--战队数据
 */
fun setTextViewDrawable03(textView: TextView, drawableId: Int) {
    //右边加图片
    setTextViewDrawable(textView, drawableId, 0, 5, 3)
}

/**
 * Textview设置图片
 *
 * @param context      上下文
 * @param textView     文本控件
 * @param drawableId01 第1个资源图片id
 * @param drawableId02 第2个资源图片id
 * @param space        间距
 * @param direction    设置图片位置 1,左 2,上 3,右 4,下 5,左右 6,上下
 */
fun setTextViewDrawable(textView: TextView, drawableId01: Int, drawableId02: Int, space: Int, direction: Int) {
    var dra01: Drawable? = null
    var dra02: Drawable? = null
    //文字和图片的间距
    val spaceVal: Int = dip2pxToInt(space)
    if (direction in 1..4) {
        if (drawableId01 <= 0) {
            return
        }
        dra01 = ResourceUtil.getDrawable(drawableId01)
        //必须设置宽高, 否则无法显示
        dra01?.apply {
            this.setBounds(0, 0, this.minimumWidth, this.minimumHeight)
        }
    }
    if (direction in 5..6) {
        if (drawableId01 <= 0 || drawableId02 <= 0) {
            return
        }
        dra01 = ResourceUtil.getDrawable(drawableId01)
        dra02 = ResourceUtil.getDrawable(drawableId02)
        //必须设置宽高, 否则无法显示
        dra01?.apply {
            this.setBounds(0, 0, this.minimumWidth, this.minimumHeight)
        }
        dra02?.apply {
            this.setBounds(0, 0, this.minimumWidth, this.minimumHeight)
        }
    }
    when (direction) {
        1 -> {
            textView.setCompoundDrawables(dra01, null, null, null)
            textView.compoundDrawablePadding = spaceVal
        }
        2 -> {
            textView.setCompoundDrawables(null, dra01, null, null)
            textView.compoundDrawablePadding = spaceVal
        }
        3 -> {
            textView.setCompoundDrawables(null, null, dra01, null)
            textView.compoundDrawablePadding = spaceVal
        }
        4 -> {
            textView.setCompoundDrawables(null, null, null, dra01)
            textView.compoundDrawablePadding = spaceVal
        }
        5 -> {
            textView.setCompoundDrawables(dra01, null, dra02, null)
            textView.compoundDrawablePadding = spaceVal
        }
        6 -> {
            textView.setCompoundDrawables(null, dra01, null, dra02)
            textView.compoundDrawablePadding = spaceVal
        }
        else -> {
            textView.setCompoundDrawables(null, null, null, null)
        }
    }
}

/**
 * 隐藏软键盘
 */
fun View?.hideSoftKeyboard() {
    if (this == null) {
        return
    }
    this.clearFocus()
    (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(this.windowToken, 0)
}

///**
// * 快速点击处理
// * - 这里的 View 只用于快速点击判断
// *
// * @param onClick 点击回调
// * @param interval 点击间隔时间，单位：ms，默认：[DEFAULT_CLICK_THROTTLE_MS]
// */
//@JvmOverloads
//inline fun View.disposeThrottleClick(onClick: () -> Unit, interval: Long = DEFAULT_CLICK_THROTTLE_MS) {
//    if (interval > 0) {
//        val lastTime = (this.getTag(R.id.base_view_click_tag) as? Long) ?: 0L
//        val currentTime = System.currentTimeMillis()
//        if (currentTime - lastTime > interval) {
//            onClick.invoke()
//            this.setTag(R.id.base_view_click_tag, currentTime)
//        }
//    } else {
//        onClick.invoke()
//    }
//}
//
///**
// * 设置点击事件
// * - 在间隔时间内点击事件拦截
// *
// * @param onClick 点击回调
// * @param interval 点击间隔时间，单位：ms，默认：[DEFAULT_CLICK_THROTTLE_MS]
// */
//@JvmOverloads
//inline fun View.setOnThrottleClickListener(crossinline onClick: () -> Unit, interval: Long = DEFAULT_CLICK_THROTTLE_MS) {
//    this.setOnClickListener {
//        this.disposeThrottleClick({
//                                      onClick.invoke()
//                                  }, interval)
//    }
//}
//
///***
// * 防止快速点击
// */
//fun View.click(listener: (view: View) -> Unit) {
//    val minTime = DEFAULT_CLICK_THROTTLE_MS
//    var lastTime = 0L
//    this.setOnClickListener {
//        val tmpTime = System.currentTimeMillis()
//        if (tmpTime - lastTime > minTime) {
//            lastTime = tmpTime
//            listener.invoke(this)
//        } else {
//            Log.d("click", "点击过快，取消触发")
//        }
//    }
//}
