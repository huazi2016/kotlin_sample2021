package com.test.base.log

import android.content.Context
import android.os.Build
import com.test.base.utils.*
import com.orhanobut.logger.Logger
import com.tencent.mmkv.MMKV
import java.io.*
import java.util.*


/**
 * author : huazi
 * time   : 2021/1/27
 * desc   : 日志上传工具类
 */
object LogUtil {
    private const val TAG = "LogUtil"
    /** 日志升级版本, 每次优化建议自增1*/
    private const val LOG_VERSION = "1.0.0"
    /** 日志存放路径*/
    private var logPath: String = ""
    /** 分割线*/
    private const val DIVIDER_LINE = "***************************"

    /**
     * init 创建存放路径
     * @desc: 最好在Application创建时调用
     */
    fun initLog(context: Context) {
        val path = context.getExternalFilesDir("").toString()
        logPath = path + File.separator + "tiLog"
        val fileLog = File(logPath)
        if (!fileLog.exists()) {
            fileLog.mkdirs()
            Logger.d("$TAG--文件夹不存在")
        }
        //基本信息
        if (MMKV.defaultMMKV().decodeInt("crash_log") == 0) {
            //仅执行一次
            setLog(getLogHeader())
            MMKV.defaultMMKV().encode("crash_log", 1)
        }
    }

    /**
     * 写入日志
     * @param errMsg 具体内容
     */
    fun setLog(errMsg: String) {
        try {
            writeToFile(errMsg)
        } catch (e: Exception) {
            Logger.d(TAG + "-errMsg=" + e.message)
        }
    }

    /**
     * 将log信息写入文件中
     * @param errMsg 具体内容
     */
    private fun writeToFile(errMsg: String) {
        if (logPath.isEmpty()) {
            Logger.d("$TAG-errMsg=日志写入失败")
            return
        }
        val fileName = logPath + File.separator + "nami_log" + ".txt"
        clearCaches(fileName)
        if (!File(logPath).exists()) {
            File(logPath).mkdirs()
        }
        //日志命名规则:时间+日志类型+key+内容
        val currentTimeMillis = System.currentTimeMillis()
        val time = TimeTiUtil.formatTimeMilli(currentTimeMillis, TIME_STYLE01)
        val log: String = "$time--$errMsg\n"
        //var fos: FileOutputStream? = null
        var bw: BufferedWriter? = null
        try {
            //FileOutputStream写入方式, 中文会出现乱码
            //fos = FileOutputStream(fileName, true)
            //bw = BufferedWriter(OutputStreamWriter(fos, "utf-8"))
            //这里的第二个参数代表追加还是覆盖，true为追加，flase为覆盖
            bw = BufferedWriter(FileWriter(fileName, true))
            bw.write(log)
        } catch (e: FileNotFoundException) {
            Logger.d("writeToFile--errMsg01==${e.message}")
        } catch (e: IOException) {
            Logger.d("writeToFile--errMsg02==${e.message}")
        } finally {
            try {
                bw?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 组装基本信息
     */
    private fun getLogHeader(): String {
        val curTime =
            TimeTiUtil.formatTimeMilli(System.currentTimeMillis(), TIME_STYLE07)
        val abi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Arrays.toString(Build.SUPPORTED_ABIS)
        } else {
            Build.CPU_ABI
        }
        val versionCode = getAPPVersionCode()
        val versionName = getAPPVersionName()
        return "\n"+ DIVIDER_LINE + "\n"+
                "nami_report: " + LOG_VERSION + "\n" +
                "Create time: '" + curTime + "'\n"+
                "App version: '"+ versionName + "'\n" +
                "API level: '" + Build.VERSION.SDK_INT + "'\n" +
                "OS version: '" + Build.VERSION.RELEASE + "'\n" +
                "ABI list: '" + abi + "'\n" +
                "Manufacturer: '" + Build.MANUFACTURER + "'\n" +
                "Brand: '" + Build.BRAND + "'\n" +
                "Model: '" + Build.MODEL + "'\n" +
                DIVIDER_LINE + "\n"
    }

    /**
     * 清空缓存目录
     * @param Path 文件路径
     */
    private fun clearCaches(Path: String) {
        val file = File(Path)
        //限制5M
        if (file.length() / 1024 > 1024 * 5) {
            file.delete()
        }
    }
}