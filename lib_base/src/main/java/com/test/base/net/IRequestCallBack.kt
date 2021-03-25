package com.test.base.net

/**
 * author : huazi
 * time   : 2021/3/10
 * desc   : 网络请求回调
 */
interface IRequestCallBack<T>  {
    //请求成功, 回调数据
    fun loadNetSuccess(resultBo: T?)
    //请求失败
    fun loadNetFailed(code: Int, msg: String?)
}