package com.test.huazi.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.base.impl.CommonTitleBackClick
import com.test.base.utils.showToast
import com.test.base.view.BaseActivity
import com.test.base.view.recycle.BaseVBAdapter
import com.test.base.view.recycle.VBViewHolder
import com.test.huazi.bo.MainItemBo
import com.test.huazi.databinding.ActivityMainBinding
import com.test.huazi.databinding.ItemMainListBinding
import com.test.sample.activity.EmptyActivity
import com.test.sample.activity.MapTestActivity
import com.test.sample.activity.RecycleActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun initView() {
        binding.testTitleLayout.setTitle("首页")
        binding.testTitleLayout.setOnTitleBackClick(object : CommonTitleBackClick {
            override fun onBackClick() {
                showToast("点击返回了")
            }
        })
        val dataList = setListData()
        binding.testMainList.let {
            it.layoutManager = LinearLayoutManager(it.context)
            it.adapter = MainListAdapter(dataList)
        }
    }

    private fun setListData(): MutableList<MainItemBo> {
        val dataList = mutableListOf<MainItemBo>()
        dataList.add(MainItemBo("RecycleView封装", 1))
        dataList.add(MainItemBo("全局loading", 2))
        dataList.add(MainItemBo("地图Demo", 3))
        return dataList
    }

    private fun setItemClick(item: MainItemBo) {
        when (item.itemType) {
            1 -> {
                //recycleView封装
                RecycleActivity.launchActivity(activity)
            }
            2 -> {
                //全局loading
                EmptyActivity.launchActivity(activity)
            }
            3 -> {
                //地图demo
                MapTestActivity.launchActivity(activity)
            }
        }
    }

    inner class MainListAdapter(dataList: MutableList<MainItemBo>) :
        BaseVBAdapter<MainItemBo, ItemMainListBinding>(dataList) {
        override fun createViewBinding(
            inflater: LayoutInflater,
            parent: ViewGroup
        ): ItemMainListBinding {
            return ItemMainListBinding.inflate(inflater, parent, false)
        }

        override fun convert(holder: VBViewHolder<ItemMainListBinding>, item: MainItemBo) {
            holder.binding.tvMainItem.text = item.name
            holder.itemView.setOnClickListener {
                setItemClick(item)
            }
        }
    }
}