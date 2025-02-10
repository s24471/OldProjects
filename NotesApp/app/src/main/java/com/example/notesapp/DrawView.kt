package com.example.notesapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        strokeWidth = 10f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }
    private var drawPath: Path = Path()
    private val paths = mutableListOf<Pair<Path, Paint>>()
    private var bitmap: Bitmap? = null
    private var scaledBitmap: Bitmap? = null
    private var bitmapMatrix: Matrix = Matrix()

    fun setBitmap(bmp: Bitmap) {
        bitmap = bmp
        if (width > 0 && height > 0) {
            scaleBitmapToFitView()
        }
        invalidate()
    }

    private fun scaleBitmapToFitView() {
        bitmap?.let {
            val viewWidth = width.toFloat()
            val viewHeight = height.toFloat()
            val bitmapWidth = it.width.toFloat()
            val bitmapHeight = it.height.toFloat()
            val scale = Math.min(viewWidth / bitmapWidth, viewHeight / bitmapHeight)
            val dx = (viewWidth - bitmapWidth * scale) / 2
            val dy = (viewHeight - bitmapHeight * scale) / 2
            bitmapMatrix.setScale(scale, scale)
            bitmapMatrix.postTranslate(dx, dy)
            scaledBitmap = Bitmap.createBitmap(it, 0, 0, it.width, it.height, bitmapMatrix, true)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        scaledBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }
        paths.forEach {
            canvas.drawPath(it.first, it.second)
        }
        canvas.drawPath(drawPath, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                drawPath.moveTo(event.x, event.y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                drawPath.lineTo(event.x, event.y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                val newPaint = Paint(paint)
                paths.add(Pair(Path(drawPath), newPaint))
                drawPath.reset()
                invalidate()
            }
        }
        return true
    }

    fun clear() {
        paths.clear()
        drawPath.reset()
        invalidate()
    }

    fun save(): Bitmap? {
        if (scaledBitmap == null) return null
        val bmp = Bitmap.createBitmap(scaledBitmap!!.width, scaledBitmap!!.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawBitmap(scaledBitmap!!, 0f, 0f, null)
        paths.forEach {
            canvas.drawPath(it.first, it.second)
        }
        return bmp
    }

    companion object {
        fun rotateBitmapIfRequired(context: Context, bitmap: Bitmap, photoPath: String): Bitmap {
            val exif = androidx.exifinterface.media.ExifInterface(photoPath)
            val orientation = exif.getAttributeInt(
                androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION,
                androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL
            )

            return when (orientation) {
                androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_90 -> {
                    rotateBitmap(bitmap, 90)
                }
                androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_180 -> {
                    rotateBitmap(bitmap, 180)
                }
                androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_270 -> {
                    rotateBitmap(bitmap, 270)
                }
                else -> {
                    bitmap
                }
            }
        }

        private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
            val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
    }
}
