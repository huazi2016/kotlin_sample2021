package com.test.base.utils

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest

/**
 * author : huazi
 * time   : 2020/9/24
 * desc   :
 */
object MD5Util {

    private const val MD5 = "MD5"

    /**
     * 增加key
     * @param content 内容
     * @desc 用于网络请求, 拼接header
     */
    fun encryptSplicingHeader(content: String): String? {
        val value = content + encrypt("weiliem@#Tiesport".toByteArray())
        return encrypt(value.toByteArray())
    }

    fun encrypt(value: ByteArray?): String? {
        return if (value == null) "" else try {
            val md = MessageDigest.getInstance(MD5Util.MD5)
            md.update(value)
            val b = md.digest()
            var i: Int
            val buf = StringBuffer("")
            for (offset in b.indices) {
                i = b[offset].toInt()
                if (i < 0) i += 256
                if (i < 16) buf.append("0")
                buf.append(Integer.toHexString(i))
            }
            //32位加密
            buf.toString()
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 对一个文件获取md5值
     * @return md5串
     */
    fun getMD5(file: File?): String? {
        var value = ""
        var `in`: FileInputStream? = null
        try {
            var numRead: Int
            val md5 = MessageDigest.getInstance("MD5")
            `in` = FileInputStream(file)
            val buffer = ByteArray(2048)
            while (`in`.read(buffer).also { numRead = it } > 0) {
                md5.update(buffer, 0, numRead)
            }
            val bi = BigInteger(1, md5.digest())
            value = bi.toString(16)
            value = String.format("%32s", value).replace(' ', '0')
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        } finally {
            if (null != `in`) {
                try {
                    `in`.close()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            }
        }
        return value
    }
}