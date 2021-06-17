package com.test.sample.activity

import android.content.Context
import android.content.Intent
import com.test.sample.R
import com.test.base.impl.CommonTitleClick
import com.test.base.utils.AppManager
import com.test.base.utils.ResourceUtil
import com.test.base.utils.StatusBarUtil
import com.test.base.view.BaseActivity
import com.test.base.view.loadview.LoadViewHelper
import com.test.sample.databinding.ActivityEmptyBinding

class EmptyActivity : BaseActivity<ActivityEmptyBinding>() {

    companion object {
        public fun launchActivity(context: Context? = AppManager.getContext()) {
            val  intent = Intent(context, EmptyActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun getViewBinding() = ActivityEmptyBinding.inflate(layoutInflater)

    override fun initView() {
        StatusBarUtil.immersive(this.window, ResourceUtil.getColor(R.color.colorPrimaryDark), 1f)
        binding.emptyTitleLayout.setTitle("loading")
        val loadView = LoadViewHelper(binding.testRlLayout)
        loadView.showPbLoading(R.string.loading)
        binding.emptyTitleLayout.setOnTitleClick(object : CommonTitleClick {
            override fun onLeftClick() {
                finish()
            }

            override fun onRightClick() {
                loadView.restoreView()
            }
        })
    }
}