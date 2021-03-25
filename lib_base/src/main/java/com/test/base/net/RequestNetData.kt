package com.test.base.net

import com.test.base.bo.BaseNetBo
import com.orhanobut.logger.Logger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform

/**
 * author : huazi
 * time   : 2021/3/10
 * desc   : 网络数据转换类
 */
object RequestNetData {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun <T> transform(response: BaseNetBo<T>) : T? {
        var data: T? = null
        flow<BaseNetBo<T>> {
            emit(response)
        }.transform<BaseNetBo<T>, T?> {
            //主线程
            Logger.d("ThreadName=" + Thread.currentThread().name)
            if (it.isSuccess()) {
                emit(it.data)
            } else {
                throw HandlerException().BusinessThrowable(it.code, it.message)
            }
        }.collect {
            data = it
        }
        return data
    }
}