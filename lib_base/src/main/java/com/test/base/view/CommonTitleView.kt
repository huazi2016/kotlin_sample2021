package com.test.base.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.test.base.R
import com.test.base.impl.CommonTitleBackClick
import com.test.base.impl.CommonTitleClick
import com.test.base.impl.CommonTitleRightClick
import com.test.base.utils.ResourceUtil
import com.test.base.utils.setViewGone
import com.test.base.utils.setViewVisible

/**
 * author : huazi
 * time   : 2021/4/7
 * company：inkr
 * desc   : 公共标题栏封装
 */
class CommonTitleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var clCommonTitle: LinearLayout
    private lateinit var ivCommonBack: AppCompatImageView
    private lateinit var tvCommonTitle: AppCompatTextView
    private lateinit var clCommonRight: ConstraintLayout
    private lateinit var ivCommonRightIcon: AppCompatImageView
    private lateinit var tvCommonRightText: AppCompatTextView
    private lateinit var pbCommonProgress: ProgressBar
    /** 展示返回按钮的标记, 默认展示*/
    private var isisShowLeftIcon = true

    init {
        LayoutInflater.from(context).inflate(R.layout.common_title_layout, this)
        initView()
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.CommonTitleView,
                defStyleAttr, 0)
            for (index in 0..typedArray.indexCount) {
                when (val attr = typedArray.getIndex(index)) {
                    R.styleable.CommonTitleView_titleBackground -> {
                        clCommonTitle.setBackgroundColor(typedArray.getColor(attr, 0))
                    }
                    R.styleable.CommonTitleView_isShowLeftIcon -> {
                        isisShowLeftIcon = typedArray.getBoolean(attr, false)
                    }
                    R.styleable.CommonTitleView_leftDrawable -> {
                        ivCommonBack.setImageResource(typedArray.getResourceId(attr,
                            R.drawable.common_left_back))
                    }
                    R.styleable.CommonTitleView_centerTitle -> {
                        setViewVisible(tvCommonTitle)
                        tvCommonTitle.text = typedArray.getString(attr)
                    }
                    R.styleable.CommonTitleView_centerTextColor -> {
                        tvCommonTitle.setTextColor(typedArray.getColor(attr, 0))
                    }
                    R.styleable.CommonTitleView_centerTextSize -> {
                        tvCommonTitle.setTextSize(
                            TypedValue.COMPLEX_UNIT_SP,
                            typedArray.getFloat(attr, 15f))
                    }
                    R.styleable.CommonTitleView_rightText -> {
                        setViewVisible(tvCommonRightText)
                        tvCommonRightText.text = typedArray.getString(attr)
                    }
                    R.styleable.CommonTitleView_rightTextColor -> {
                        tvCommonRightText.setTextColor(typedArray.getColor(attr, 0))
                    }
                    R.styleable.CommonTitleView_rightTextSize -> {
                        tvCommonRightText.setTextSize(
                            TypedValue.COMPLEX_UNIT_SP,
                            typedArray.getFloat(attr, 12f))
                    }
                    R.styleable.CommonTitleView_rightDrawable -> {
                        setViewVisible(ivCommonRightIcon)
                        ivCommonRightIcon.setImageResource(typedArray.getResourceId(attr, 0))
                    }
                }
            }
            typedArray.recycle()
            if (isisShowLeftIcon) {
                setViewVisible(ivCommonBack)
            } else {
                setViewGone(ivCommonBack)
            }
            ivCommonBack.setOnClickListener {
                clickListener?.onLeftClick()
                backListener?.onBackClick()
            }
            ivCommonRightIcon.setOnClickListener {
                clickListener?.onRightClick()
                rightListener?.onRightClick()
            }
            tvCommonRightText.setOnClickListener {
                clickListener?.onRightClick()
                rightListener?.onRightClick()
            }
        }
    }

    private fun initView() {
        clCommonTitle = findViewById(R.id.clCommonTitle)
        ivCommonBack = findViewById(R.id.ivCommonBack)
        tvCommonTitle = findViewById(R.id.tvCommonTitle)
        clCommonRight = findViewById(R.id.clCommonRight)
        ivCommonRightIcon = findViewById(R.id.ivCommonRightIcon)
        tvCommonRightText = findViewById(R.id.tvCommonRightText)
        pbCommonProgress = findViewById(R.id.pbCommonProgress)
    }

    /** webView加载进度*/
    fun setProgressBar(progress: Int): ProgressBar {
        if (progress > 0) {
            setViewVisible(pbCommonProgress)
            pbCommonProgress.progress = progress
        }
        return pbCommonProgress
    }

    fun hideProgressBar() {
        pbCommonProgress.progress = 100
        setViewGone(pbCommonProgress)
    }

    fun setTitleBackground(colorId: Int) {
        clCommonTitle.setBackgroundColor(ResourceUtil.getColor(colorId))
    }

    fun setLeftIcon(drawId: Int) {
        ivCommonBack.setImageResource(drawId)
    }

    fun setTitle(title: String) {
        tvCommonTitle.text = title
    }

    fun setTitleSize(size: Float) {
        tvCommonTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    fun setRightText(text: String) {
        tvCommonRightText.text = text
    }

    fun setRightTextSize(size: Float) {
        tvCommonRightText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    fun setRightIcon(drawId: Int) {
        ivCommonRightIcon.setImageResource(drawId)
    }

    /** 两边的按钮点击事件的回调*/
    private var clickListener: CommonTitleClick? = null
    fun setOnTitleClick(clickListener: CommonTitleClick) {
        this.clickListener = clickListener
    }

    /** 返回按钮点击事件的回调*/
    private var backListener: CommonTitleBackClick? = null
    fun setOnTitleBackClick(backListener: CommonTitleBackClick) {
        this.backListener = backListener
    }


    /** 右侧按钮点击事件的回调*/
    private var rightListener: CommonTitleRightClick? = null
    fun setOnTitleRightClick(rightListener: CommonTitleRightClick) {
        this.rightListener = rightListener
    }
}