package com.test.base.utils

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import kotlin.collections.HashMap

/**
 * author : ning
 * desc   : 简单路由工具
 */

class Router {
    var maps= mutableMapOf<String, Class<out Any>>()
    var context:Context?=null
    var host:String?=null
    companion object {
        //通过@JvmStatic注解，使得在Java中调用instance直接是像调用静态函数一样，
        //类似KLazilyDCLSingleton.getInstance(),如果不加注解，在Java中必须这样调用: KLazilyDCLSingleton.Companion.getInstance().
        @JvmStatic
        //使用lazy属性代理，并指定LazyThreadSafetyMode为SYNCHRONIZED模式保证线程安全
        val instance: Router by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { Router() }
    }
    fun init(context: Context, build: Router.() -> Unit){
        this.context=context
        host=context.applicationInfo.packageName
        build()
    }
    operator fun Pair<String, Class<out Any>>.unaryPlus(){
        map(this)
    }
    private fun map(p: Pair<String, Class<out Any>>){
        maps[p.first]=p.second
    }
    @JvmName("unaryPlusStringAny")
    operator fun Pair<String, Handler<Any>>.unaryPlus(){
        handlers[first]=second
    }


}

@Throws(Exception::class)
fun convertToQueryStringToHashMap(
    source: String
): HashMap<String, String>? {
    val data = HashMap<String, String>()
    val arrParameters = source.split("&".toRegex()).toTypedArray()
    for (tempParameterString in arrParameters) {
        val arrTempParameter = tempParameterString
            .split("=".toRegex()).toTypedArray()
        if (arrTempParameter.size >= 2) {
            val parameterKey = arrTempParameter[0]
            val parameterValue = arrTempParameter[1]
            data[parameterKey] = parameterValue
        } else {
            val parameterKey = arrTempParameter[0]
            data[parameterKey] = ""
        }
    }
    return data
}
fun Uri.open(){
    val uri=this
    if(uri.host==Router.instance.host) {
        when (uri.scheme) {
            SCHEME_ACTIVITY -> {
                val clazz=Router.instance.maps[uri.path]
                clazz?.apply {
                    val params= convertToQueryStringToHashMap(uri.query.orEmpty())
                    Router.instance.context?.startActivity(Intent(Router.instance.context, clazz).apply {
                        addFlags(FLAG_ACTIVITY_NEW_TASK)
                        params?.forEach {
                            putExtra(it.key, it.value)
                        }
                    })
                }
            }
        }
    }
}

/**
 * 路由调用简单工具
 */
typealias Handler<T> = suspend (CallParams) -> T
typealias CallParams = Map<String, String>
var handlers = mutableMapOf<String, Handler<Any>>()
suspend inline fun <T> Uri.exec(): T? {
    val uri=this
    if(uri.host==Router.instance.host) {
        when (uri.scheme) {
            SCHEME_INVOKE-> {
                val params = convertToQueryStringToHashMap(uri.query.orEmpty()).orEmpty()
                return handlers[uri.path]?.invoke(params) as T
            }
        }
    }
    return null
}

const val SCHEME_ACTIVITY="act"
const val SCHEME_INVOKE="invoke"