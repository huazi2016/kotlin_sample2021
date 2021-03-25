package com.test.base.glide.transform

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


/**
 * author : huazi
 * time   : 2021/3/13
 * desc   : 圆形Transformation
 */
class CircleTransform(context: Context, borderWidth: Int, borderColor: Int) : BitmapTransformation() {

    private var mBorderPaint: Paint? = null
    private var mBorderWidth = 0f
    private val ID = javaClass.name

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

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        return circleCrop(pool, toTransform)!!
    }

    private fun circleCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
        if (source == null) {
            return null
        }
        val size = (Math.min(source.width, source.height) - mBorderWidth / 2).toInt()
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
        paint.shader = BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        mBorderPaint?.apply {
            val borderRadius = r - mBorderWidth / 2
            canvas.drawCircle(r, r, borderRadius, this)
        }
        return result
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + mBorderWidth * 10).toByteArray(Key.CHARSET))
    }

}