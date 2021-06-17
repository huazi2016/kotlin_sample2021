package com.test.sample.activity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseBinderAdapter
import com.chad.library.adapter.base.binder.QuickViewBindingItemBinder
import com.test.base.utils.AppManager
import com.test.base.utils.ResourceUtil
import com.test.base.utils.StatusBarUtil
import com.test.base.view.BaseActivity
import com.test.base.view.recycle.BaseVBAdapter
import com.test.base.view.recycle.VBViewHolder
import com.test.sample.R
import com.test.sample.bo.RecycleBo01
import com.test.sample.bo.RecycleBo02
import com.test.sample.bo.RecycleBo03
import com.test.sample.databinding.ActivityRecycleBinding
import com.test.sample.databinding.ItemTestList02Binding
import com.test.sample.databinding.ItemTestList03Binding
import com.test.sample.databinding.ItemTestListBinding

class RecycleActivity : BaseActivity<ActivityRecycleBinding>() {

    private val dataList = mutableListOf<Any>()
    private val adapter = BaseBinderAdapter()

    companion object {
        public fun launchActivity(context: Context? = AppManager.getContext()) {
            val  intent = Intent(context, RecycleActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun getViewBinding() = ActivityRecycleBinding.inflate(layoutInflater)

    override fun initView() {
        StatusBarUtil.immersive(this.window, ResourceUtil.getColor(R.color.colorPrimaryDark), 1f)
        adapter.addItemBinder(RecycleBo01::class.java, TestAdapter02(), null)
        adapter.addItemBinder(RecycleBo02::class.java, TestAdapter03(), null)
        adapter.addItemBinder(RecycleBo03::class.java, TestAdapter04(), null)
        binding.testRV.let {
            it.layoutManager = LinearLayoutManager(it.context)
            it.adapter = adapter
        }
        setListData()
        binding.testRefresh.setOnRefreshListener {
            dataList.clear()
            setListData()
            binding.testRefresh.finishRefresh(2000)
        }
        binding.testRefresh.setOnLoadMoreListener {
            setListData()
            binding.testRefresh.finishLoadMore(2000)
        }
    }

    private fun setListData() {
        for (index in 1..10) {
            if (index % 2 == 0) {
                dataList.add(RecycleBo01("一类型$index"))
            } else {
                dataList.add(RecycleBo02("二类型$index"))
            }
            dataList.add(RecycleBo03("三类型$index"))
        }
        adapter.setList(dataList)
    }

    inner class TestAdapter(list: MutableList<String>) : BaseVBAdapter<String, ItemTestListBinding>(list) {

        override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemTestListBinding {
            return ItemTestListBinding.inflate(inflater, parent, false)
        }

        override fun convert(holder: VBViewHolder<ItemTestListBinding>, item: String) {
            holder.binding.testText.text = item
        }
    }

    inner class TestAdapter02 : QuickViewBindingItemBinder<RecycleBo01, ItemTestListBinding>() {

        override fun onCreateViewBinding(layoutInflater: LayoutInflater, parent: ViewGroup,
                                         viewType: Int): ItemTestListBinding {
            return ItemTestListBinding.inflate(layoutInflater, parent, false)
        }

        override fun convert(holder: BinderVBHolder<ItemTestListBinding>, data: RecycleBo01) {
            holder.viewBinding.testText.text = data.name
        }
    }

    inner class TestAdapter03 : QuickViewBindingItemBinder<RecycleBo02, ItemTestList02Binding>() {

        override fun onCreateViewBinding(layoutInflater: LayoutInflater, parent: ViewGroup,
                                         viewType: Int): ItemTestList02Binding {
            return ItemTestList02Binding.inflate(layoutInflater, parent, false)
        }

        override fun convert(holder: BinderVBHolder<ItemTestList02Binding>, data: RecycleBo02) {
            holder.viewBinding.testText.text = data.name
        }
    }

    inner class TestAdapter04 : QuickViewBindingItemBinder<RecycleBo03, ItemTestList03Binding>() {

        override fun onCreateViewBinding(layoutInflater: LayoutInflater, parent: ViewGroup,
                                         viewType: Int): ItemTestList03Binding {
            return ItemTestList03Binding.inflate(layoutInflater, parent, false)
        }

        override fun convert(holder: BinderVBHolder<ItemTestList03Binding>, data: RecycleBo03) {
            holder.viewBinding.testText.text = data.name
        }
    }

}