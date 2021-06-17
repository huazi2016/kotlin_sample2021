package com.test.base.view.loadview

import android.animation.Animator
import android.app.Activity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.lottie.LottieAnimationView
import com.test.base.R
import com.test.base.utils.NetworkUtil
import com.test.base.utils.ResourceUtil
import com.test.base.utils.setViewGone
import com.test.base.utils.setViewVisible
import java.lang.ref.WeakReference


/**
 * author : huazi
 * time   : 2021/3/26
 * desc   : loadView调用类
 */
class LoadViewHelper(view: View?) {

    private var helper: IVaryViewHelper? = VaryViewHelper(view)

    /** 处理文案到顶部的距离,不需要转*/
    private var marginTop = 0
    /** DES: 背景色*/
    private var mBackgroundColor: Int = R.color.app_bg
    /** DES: 是否修改背景色*/
    private var mIsBackgroundColor = false
    private var animationView: LottieAnimationView? = null

    /**
     * 构造2
     * @param view 指定view
     * @param isWebView true:是webView
     */
    constructor(view: View, isWebView: Boolean) : this(view) {
        this.helper = VaryViewHelper(view, isWebView)
    }

    /** 自定义图片距离上面的距离*/
    fun setMarginTop(marginTop: Int) {
        this.marginTop = marginTop
    }

    /**
     * 展示loadingView--lottie
     * @param resId 文案
     */
    fun showAmLoading(@StringRes resId: Int) {
        val layout = helper?.inflate(R.layout.base_loading_layout)
        if (mIsBackgroundColor) {
            //设置整体背景色
            layout?.setBackgroundResource(mBackgroundColor)
        }
        animationView = layout?.findViewById(R.id.lavAnimaView)
        setViewVisible(animationView)
        val tvLoadText = layout?.findViewById<AppCompatTextView>(R.id.tvLoadText)
        //提示文案
        val loadText = ResourceUtil.getStringById(resId)
        if (loadText.isEmpty()) {
            setViewGone(tvLoadText)
        } else {
            setViewVisible(tvLoadText)
            tvLoadText?.text = loadText
        }
        //启动lottie动画
        animationView?.setAnimation("datas.json")
        //已废弃,使用repeatCount替代
        //mLottieAnimationView.loop(true)
        animationView?.repeatCount = -1
        //animationView?.addAnimatorListener(LavAnimaListener(ivLoadView, animationView))
        animationView?.playAnimation()
        //展示loadingView
        helper?.showLayout(layout)
    }

    /**
     * 展示loadingView--progressBar
     * @param resId 文案
     */
    fun showPbLoading(@StringRes resId: Int) {
        val layout = helper?.inflate(R.layout.base_loading_layout)
        if (mIsBackgroundColor) {
            //设置整体背景色
            layout?.setBackgroundResource(mBackgroundColor)
        }
        val pbLoadView = layout?.findViewById<AppCompatImageView>(R.id.pbLoadView)
        setViewVisible(pbLoadView)
        val tvLoadText = layout?.findViewById<AppCompatTextView>(R.id.tvLoadText)
        //提示文案
        val loadText = ResourceUtil.getStringById(resId)
        if (loadText.isEmpty()) {
            setViewGone(tvLoadText)
        } else {
            setViewVisible(tvLoadText)
            tvLoadText?.text = loadText
        }
        pbLoadView?.startAnimation(
            AnimationUtils.loadAnimation(pbLoadView.context, R.anim.period_rotating)
        )
        helper?.showLayout(layout)
    }

    /**
     * 展示loadingView--静态图
     * @param resId 文案
     */
     fun showImgLoading(@StringRes resId: Int) {
        val layout = helper?.inflate(R.layout.base_loading_layout)
        if (mIsBackgroundColor) {
            //设置整体背景色
            layout?.setBackgroundResource(mBackgroundColor)
        }
        val ivLoadView = layout?.findViewById<AppCompatImageView>(R.id.ivLoadView)
        setViewVisible(ivLoadView)
        val tvLoadText = layout?.findViewById<AppCompatTextView>(R.id.tvLoadText)
        //提示文案
        val loadText = ResourceUtil.getStringById(resId)
        if (loadText.isEmpty()) {
            setViewGone(tvLoadText)
        } else {
            setViewVisible(tvLoadText)
            tvLoadText?.text = loadText
        }
        helper?.showLayout(layout)
    }

    /**
     * 展示空视图/无数据等样式
     * @param activity 上下文
     * @param businessType 业务类型
     */
    fun showEmptyView(activity: Activity, businessType: Int) {
        // TODO: 2021/3/27 根据项目完善
        val layout = helper?.inflate(R.layout.common_empty_layout)
        val ivEmptyIcon = layout?.findViewById<AppCompatImageView>(R.id.ivEmptyIcon)
        val tvEmptyContent = layout?.findViewById<AppCompatTextView>(R.id.tvEmptyContent)
        var errMsg = "暂无数据，看看其他的数据吧~"
        if (!NetworkUtil.isConnected(activity)) {
            errMsg = "网络异常，请重试"
            //drawId = R.drawable.networ_failed_icon
        } else {
            //var drawId = R.drawable.empty_icon02
            if (businessType == 1) {
                //关注列表--空视图
                errMsg = "还没有关注，去找找您喜欢的比赛关注吧"
                //drawId = R.drawable.no_follow_icon
            } else if (businessType == 2){
                //修改头像
                errMsg = "暂无图片资源"
                //drawId = R.drawable.match_empty_icon
            }
        }
        helper?.showLayout(layout)
    }

    /** 还原原来的布局*/
    fun restoreView() {
        helper?.restoreView()
        animationView?.cancelAnimation()
    }

    internal class LavAnimaListener(
        tvLoadView: ImageView?,
        lottieAnimationView: LottieAnimationView?
    )
        : Animator.AnimatorListener {
        var imageViewWeakReference: WeakReference<ImageView>? = WeakReference(tvLoadView)
        var lottieAnimaReference: WeakReference<LottieAnimationView>? = WeakReference(
            lottieAnimationView
        )
        override fun onAnimationStart(animation: Animator) {
            if (imageViewWeakReference == null || lottieAnimaReference == null) {
                return
            }
            val tvLoadView = imageViewWeakReference?.get() ?: return
            setViewGone(tvLoadView)
            val lottieAnimationView = lottieAnimaReference?.get() ?: return
            setViewVisible(lottieAnimationView)
        }

        override fun onAnimationEnd(animation: Animator) {}
        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    }
}