package com.test.base.bo

import com.test.base.const.NET_RESPONSE_FAILED
import com.test.base.const.NET_RESPONSE_SUCCESS
import java.io.Serializable

/**
 * author : huazi
 * time   : 2020/10/9
 * desc   :
 */
open class BaseNetBo<T> : Serializable {
    var code: Int = -1
    var message: String = ""
    var data: T? = null

    /**
     * 检查返回结果, 拦截登录失效code
     * @return 请求是否成功
     */
    fun isSuccess(): Boolean {
        if (code == NET_RESPONSE_FAILED) {
            //登录失败，需要重新登录--errCode待与后台确认
            //LoginActivity.actionStart(fromNet = true)
        }
        return code == NET_RESPONSE_SUCCESS
    }
}