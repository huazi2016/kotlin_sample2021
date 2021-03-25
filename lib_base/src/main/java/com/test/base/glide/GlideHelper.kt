package com.test.base.glide

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.text.TextUtils
import android.webkit.MimeTypeMap
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.test.base.R
import com.test.base.glide.transform.*
import com.test.base.utils.AppManager
import com.test.base.utils.showToast
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * author : huazi
 * time   : 2021/3/13
 * desc   : glide帮助类
 */
object GlideHelper {

    /**
     * 加载普通图片
     */
    fun loadUrl(iv: AppCompatImageView, url: String?, drawId: Int) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(drawId)
            return
        }
        Glide.with(AppManager.getApplication())
            .load(url)
            .placeholder(drawId)
            .error(drawId)
            .into(iv)
    }

    /**
     * 加载普通图片
     */
    fun loadUrl02(iv: AppCompatImageView, url: String?, drawId: Int) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(drawId)
            return
        }
        Glide.with(AppManager.getApplication())
            .load(url)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .fitCenter()
            .placeholder(drawId)
            .error(drawId)
            .into(iv)
    }

    /**
     * 加载圆形图片
     * @param iv 控件
     * @param url 图片url
     */
    fun loadCircleUrl(iv: AppCompatImageView, url: String) {
        Glide.with(AppManager.getApplication())
            .load(url)
            //.circleCrop()
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(iv)
    }

    /**
     * 加载圆形图片
     * @param iv 控件
     * @param url 图片url
     * @param drawId 占位图
     */
    fun loadCircleUrl(iv: AppCompatImageView, url: String?, drawId: Int) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(drawId)
            return
        }
        Glide.with(AppManager.getApplication())
            .load(url)
            //.circleCrop()
            .placeholder(drawId)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .error(drawId)
            .into(iv)
    }

    /**
     * 加载矩形圆角图片
     * @param drawId 占位图
     * @param radius 圆角
     */
    fun loadRoundUrl(iv: AppCompatImageView, url: String, drawId: Int, radius: Int) {
        loadRoundUrl(iv, url, drawId, radius, 0)
    }

    /**
     * 加载矩形圆角图片
     * @param drawId 占位图
     * @param radius 圆角
     * @param margin 图片内部边距值
     */
    fun loadRoundUrl(iv: AppCompatImageView, url: String, drawId: Int, radius: Int, margin: Int) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(drawId)
            return
        }
        Glide.with(AppManager.getApplication())
            .load(url)
            .placeholder(drawId)
            .transform(CenterCrop(), RoundTransform(radius, margin))
            .error(drawId)
            .into(iv)
    }

    /**
     * 加载高斯模糊图片
     * @param drawId 占位图
     * @param radius 模糊值, 越大越模糊
     */
    fun loadBlurImageUrl(iv: AppCompatImageView, url: String, drawId: Int, radius: Int) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(drawId)
            return
        }
        Glide.with(AppManager.getApplication())
            .load(url)
            .placeholder(drawId)
            .transform(BlurTransform(AppManager.getApplication(), radius))
            .error(drawId)
            .into(iv)
    }

    /**
     * 加载黑白图片
     * @param drawId 占位图
     */
    fun loadGrayscaleUrl(iv: AppCompatImageView, url: String, drawId: Int) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(drawId)
            return
        }
        Glide.with(AppManager.getApplication())
            .load(url)
            .placeholder(drawId)
            .transform(GrayTransform())
            .error(drawId)
            .into(iv)
    }

    /**
     * 加载带边框(内框)图片 -- 暂不支持原形图
     * @param drawId 占位图
     * @param borderWidth 内框宽度
     * @param borderColor 内框色值
     */
    fun loadInnerBorderUrl(iv: AppCompatImageView, url: String, drawId: Int, borderWidth: Int, borderColor: Int) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(drawId)
            return
        }
        Glide.with(AppManager.getApplication())
            .load(url)
            .placeholder(drawId)
            .transform(InnerTransform(borderWidth, borderColor))
            .error(drawId)
            .into(iv)
    }

    /**
     * 加载带边框(外框)图片
     * @param drawId 占位图
     * @param borderWidth 外框宽度
     * @param borderColor 外框色值
     */
    fun loadOutsideBorderUrl(iv: AppCompatImageView, url: String, drawId: Int, borderWidth: Int, borderColor: Int) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(drawId)
            return
        }
        Glide.with(AppManager.getApplication())
            .load(url)
            .centerCrop()
            .placeholder(drawId)
            .dontAnimate()
            .skipMemoryCache(true)
            .transform(CircleTransform(AppManager.getApplication(), borderWidth, borderColor))
            .error(drawId)
            .into(iv)
    }

    /**
     * 加载图片完成后, 回调结果
     * @param requestListener 结果监听
     */
    fun loadImageCallBack(iv: AppCompatImageView, url: String, drawId: Int, requestListener: RequestListener<Drawable?>? = null) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(drawId)
            return
        }
        Glide.with(AppManager.getApplication())
            .load(url)
            .placeholder(drawId)
            .error(drawId)
            .listener(requestListener)
            //.listener(object : RequestListener<Drawable> {
            //    /**
            //     * 加载失败
            //     * @return false 未消费，继续走into(ImageView)
            //     *         true 已消费，不再继续走into(ImageView)
            //     */
            //    override fun onLoadFailed(e: GlideException?, model: Any?,
            //                              target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            //        return false
            //    }

            //    /**
            //     * 加载成功
            //     * @return false 未消费，继续走into(ImageView)
            //     *         true 已消费，不再继续走into(ImageView)
            //     */
            //    override fun onResourceReady(resource: Drawable?, model: Any?,
            //                                 target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            //        return false
            //    }
            //})
            .into(iv)
    }

    /**
     * 下载图片，并在媒体库中显示
     * @param context 上下文
     * @param imgUrl 图片url
     * @param fileName 保存文件名称, 拼接时间戳是为了确保不重复
     */
    @SuppressLint("CheckResult")
    private fun downloadImageToGallery(context: Context, imgUrl: String, fileName: String) {
        val extension = MimeTypeMap.getFileExtensionFromUrl(imgUrl)
        Observable.create(ObservableOnSubscribe { emitter: ObservableEmitter<File?> ->

            /*Glide提供了一个download() 接口来获取缓存的图片文件，但是前提必须要设置diskCacheStrategy方法的缓存策略为
            DiskCacheStrategy.ALL或者DiskCacheStrategy.SOURCE，还有download()方法需要在子线程里进行。*/
            val file = Glide.with(context).download(imgUrl).submit().get()

            //storage/emulated/0/Android/data/xxx/files/Pictures 刷新相册失效
            //val fileParentPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath

            ///storage/emulated/0/
            val fileParentPath = AppManager.getGlobalPicPath()
            Logger.d("GlideUtil_filePath==$fileParentPath")
            val appDir = File(fileParentPath)
            if (!appDir.exists()) {
                appDir.mkdirs()
            }
            //获得原文件流
            val fis = FileInputStream(file)
            //目标文件
            val targetFile = File(appDir, "$fileName.$extension")
            //输出文件流
            val fos = FileOutputStream(targetFile)
            // 缓冲数组
            val b = ByteArray(1024 * 8)
            while (fis.read(b) != -1) {
                fos.write(b)
            }
            fos.flush()
            fis.close()
            fos.close()
            //扫描媒体库, 刷新相册
            val mimeTypes = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            MediaScannerConnection.scanFile(context, arrayOf(targetFile.absolutePath), arrayOf(mimeTypes), null)
            //暂不用此方法
            //scanFile(context, Uri.parse("file://${targetFile.absolutePath}"))
            Logger.d("GlideUtil_filePath==file://${targetFile.absolutePath}")
            emitter.onNext(targetFile)
            //发送事件在io线程
        }).subscribeOn(Schedulers.io())
            //最后切换主线程提示结果
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    //onNext
                    showToast(R.string.save_img_success)})
                {
                    //onError
                    showToast(R.string.save_img_failed) }
    }

    /**
     * 扫描文件 放入 媒体库
     */
    fun scanFile(ctx: Context?, uri: Uri?) {
        if (null == ctx || null == uri) {
            return
        }
        val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        scanIntent.data = uri
        ctx.sendBroadcast(scanIntent)
    }
}