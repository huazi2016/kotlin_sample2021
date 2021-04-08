package com.test.huazi.activity

import android.content.Context
import android.content.Intent
import com.test.base.impl.CommonTitleBackClick
import com.test.base.impl.CommonTitleClick
import com.test.base.utils.AppManager
import com.test.base.utils.showToast
import com.test.base.view.BaseActivity
import com.test.base.view.loadview.LoadViewHelper
import com.test.huazi.R
import com.test.huazi.databinding.ActivityEmptyBinding
import com.test.huazi.databinding.ActivityMainBinding

class EmptyActivity : BaseActivity<ActivityEmptyBinding>() {

    companion object {
        public fun launchActivity(context: Context? = AppManager.getContext()) {
            val  intent = Intent(context, EmptyActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun getViewBinding() = ActivityEmptyBinding.inflate(layoutInflater)

    override fun initView() {
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