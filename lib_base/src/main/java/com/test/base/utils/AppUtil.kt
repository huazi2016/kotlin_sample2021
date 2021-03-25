package com.test.base.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.MediaStore
import android.view.Gravity
import android.view.ViewConfiguration
import android.widget.Toast
import com.orhanobut.logger.Logger
import java.io.*
import java.security.MessageDigest


/**
 * author : huazi
 * time   : 2020/9/3
 * desc   : 应用相关工具类
 */
@Suppress("unused")
object AppUtil {

    /** 算法 - MD5 */
    const val ALGORITHM_MD5 = "MD5"
    /** 算法 - SHA1 */
    const val ALGORITHM_SHA1 = "SHA1"
    /** 算法 - SHA256 */
    const val ALGORITHM_SHA256 = "SHA256"

    /**
     * 得到外部存储总空间(MB)
     */
    @JvmStatic
    fun getTotalExternalMemorySize(): Long {
        return if (hasSdcard()) {
            val path = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val totalBlocks = stat.blockCountLong
            totalBlocks * blockSize
        } else {
            -1
        }
    }

    /**
     * 得到外部存储剩余空间(MB)
     */
    @JvmStatic
    fun getAvailableExternalMemorySize(): Long {
        return if (hasSdcard()) {
            val path = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val availableBlocks = stat.availableBlocksLong
            availableBlocks * blockSize
        } else {
            -1
        }
    }

    /**
     * 将输入流写入文件
     */
    @JvmStatic
    fun isToFile(`in`: InputStream, file: File): Boolean {

        var bos: BufferedOutputStream? = null
        var bis: BufferedInputStream? = null

        return try {
            // 打开一个已存在文件的输出流
            bos = BufferedOutputStream(FileOutputStream(file))
            bis = BufferedInputStream(`in`)

            // 将输入流is写入文件输出流fos中
            val bytes = ByteArray(1024)
            var len: Int

            while (true) {
                len = bis.read(bytes, 0, bytes.size)
                if (len != -1) {
                    bos.write(bytes, 0, len)
                } else {
                    break
                }
            }
            true
        } catch (e: Exception) {
            Logger.e("AppUtil", "isToFile", e)
            false
        } finally {
            //关闭输入流等（略）
            if (bos != null) {
                try {
                    bos.close()
                } catch (e: IOException) {
                    Logger.e("AppUtil", "isToFile", e)
                }
            }
            if (bis != null) {
                try {
                    bis.close()
                } catch (e: IOException) {
                    Logger.e("AppUtil", "isToFile", e)
                }
            }
        }
    }

    /**
     * 判断是否插入SD卡，SD卡是否可用
     */
    @JvmStatic
    fun hasSdcard() =
            Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED && Environment.getExternalStorageDirectory().canWrite()

    /**
     * 获取屏幕宽度
     *
     * @return 屏幕宽度，单位：像素
     */
    @JvmStatic
    fun getScreenWidth() = getScreenWidth(AppManager.getApplication())

    /**
     * 获取屏幕宽度
     *
     * @return 屏幕宽度，单位：像素
     */
    @JvmStatic
    fun getScreenWidth(context: Context) = context.resources.displayMetrics.widthPixels

