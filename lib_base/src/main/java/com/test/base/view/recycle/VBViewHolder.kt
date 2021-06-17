package com.test.base.view.recycle

import android.view.View
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * author : huazi
 * time   : 2021/3/26
 * desc   : viewBinding ViewHolder
 */
class VBViewHolder<VB : ViewBinding>(val binding: VB, view: View) : BaseViewHolder(view)