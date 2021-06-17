package com.test.base.view.recycle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * author : huazi
 * time   : 2021/3/26
 * desc   : viewBinding Adapter
 */
abstract class BaseVBAdapter<T, VB : ViewBinding>(data: MutableList<T>? = null)
    : BaseQuickAdapter<T, VBViewHolder<VB>>(0, data) {

    abstract fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): VB

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VBViewHolder<VB> {
        val viewBinding = createViewBinding(LayoutInflater.from(parent.context), parent)
        return VBViewHolder(viewBinding, viewBinding.root)
    }
}