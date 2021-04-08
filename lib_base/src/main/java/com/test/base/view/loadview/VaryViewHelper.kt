package com.test.base.view.loadview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.base.R
import com.test.base.utils.setViewGone
import com.test.base.utils.setViewVisible

/**
 * author : huazi
 * time   : 2021/3/26
 * company：inkr
 * desc   : 动态loading视图帮助类
 */
class VaryViewHelper(view: View?) : IVaryViewHelper {

    private var mSourceView: View? = view
    private var parentView: ViewGroup? = null
    private var viewIndex = 0
    private var params: ViewGroup.LayoutParams? = null
    private var currentView: View? = null
    private var isWebView = false
    private var mLoadView: View? = null

    constructor(view: View, isWebView: Boolean) : this(view) {
        this.isWebView = isWebView
    }

    private fun initView() {
        if (mSourceView != null) {
            params = mSourceView!!.layoutParams
            if (mSourceView!!.parent != null) {
                parentView = mSourceView!!.parent as ViewGroup
            } else {
                parentView = mSourceView!!.rootView.findViewById(R.id.content)
            }
            if (parentView != null) {
                val childCount = parentView!!.childCount
                for (index in 0 until childCount) {
                    if (mSourceView === parentView!!.getChildAt(index)) {
                        viewIndex = index
                        break
                    }
                }
            }
            currentView = mSourceView
        }
    }

    override fun getCurrentLayout() = currentView

    /** 还原原来的布局*/
    override fun restoreView() {
        showLayout(mSourceView)
    }

    override fun showLayout(layoutId: Int) {
        showLayout(inflate(layoutId))
    }

    /** 动态处理view的展示*/
    override fun showLayout(view: View?) {
        if (parentView == null) {
            initView()
        }
        if (isWebView) {
            if (view !== mSourceView) {
                mLoadView = view
                parentView!!.addView(mLoadView, params)
                setViewVisible(mSourceView)
                setViewVisible(mLoadView)
            } else {
                setViewGone(mLoadView)
                setViewVisible(mSourceView)
            }
        } else {
            currentView = view
            if (parentView != null) {
                val childAt = parentView?.getChildAt(viewIndex)
                if (childAt != null && childAt !== view) {
                    if (view?.parent != null) {
                        val parent = view.parent as ViewGroup
                        parent.removeView(view)
                    }
                    parentView?.removeViewAt(viewIndex)
                    parentView?.addView(view, viewIndex, params)
                }
            }
        }
    }

    /** 填充布局*/
    override fun inflate(layoutId: Int): View? {
        return if (mSourceView != null) {
            LayoutInflater.from(mSourceView?.context).inflate(layoutId, null)
        } else {
            LayoutInflater.from(parentView?.context).inflate(layoutId, null)
        }
    }

    /** 获取Context*/
    override fun getContext(): Context? {
       return mSourceView?.context
    }

    /** 获取当前View*/
    override fun getView(): View? {
        return mSourceView
    }
}