package com.test.base.bo

import okhttp3.Cookie

/**
 * author : huazi
 * time   : 2020/9/3
 * desc   : Cookie 数据实体类
 */
data class CookieBo(var cookies: ArrayList<Cookie>? = arrayListOf())