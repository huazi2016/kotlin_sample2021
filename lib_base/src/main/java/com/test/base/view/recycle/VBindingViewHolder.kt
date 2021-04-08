package com.test.base.view.recycle

import android.view.View
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * author : huazi
 * time   : 2021/3/26
 * company：inkr
 * desc   : viewBinding ViewHolder
 */
class VBindingViewHolder<VB : ViewBinding>(val vb: VB, view: View) : BaseViewHolder(view)