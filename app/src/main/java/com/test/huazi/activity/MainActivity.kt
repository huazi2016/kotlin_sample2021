package com.test.huazi.activity

import com.test.base.view.BaseActivity
import com.test.huazi.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun initView() {
        binding.testTv.text = "我是华子"
    }
}