package com.test.base.utils

import android.app.Activity
import android.graphics.drawable.Drawable
import com.orhanobut.logger.Logger
import java.io.*


/**
 * author : huazi
 * time   : 2020/10/31
 * desc   : 文件操作工具类
 */
object FileUtil {

    /**
     * 读取Assets目录下的图片资源
     */
    fun loadAssetsFileToDrawable(activity: Activity, fileName: String) : Drawable? {
        try {
            // get input stream
            //val ims: InputStream = activity.assets.open("avatar.jpg")
            val ims: InputStream = activity.assets.open(fileName)
            // load image as Drawable
            return Drawable.createFromStream(ims, null)
        } catch (ex: IOException) {
            return null
        }
    }

    /**
     * 读取Assets目录下的TAB json数据
     */
    fun loadAssetsFileToTab() {
        var inStr: InputStream? = null
        var buffRead: BufferedReader? = null
        val stringBuffer = StringBuffer()
        try {
            val assetManager = AppManager.getApplication().assets
            inStr = assetManager.open("nami_tab.json")
            buffRead = BufferedReader(InputStreamReader(inStr))
            var str: String?
            while (buffRead.readLine().also { str = it } != null) {
                stringBuffer.append(str)
            }
            val json = stringBuffer.toString()
            //Logger.d("loadAssetsFileToTab_json$stringBuffer")
            //val infoBo = Gson().fromJson(json, TabInfoAllBo::class.java)
            //Logger.d("loadAssetsFileToTab_bean==$infoBo")
            //saveDefaultTabListInfo(infoBo, true)
        } catch (ex: IOException) {
            Logger.e("loadAssetsFileToTab_error==" + ex.message)
        } finally {
            inStr?.close()
            buffRead?.close()
        }
    }

    /**
     * 读取Assets目录下的TAB json数据
     */
    fun loadAssetsFile() : String? {
        var inStr: InputStream? = null
        var buffRead: BufferedReader? = null
        val stringBuffer = StringBuffer()
        try {
            val assetManager = AppManager.getApplication().assets
            inStr = assetManager.open("img_js.txt")
            buffRead = BufferedReader(InputStreamReader(inStr))
            var str: String?
            while (buffRead.readLine().also { str = it } != null) {
                stringBuffer.append(str)
            }
            Logger.d("loadAssetsFile_msg=${stringBuffer.toString()}")
            return stringBuffer.toString()
        } catch (ex: IOException) {
            Logger.e("loadAssetsFile_error==" + ex.message)
        } finally {
            inStr?.close()
            buffRead?.close()
        }
        return null
    }
}