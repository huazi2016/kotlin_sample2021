package com.test.base.glide.transform

import android.content.res.Resources
import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


/**
 * author : huazi
 * time   : 2021/3/13
 * desc   : 原型外框Transformation -- 暂不使用
 */
class OutTransform(borderWidth: Int, borderColor: Int) : BitmapTransformation() {

    private var mBorderPaint: Paint? = null
    private var mBorderWidth = 0f

    init {
        mBorderWidth = Resources.getSystem().displayMetrics.density * borderWidth
        mBorderPaint = Paint()
        mBorderPaint?.apply {
            this.isDither = true
            this.isAntiAlias = true
            this.color = borderColor
            this.style = Paint.Style.STROKE
            this.strokeWidth = mBorderWidth
        }
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {

    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        return circleCrop(pool, toTransform)!!
    }

    private fun circleCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
        if (source == null) {
            return null
        }
        val size =
            (source.width.coerceAtMost(source.height) - mBorderWidth / 2) as Int
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        val squared = Bitmap.createBitmap(source, x, y, size, size)
        var result: Bitmap? = pool[size, size, Bitmap.Config.ARGB_8888]
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }
        //创建画笔 画布 手动描绘边框
        val canvas = Canvas(result!!)
        val paint = Paint()
        //CLAMP 拉伸:横向的最后一个横行像素，不断的重复，纵项的那一列像素，不断的重复；
        //REPEAT 重复:就是横向、纵向不断重复这个bitmap
        //MIRROR 镜像:横向不断翻转重复，纵向不断翻转重复；
        paint.shader = BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        if (mBorderPaint != null) {
            val borderRadius: Float = r - mBorderWidth / 2
            canvas.drawCircle(r, r, borderRadius, mBorderPaint!!)
        }
        return result
    }


}