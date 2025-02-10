package com.example.notesapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.io.FileOutputStream
import java.io.IOException

class DrawActivity : AppCompatActivity() {

    private lateinit var drawView: DrawView
    private var photoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw)

        drawView = findViewById(R.id.draw_view)
        val saveButton = findViewById<Button>(R.id.button_save_photo)
        val clearButton = findViewById<Button>(R.id.button_clear_drawing)

        photoPath = intent.getStringExtra("photoPath")

        drawView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                photoPath?.let {
                    val bitmap = BitmapFactory.decodeFile(it)
                    val rotatedBitmap = DrawView.rotateBitmapIfRequired(this@DrawActivity, bitmap, it)
                    drawView.setBitmap(rotatedBitmap)
                }
                drawView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        saveButton.setOnClickListener {
            drawView.save()?.let { bmp ->
                photoPath?.let {
                    saveBitmap(bmp, it)
                }
                setResult(RESULT_OK)
                finish()
            }
        }

        clearButton.setOnClickListener {
            drawView.clear()
        }
    }

    private fun saveBitmap(bmp: Bitmap, path: String) {
        try {
            FileOutputStream(path).use { out ->
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
