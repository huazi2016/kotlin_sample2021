package com.test.base.net

/**
 * author : huazi
 * time   : 2021/3/10
 * desc   : 网络异常回调
 */
interface INetErrorCallBack {
    //网络失败
    fun OnFailed(code: Int, msg: String?)
    //业务异常
    fun onError(code: Int, msg: String?)
}