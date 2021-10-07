package net.basicmodel

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.hjq.toast.ToastUtils
import com.isseiaoki.simplecropview.CropImageView
import com.isseiaoki.simplecropview.callback.CropCallback
import com.isseiaoki.simplecropview.callback.SaveCallback
import kotlinx.android.synthetic.main.layout_activity_crop.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import net.event.MessageEvent
import net.utils.Constant
import net.utils.GlideImageEngine
import net.utils.Utils
import org.greenrobot.eventbus.EventBus
import java.io.File

/**
 * Copyright (C) 2021,2021/9/30, a Tencent company. All rights reserved.
 *
 * User : v_xhangxie
 *
 * Desc :
 */
class CropActivity : BaseActivity(), SaveCallback, CropCallback {

    var type = ""

    override fun getLayoutId(): Int {
        return R.layout.layout_activity_crop
    }

    override fun initView() {
        leftTv.text = "back"
        titleTv.text = "crop"
        rightTv.text = "done"
        val i = intent
        type = i.getStringExtra("type").toString()
        Log.i("xxxxxxH", "type = $type")
        val data = i.getStringExtra("data")
        data?.let {
            GlideImageEngine().loadImage(this, it, cropImageView)
        }
        cropImageView.setCropMode(CropImageView.CropMode.FREE)
        radio_group.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.radio_square -> cropImageView.setCropMode(CropImageView.CropMode.SQUARE)
                R.id.radio_circle -> cropImageView.setCropMode(CropImageView.CropMode.CIRCLE)
                R.id.radio_34 -> cropImageView.setCropMode(CropImageView.CropMode.RATIO_3_4)
                R.id.radio_43 -> cropImageView.setCropMode(CropImageView.CropMode.RATIO_4_3)
                R.id.radio_916 -> cropImageView.setCropMode(CropImageView.CropMode.RATIO_9_16)
                R.id.radio_169 -> cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9)
                R.id.radio_77 -> cropImageView.setCustomRatio(7, 5)
            }
        }
        free.setOnClickListener {
            radio_group.clearCheck()
            cropImageView.setCropMode(CropImageView.CropMode.FREE)
        }
        fit.setOnClickListener {
            radio_group.clearCheck()
            cropImageView.setCropMode(CropImageView.CropMode.FIT_IMAGE)
        }
        buttonRotateLeft.setOnClickListener {
            cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D)
        }
        buttonRotateRight.setOnClickListener {
            cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D)
        }
        leftTv.setOnClickListener {
            finish()
        }
        rightTv.setOnClickListener {
            cropImageView.startCrop(Uri.fromFile(File(this.cacheDir, "cropped")), this, this)
        }
    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
    }

    override fun onSuccess(cropped: Bitmap?) {
        Constant.bitmap = cropped
        when (type) {
            "pip" -> {
                startActivity(Intent(this, PipActivity::class.java))
            }
            "color" ->{
                startActivity(Intent(this, ColorActivity::class.java))
            }
            "bokeh" -> {
                startActivity(Intent(this, BokehActivity::class.java))
            }
            "pixel" -> {
                startActivity(Intent(this, PixelActivity::class.java))
            }
            "shattering" -> {
                startActivity(Intent(this, ShatterActivity::class.java))
            }
        }
    }

    override fun onSuccess(uri: Uri?) {
        ToastUtils.show("success")
    }

}