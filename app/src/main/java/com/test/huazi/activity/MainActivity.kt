package com.test.huazi.activity

import com.test.base.impl.CommonTitleBackClick
import com.test.base.utils.showToast
import com.test.base.view.BaseActivity
import com.test.huazi.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun initView() {
        binding.testTitleLayout.setOnTitleBackClick(object : CommonTitleBackClick {
            override fun onBackClick() {
                showToast("点击返回了")
            }
        })
        binding.testTv.text = "loading和empty view"
        binding.testTv.setOnClickListener {
            EmptyActivity.launchActivity(activity)
        }

        binding.btnMap.setOnClickListener {
            MapTestActivity.launchActivity(activity)
        }
    }
}