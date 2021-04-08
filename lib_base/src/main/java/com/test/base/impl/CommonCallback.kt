package com.test.base.impl


/**
 * author : huazi
 * time   : 2020/10/13
 * desc   : 公共回调基类
 */
interface OnConfirmListener {
    //确认框回调
    fun onConfirm()
    fun onCancel()
}

interface OnPermissionListener {
    //权限申请回调
    fun onGranted()
    fun onDenied()
}

interface CommonTitleClick {
    //两边按钮点击回调
    fun onLeftClick()
    fun onRightClick()
}

interface CommonTitleBackClick {
    //返回按钮回调
    fun onBackClick()
}

interface CommonTitleRightClick {
    //点击右边按钮回调
    fun onRightClick()
}