    /**
     * 获取虚拟按键的高度
     */
    @JvmStatic
    fun getNavigationBarHeight(): Int {
        val context = AppManager.getApplication()
        var result = 0
        if (hasNavBar(context)) {
            val res = context.resources
            val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId)
            }
        }
        return result
    }

    /**
     * 检查是否存在虚拟按键栏
     */
    @JvmStatic
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private fun hasNavBar(context: Context): Boolean {
        val res = context.resources
        val resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android")
        return if (resourceId != 0) {
            var hasNav = res.getBoolean(resourceId)
            // checked override flag
            val sNavBarOverride = getNavBarOverride()
            if ("1" == sNavBarOverride) {
                hasNav = false
            } else if ("0" == sNavBarOverride) {
                hasNav = true
            }
            hasNav
        } else { // fallback
            !ViewConfiguration.get(context).hasPermanentMenuKey()
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     */
    @JvmStatic
    @SuppressLint("PrivateApi")
    private fun getNavBarOverride(): String? {
        var sNavBarOverride: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                val c = Class.forName("android.os.SystemProperties")
                val m = c.getDeclaredMethod("get", String::class.java)
                m.isAccessible = true
                sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String
            } catch (e: Throwable) {
            }

        }
        return sNavBarOverride
    }

    /**
     * 获取包名
     */
    @JvmStatic
    fun getAppName(pID: Int, context: Context): String? {
        var processName: String? = null
        val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val l = am.runningAppProcesses
        val i = l.iterator()
        while (i.hasNext()) {
            val info = i.next() as ActivityManager.RunningAppProcessInfo
            try {
                if (info.pid == pID) {
                    processName = info.processName
                    return processName
                }
            } catch (e: Exception) {
                Logger.e("AppUtil", "getAppName", e)
            }

        }
        return processName
    }

    /**
     * 通过相册图片 Uri 获取图片路径
     */
    @JvmStatic
    fun getImgPathByUri(imgUri: Uri?): String {
        if (imgUri == null) {
            return ""
        }
        val context = AppManager.getApplication()
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(imgUri, filePathColumn, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()

            if (picturePath == null || picturePath == "null") {
                val toast = Toast.makeText(context, "找不到图片", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                return ""
            }
            return picturePath
        } else {
            val file = File(imgUri.path.orEmpty())
            if (!file.exists()) {
                val toast = Toast.makeText(context, "找不到图片", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                return ""

            }
            return file.absolutePath
        }
    }

    /**
     * 获取应用的签名数据
     *
     * @param context [Context] 对象
     * @param algorithm 签名算法
     *  - MD5: [ALGORITHM_MD5] or SHA1: [ALGORITHM_SHA1] or SHA256: [ALGORITHM_SHA256]
     */
    @Suppress("DEPRECATION")
    @JvmOverloads
    fun getSignature(context: Context, algorithm: String, packageName: String? = null): String {
        // 包管理器
        val pm = context.packageManager
        // 获取已安装应用列表
        val apps = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pm.getInstalledPackages(PackageManager.GET_SIGNING_CERTIFICATES)
        } else {
            pm.getInstalledPackages(PackageManager.GET_SIGNATURES)
        }

        // 获取要获取的应用的包名
        val pName = if (packageName.isNullOrBlank()) {
            context.packageName
        } else {
            packageName
        }

        // 获取应用信息对象
        val app = apps.firstOrNull { it.packageName == pName } ?: return ""

        // 获取签名信息
        val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            app.signingInfo.apkContentsSigners
        } else {
            app.signatures
        }
        if (signatures.isNullOrEmpty()) {
            // 没有签名信息
            return ""
        }
        val signature = signatures[0]

        // 获取签名数据
        val info = MessageDigest.getInstance(algorithm)
        info.update(signature.toByteArray())
        return bytesToHexString(info.digest())
    }
    /**
     * 获取当前应用名称
     */
    fun getAppName(context: Context) : String?{
        if (context == null) {
            return null;
        }
        try {
            var packageManager = context.getPackageManager();
            return packageManager.getApplicationLabel(context.getApplicationInfo()).toString();
        } catch (e: Throwable) {
            Logger.d(e.message)
        }
        return null;
    }
    fun isAvilible(context: Context, packageName: String): Boolean {
        val packageManager = context.packageManager
        // 获取所有已安装程序的包信息
        val pinfo = packageManager.getInstalledPackages(0)
        for (i in pinfo.indices) {
            if (pinfo[i].packageName.equals(packageName, ignoreCase = true)) return true
        }
        return false
    }
}


/**
 * 当前进程名称
 */
val Context.curProcessName: String?
    get() {
        val pid = android.os.Process.myPid()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in activityManager.runningAppProcesses) {
            if (appProcess.pid == pid) {
                return appProcess.processName
            }
        }
        return null
    }

/**
 * 是否是主进程
 */
val Context.isMainProcess: Boolean
    get() = packageName == curProcessName

