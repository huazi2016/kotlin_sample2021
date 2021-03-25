package com.test.base.net


import android.util.MalformedJsonException
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.test.base.utils.orFalse
import com.test.base.utils.toJsonString
import com.tencent.mmkv.MMKV
import org.json.JSONException
import java.io.IOException
import java.lang.reflect.Type
import java.net.URISyntaxException

/**
 * author : ning
 * desc   :
 */
class NetStatus(var error: Exception?, var willDone: Boolean = false)

/**
 * 同步处理网络请求流程和ui显示流程
 */
suspend fun <T> exec(exeBlock: suspend () -> T, backBlock: suspend (T) -> Unit): NetStatus {
    val data: T
    try {
        data = exeBlock()
    } catch (e: Exception) {
        e.printStackTrace()
        return NetStatus(e)
    }
    backBlock(data)
    return NetStatus(null)
}

/**
 * 请求网络并缓存返回内容.
 * 因为一定有结果, 暂时不返回异常
 * 首先取网络,如果网络请求成功,保存数据到本地并;如果失败,取本地数据展示
 */
suspend inline fun <reified T> execAndCache(
    spKey: String,
    default: String,
    exeBlock: suspend () -> T,
    noinline returnblock: (suspend (T) -> Unit)?
): Unit {
    var data: T? = null
    try {
        //读取本地sp内容
        var json = MMKV.defaultMMKV().decodeString(spKey)
        if (json.isNullOrEmpty()) {//如果没有,就把apk中默认内容写入sp
            MMKV.defaultMMKV().encode(spKey, default)
        }
        //请求网络
        data = exeBlock()
        //把网络结果写入sp
        MMKV.defaultMMKV().encode(spKey, data.toJsonString())
    } catch (e: Exception) {
        e.printStackTrace()
        //如果网络有错误, 读取sp
        val jsonType: Type = object : TypeToken<T>() {}.type
        data = Gson().fromJson(MMKV.defaultMMKV().decodeString(spKey), jsonType)
    }
    data?.apply {
        returnblock?.apply {
            returnblock(data)
        }
    }
}


/**
 * 网络错误处理--全部
 */
infix fun NetStatus.allException(block: (Exception?) -> Unit): NetStatus {
    return when (this.error) {
        is Exception -> {
            block(this.error)
            NetStatus(null)
        }
        else -> this
    }
}

/**
 * 网络错误处理
 */
infix fun NetStatus.netException(block: (Exception?) -> Unit): NetStatus {
    return when {
        this.error is URISyntaxException ||
                (this.error is IOException && this.error !is MalformedJsonException) -> {
            block(this.error)
            NetStatus(null)
        }
        else -> this
    }
}


/**
 * json解析错误处理
 */
infix fun NetStatus.jsonException(block: (Exception?) -> Unit): NetStatus {
    return when (this.error) {
        is retrofit2.HttpException,
        is JsonIOException,
        is JsonParseException,
        is JSONException,
        is MalformedJsonException,
        is JsonSyntaxException -> {
            block(this.error)
            NetStatus(null)
        }
        else -> this
    }
}

/**
 * 筛选异常类型
 */
infix fun NetStatus.filter(block: () -> Array<Class<*>>): NetStatus {
    val clazzes = block()
    clazzes.forEach {
        this.error?.javaClass?.apply {
            if (it.isAssignableFrom(error?.javaClass).orFalse()) {
                return NetStatus(error, true)
            }
        }
    }
    return this
}

/**
 * 处理
 */
infix fun NetStatus.then(block: () -> Unit): NetStatus {
    if (willDone) {
        error?.apply {
            block()
        }
    }
    return NetStatus(error, false)
}