package com.test.base.bo

import com.flyco.tablayout.listener.CustomTabEntity

/**
 * author : huazi
 * time   : 2021/3/15
 * desc   : tab基础数据Bo
 */
class BaseTabBo (var title: String, var selectedIcon: Int, var unSelectedIcon: Int)
    : CustomTabEntity {

    override fun getTabUnselectedIcon(): Int {
        return unSelectedIcon
    }

    override fun getTabSelectedIcon(): Int {
        return selectedIcon
    }

    override fun getTabTitle(): String {
       return title
    }
}