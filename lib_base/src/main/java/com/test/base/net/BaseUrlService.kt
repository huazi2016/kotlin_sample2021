package com.test.base.net

import com.test.base.bo.BaseNetBo
import com.test.base.bo.ServiceTimeBo
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * author : huazi
 * time   : 2021/3/9
 * desc   : 网络请求-基础UrlService，每个module公共服务统一在这里声明
 */
interface BaseUrlService {

    @GET(NetUrlAddress.GET_SERVICE_TIME)
    suspend fun getServiceTime(
        @Query("mobile") mobile: String,
        @Query("type") type: Int
    ) : BaseNetBo<ServiceTimeBo>
}