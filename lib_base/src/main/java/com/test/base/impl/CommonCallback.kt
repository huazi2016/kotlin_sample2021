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
