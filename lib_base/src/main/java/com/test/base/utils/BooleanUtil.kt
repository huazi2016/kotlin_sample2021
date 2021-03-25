@file:Suppress("unused")
@file:JvmName("BooleanExt")

package com.test.base.utils

/**
 * 如果为 null 则为 false
 */
fun Boolean?.orFalse(): Boolean {
    return this ?: false
}

/**
 * 如果为 null 则为 true
 */
fun Boolean?.orTrue(): Boolean {
    return this ?: true
}
/**
 * 转换成int, true=1, false=0
 */
fun Boolean?.toInt():Int{
    return if(this.orFalse()) 1 else 0
}
/**
 * 判断条件节点
 */
val Boolean?.condition: Boolean
    get() = this == true

