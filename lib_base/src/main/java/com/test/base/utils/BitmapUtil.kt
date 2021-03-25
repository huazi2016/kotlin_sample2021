@file:Suppress("unused")
@file:JvmName("ImageTools")

package com.test.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import com.orhanobut.logger.Logger
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


/**
 * author : huazi
 * time   : 2020/3/13
 * desc   : bitmap工具类
 */
class BitmapUtil {
}

/**
 * drawable保存到相册
 * @param drawId 图片id
 * @param shareType 0:保存图片 1:微信分享 2:QQ分享
 */
//fun saveImgToAlbum(activity: Activity, drawId: Int, shareType: Int = 0) {
//    //先申请sd卡权限
//    PermissionUtil.requestStoragePermission(activity, false, object : OnPermissionListener {
//        override fun onGranted() {
//            val bitmap = drawableToBitmap(ResourceUtil.getDrawable(drawId)!!)
//            var isFailed = true
//            if (bitmap != null) {
//                if (saveBitmapToLocal(activity, bitmap, true)) {
//                    isFailed = false
//                    when (shareType) {
//                        1 -> {
//                            //唤起微信
//                            //val sharePop = SharePopupWindow(activity, "", "")
//                            //sharePop.shareWeixin(activity)
//                        }
//                        2 -> {
//                            //唤起QQ
//                            //val sharePop = SharePopupWindow(activity, "", "")
//                            //sharePop.shareQQImage(activity)
//                        }
//                        else -> {
//                            showToast(R.string.save_img_success)
//                        }
//                    }
//                }
//            }
//            if (isFailed) {
//                showToast(R.string.save_img_failed)
//            }
//        }
//
//        override fun onDenied() {
//            //权限被取消, Toast提示操作失败
//        }
//    })
//}

/**
 * 将 Bitmap 转换成 ByteArray
 *
 * @param bitmap [Bitmap] 对象
 * @param format Bitmap 格式
 *
 * @return ByteArray
 */
fun bitmapToBytes(bitmap: Bitmap, format: Bitmap.CompressFormat): ByteArray {
    val baos = ByteArrayOutputStream()
    bitmap.compress(format, 100, baos)
    return baos.toByteArray()
}

/**
 * 将 ByteArray 转换成 Bitmap
 *
 * @param bytes Byte 数组
 *
 * @return Bitmap
 */
fun bytesToBitmap(bytes: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

/**
 * 将 Drawable 转换成 Bitmap
 *
 * @param drawable [Drawable] 对象
 *
 * @return Bitmap
 */
fun drawableToBitmap(drawable: Drawable): Bitmap? {
    if (drawable is BitmapDrawable) {
        if (drawable.bitmap != null) {
            return drawable.bitmap
        }
    }
    val bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
        Bitmap.createBitmap(1, 1, if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888
        else Bitmap.Config.RGB_565)
    } else {
        Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888
        else Bitmap.Config.RGB_565)
    }
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

/**
 * 将 Bitmap 转换成 Drawable
 *
 * @param bitmap [Bitmap] 对象
 * @param context [Context] 对象
 *
 * @return Drawable
 */
@JvmOverloads
fun bitmapToDrawable(bitmap: Bitmap, context: Context = AppManager.getContext()): Drawable {
    return BitmapDrawable(context.resources, bitmap)
}

/**
 * 从 View 中获取 Bitmap 对象
 *
 * @param view [View] 对象
 * @param w 截取宽度 为 -1 时使用 View 宽度
 * @param h 截取高度 为 -1 时使用 View 高度
 *
 * @return Bitmap
 */
