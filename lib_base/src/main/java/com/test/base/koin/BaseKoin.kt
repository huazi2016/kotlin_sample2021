package com.test.base.koin

import com.test.base.BuildConfig
import com.test.base.bo.CookieBo
import com.test.base.net.BaseUrlService
import com.test.base.net.InterceptorLogger
import com.test.base.net.LoggerInterceptor
import com.test.base.utils.UrlUtil
import com.test.base.utils.toJsonString
import com.test.base.utils.toTypeEntity
import com.test.base.const.SP_KEY_COOKIES
import com.orhanobut.logger.Logger
import com.tencent.mmkv.MMKV
import okhttp3.*
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * author : huazi
 * time   : 2021/3/8
 * desc   :
 */
val netModule: Module = module {

    single {

        val logger = object : InterceptorLogger {
            override fun invoke(msg: String) {
                Logger.t("NET_LOG").i(msg)
            }
        }

        OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val oldRequest = chain.request().newBuilder()
                        .header("Content-Type", "multipart/form-data")
                        //.header("Accept-Encoding", "gzip, deflate")
                        //.header("Accept-Charset", "UTF-8")
                        //.header("Connection", "keep-alive")
                        .header("Token", "lQ24EDC44TsQ")
                        .header("Device-Type", "ANDROID")
                        .build()
                    Logger.d("Koin_test-->oldUrl=" + oldRequest.url)
                    //获取当前时间戳, 此处可以不取服务器时间
                    val currentTimeMillis = System.currentTimeMillis().toString()
                    val oldUrl = oldRequest.url.toString()
                    val signMd5 = UrlUtil.urlParsing(oldUrl, currentTimeMillis)
                    //拼接公共参数
                    val authorizedUrlBuilder = oldRequest.url.newBuilder()
                        .addQueryParameter("t", currentTimeMillis)
                        .addQueryParameter("sign", signMd5)
                        //.addQueryParameter("stk", getToken())
                        // TODO: 2021/3/9 待补充token
                        .addQueryParameter("stk", "")
                    //重新构造
                    val newRequest = oldRequest.newBuilder()
                        .method(oldRequest.method, oldRequest.body)
                        .url(authorizedUrlBuilder.build())
                        .build()
                    Logger.d("Koin_test-->newUrl=" + newRequest.url)
                    return chain.proceed(newRequest)
                }
            })
            .cookieJar(object : CookieJar {
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    val cookieEntity = MMKV.defaultMMKV().decodeString(SP_KEY_COOKIES, "")
                        .toTypeEntity<CookieBo>()
                    return cookieEntity?.cookies.orEmpty()
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    if (cookies.size > 1) {
                        val ls = arrayListOf<Cookie>()
                        ls.addAll(cookies)
                        val cookieEntity = CookieBo(ls)
                        MMKV.defaultMMKV().encode(SP_KEY_COOKIES, cookieEntity.toJsonString())
                    }
                }
            }).addNetworkInterceptor(
                LoggerInterceptor(logger, if (BuildConfig.DEBUG) LoggerInterceptor.LEVEL_BODY else LoggerInterceptor.LEVEL_NONE)
            )/*.connectTimeout(3000, TimeUnit.SECONDS)*//*.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier(HostnameVerifier{_,_->true})*/.build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("http://18pinpin.cn")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get<OkHttpClient>())
            .build()
    }

    single<BaseUrlService> {
        get<Retrofit>().create(BaseUrlService::class.java)
    }
}