package net.basicmodel

import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hjq.toast.ToastUtils
import com.tencent.mmkv.MMKV
import net.utils.Constant
import net.utils.Utils
import net.widget.MMKVUtils
import java.io.File
import java.io.FileOutputStream

/**
 * Copyright (C) 2021,2021/9/30, a Tencent company. All rights reserved.
 *
 * User : v_xhangxie
 *
 * Desc :
 */
abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        MMKV.initialize(this)
        initView()
        ToastUtils.init(this.application)
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    fun saveImg(view: View) {
        view.isDrawingCacheEnabled = true
        Constant.bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false
        val path = File(Environment.getExternalStorageDirectory().path + File.separator)
        val fileName = System.currentTimeMillis().toString()
        try {
            if (Constant.bitmap == null) return
            val imgFile = File(path, "$fileName.png")
            if (!imgFile.exists())
                imgFile.createNewFile()
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(imgFile)
                Constant.bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.close()
                MediaScannerConnection.scanFile(
                    this,
                    arrayOf(imgFile.absolutePath),
                    null,
                    object : MediaScannerConnection.MediaScannerConnectionClient {
                        override fun onMediaScannerConnected() {}
                        override fun onScanCompleted(path: String, uri: Uri) {}
                    })
                MMKVUtils.saveKeys("keys", fileName)
                MMKV.defaultMMKV()!!.encode(fileName, Utils.BitmapToBytes(Constant.bitmap))
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                fos?.flush()
                fos!!.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}