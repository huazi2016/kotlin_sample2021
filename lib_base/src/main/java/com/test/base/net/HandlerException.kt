package com.test.base.net

import android.os.Handler
import android.os.Looper
import com.google.gson.JsonParseException
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import org.json.JSONException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.io.NotSerializableException
import java.net.*
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException
import kotlin.coroutines.CoroutineContext

/**
 * author : huazi
 * time   : 2021/3/10
 * desc   : 网络异常处理
 */
class HandlerException constructor() : CoroutineExceptionHandler {

    val handler = Handler(Looper.getMainLooper())
    /** 异常回调*/
    var errorCallBack: INetErrorCallBack? = null
    constructor(error: INetErrorCallBack?) : this() {
        errorCallBack = error
    }

    companion object {

        private const val UNAUTHORIZED = 401

        private const val FORBIDDEN = 403

        private const val NOT_FOUND = 404

        private const val REQUEST_TIMEOUT = 408

        private const val INTERNAL_SERVER_ERROR = 500

        private const val BAD_GATEWAY = 502

        private const val SERVICE_UNAVAILABLE = 503

        private const val GATEWAY_TIMEOUT = 504

        /**http异常**/
        private const val HTTP_ERROR = 600

        /**JSON数据解析数据失败**/
        private const val JSON_PARSE_ERROR = 800

        /**没有网络**/
        private const val NO_NET_CONNECT = 1000

        /**URL格式错误**/
        private const val URL_ERROR = 1001

        /**网络中断 DNS服务器故障 域名解析劫持 触发该异常**/
        private const val UNKNOWN_HOST_ERROR = 1002

        /**网络中断 DNS服务器故障 域名解析劫持 触发该异常**/
        private const val UNKNOWN_SERVICE_ERROR = 1003

        /**一般出现在缓存没有进行序列化，反序列化的过程中出现异常**/
        private const val NOT_SERIALIZABLE_ERROR = 1004

        /**带宽低、延迟高 路径拥堵、服务端负载吃紧 路由节点临时异常**/
        private const val CONNECT_TIMEOUT_ERROR = 1005

        /**请求读写阶段，请求线程被中断 触发该异常**/
        private const val INTERRUPTED_ERROR = 1006

        /**Tls协议协商失败 握手失败 客户端认证证书无法通过 ---可以降级HTTP**/
        private const val SSL_HANDSHAKE_ERROR = 1007

        /**使用 HostnameVerifier 来验证 host 是否合法，如果不合法会抛出 SSLPeerUnverifiedException**/
        private const val SSL_PEER_UNVERIFIED_ERROR = 1008

        /**服务器未运行 或者 端口关闭、防火墙阻止等**/
        private const val CONNECT_ERROR = 1009

        /**其他错误**/
        private const val OTHER_ERROR = 9999
    }

    override val key: CoroutineContext.Key<*>
        get() = CoroutineExceptionHandler

    /**
     * 根据错误类型，处理相关异常
     */
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        if (exception is HttpException) {
            when (exception.code()) {
                UNAUTHORIZED -> NetThrowable(UNAUTHORIZED, exception.message)
                FORBIDDEN -> NetThrowable(FORBIDDEN, exception.message)
                NOT_FOUND -> NetThrowable(NOT_FOUND, exception.message)
                REQUEST_TIMEOUT -> NetThrowable(REQUEST_TIMEOUT, exception.message)
                INTERNAL_SERVER_ERROR -> NetThrowable(INTERNAL_SERVER_ERROR, exception.message)
                BAD_GATEWAY -> NetThrowable(BAD_GATEWAY, exception.message)
                SERVICE_UNAVAILABLE -> NetThrowable(SERVICE_UNAVAILABLE, exception.message)
                GATEWAY_TIMEOUT -> NetThrowable(GATEWAY_TIMEOUT, exception.message)
                else -> NetThrowable(HTTP_ERROR, exception.message)
            }
        } else if (exception is JsonParseException || exception is JSONException) {
            NetThrowable(JSON_PARSE_ERROR, exception.message)
        } else if (exception is MalformedURLException) {
            NetThrowable(URL_ERROR, exception.message)
        } else if (exception is UnknownHostException) {
            NetThrowable(UNKNOWN_HOST_ERROR, exception.message)
        } else if (exception is UnknownServiceException) {
            NetThrowable(UNKNOWN_SERVICE_ERROR, exception.message)
        } else if (exception is NotSerializableException) {
            NetThrowable(NOT_SERIALIZABLE_ERROR, exception.message)
        } else if (exception is SocketTimeoutException) {
            NetThrowable(CONNECT_TIMEOUT_ERROR, exception.message)
        } else if (exception is InterruptedIOException) {
            NetThrowable(INTERRUPTED_ERROR, exception.message)
        } else if (exception is SSLHandshakeException) {
            NetThrowable(SSL_HANDSHAKE_ERROR, exception.message)
        } else if (exception is SSLPeerUnverifiedException) {
            NetThrowable(SSL_PEER_UNVERIFIED_ERROR, exception.message)
        } else if (exception is ConnectException) {
            NetThrowable(CONNECT_ERROR, exception.message)
        } else if (exception is BusinessThrowable) {
            BusinessThrowable(exception.code, exception.error_msg)
        } else {
            NetThrowable(OTHER_ERROR, exception.message)
        }
    }

    /**
     * 网络等相关异常
     */
    inner class NetThrowable(code: Int, override var message: String?) : Throwable() {
        init {
            handler.post {
                errorCallBack?.OnFailed(code, message)
                Logger.d("NetThrowable: code=$code message=$message")
            }
        }
    }

    /**
     * 业务异常（Retrofit的HttpException Gson的解析、IO异常等等）
     */
    inner class BusinessThrowable constructor(var code: Int, var error_msg: String?) : Throwable() {
        init {
            handler.post {
                errorCallBack?.onError(code, error_msg)
                Logger.d("BusinessThrowable: code=$code message=$$error_msg")
            }
        }
    }

}