@JvmOverloads
fun getBitmapFromView(view: View, w: Int = -1, h: Int = -1): Bitmap {
    val cWidth = if (w != -1) {
        w
    } else {
        view.width
    }
    val cHeight = if (h != -1) {
        h
    } else {
        view.height
    }
    val ret = Bitmap.createBitmap(cWidth, cHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(ret)
    val bgDrawable = view.background
    if (bgDrawable != null) {
        bgDrawable.draw(canvas)
    } else {
        canvas.drawColor(Color.WHITE)
    }
    view.draw(canvas)
    return ret
}

/**
 * 通过 File 对象获取 Bitmap 对象
 *
 * @param file [File] 对象
 *
 * @return Bitmap
 */
fun getBitmapFromFile(file: File): Bitmap {
    return getBitmapFromPath(file.absolutePath)
}

/**
 * 通过文件路径获取 Bitmap 对象
 *
 * @param path 文件路径
 *
 * @return Bitmap
 */
fun getBitmapFromPath(path: String): Bitmap {
    return BitmapFactory.decodeFile(path)
}

/**
 * 将图片保存到手机
 *
 * @param activity 上下文
 * @param bitmap 图片
 * @param isSame true始终保存一张图片到相册
 *
 * @return 是否保存成功
 */
@JvmOverloads
//fun saveBitmapToLocal(activity: Activity, bitmap: Bitmap, isSame: Boolean = false): Boolean {
//    //存放路径
//    val storePath = AppManager.getGlobalPicPath()
//    val appDir = File(storePath)
//    if (!appDir.exists()) {
//        appDir.mkdir()
//    }
//    var fileName = fileNamePrefix + System.currentTimeMillis().toString() + ".jpg"
//    if (isSame) {
//        fileName = fileNamePrefix + "invite" + ".jpg"
//    }
//    val file = File(appDir, fileName)
//    try {
//        val fos = FileOutputStream(file)
//        //通过io流的方式来压缩保存图片
//        val success = bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos)
//        fos.flush()
//        fos.close()
//        //更新图片媒体库(相册)
//        updateImageMedia(activity, file)
//        return success
//    } catch (e: IOException) {
//        e.printStackTrace()
//    }
//    return false
//}

/**
 *插入图片媒体库(相册)--同个文件可以被插入多次
 */
fun insertImageMedia(activity: Activity, file: File, fileName: String) {
    val content = activity.contentResolver
    val path = file.absolutePath
    MediaStore.Images.Media.insertImage(content, path, fileName, null)
}

/**
 * 更新图片媒体库(相册)
 */
fun updateImageMedia(activity: Activity, file: File) {
    val uri = Uri.fromFile(file)
    val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
    activity.sendBroadcast(intent)
}

/**
 * 网络图片转Drawable
 * @param imageUrl 网络图片url
 * @param defaultDraw 默认本地Drawable
 */
fun imgUrlToDrawable(imageUrl: String, defaultDraw: Drawable): Drawable? {
    //var drawable = ResourceUtil.getTiDrawable(R.drawable.head_default01)
    var drawable = defaultDraw
    try {
        // 可以在这里通过文件名来判断，是否本地有此图片
        drawable = Drawable.createFromStream(URL(imageUrl).openStream(), "image.jpg")
    } catch (e: IOException) {
        Logger.d("loadImageFromNetwork", e.message)
    }
    return drawable
}

/**
 * 按比例缩放bitmap
 * @param url 图片url
 * @param scaleVal 缩放值
 * @param tip 主客队的标记
 */
fun zoomBitmap(url: String, scaleVal: Float, tip: String): Bitmap? {
    var bitmap = imgUrlToBitmap(url)
    if (bitmap != null) {
        val width = bitmap.width
        val height = bitmap.height
        val scalePx = dip2px(scaleVal)
        val scaleWidth = scalePx / width
        val scaleHeight = scalePx / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
        if (!TextUtils.isEmpty(tip)) {
            setBitmapBorder(bitmap, tip)
            //setBitmapRound(bitmap, 20f)
        }
    }
    return bitmap
}

/**
 * 设置bitmap圆角
 */
fun setBitmapRound(source: Bitmap, index: Float): Bitmap {
    try {
        val bitmap = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        //设置矩形大小
        val rect = Rect(0, 0, source.width, source.height)
        val rectf = RectF(rect)
        // 相当于清屏
        canvas.drawARGB(0, 0, 0, 0)
        //paint.color = Color.TRANSPARENT
        //画圆角
        canvas.drawRoundRect(rectf, index, index, paint)
        //canvas.drawOval(rectf, paint)
        // 取两层绘制，显示上层
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        // 把原生的图片放到这个画布上，使之带有画布的效果
        canvas.drawBitmap(source, rect, rect, paint)
        return bitmap
    } catch (e: java.lang.Exception) {
        return source
    }
}

/**
 * bitmap加边框
 * @param tip 主客队的标记
 */
fun setBitmapBorder(bitmap: Bitmap, tip: String) {
    val canvas = Canvas(bitmap)
    val rect: Rect = canvas.clipBounds
    val paint = Paint()
    //根据主客队，设置边框颜色
    paint.color = Color.parseColor(if (tip == "红队") "#2AB7CA" else "#F35463")
    paint.style = Paint.Style.STROKE
    //设置边框宽度
    paint.strokeWidth = 5f
    canvas.drawRect(rect, paint)
}

/**
 * 网络图片转换为bitmap
 * @param url 图片url
 */
fun imgUrlToBitmap(url: String): Bitmap? {
    var bitmap: Bitmap? = null
    var input: InputStream? = null
    var bis: BufferedInputStream? = null
    try {
        val iconUrl = URL(url)
        val conn = iconUrl.openConnection()
        val http = conn as HttpURLConnection
        val length = http.contentLength
        conn.connect()
        // 获得图像的字符流
        input = conn.getInputStream()
        bis = BufferedInputStream(input, length)
        bitmap = BitmapFactory.decodeStream(bis)
    } catch (e: Exception) {
        Logger.d("getBitmap==" + e.message)
    } finally {
        bis?.close()
        input?.close()
    }
    return bitmap
}

/**
 * 保存图片到指定文件
 */
suspend fun saveBitmapToFile(bitmap: Bitmap, path: String):Boolean{
    // 首先保存图片
    try{
        val fos = FileOutputStream(path)
        // 通过io流的方式来压缩保存图片
        val success = bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos)
        fos.flush()
        fos.close()
        return success
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return false
}