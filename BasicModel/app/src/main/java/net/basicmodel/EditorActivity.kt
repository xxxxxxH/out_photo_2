package net.basicmodel

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import kotlinx.android.synthetic.main.layout_activity_editor.*
import net.adapter.EditorAdapter
import net.utils.DialogManager
import net.utils.GlideImageEngine
import net.utils.ResourceManager

/**
 * Copyright (C) 2021,2021/9/30, a Tencent company. All rights reserved.
 *
 * User : v_xhangxie
 *
 * Desc :
 */
class EditorActivity : BaseActivity() {

    var data: ArrayList<String>? = null
    var editorAdapter: EditorAdapter? = null
    val dialogData = arrayListOf("TAKE PHOTO", "CHOOSE PHOTO")
    var type: String? = null

    override fun getLayoutId(): Int {
        return R.layout.layout_activity_editor
    }

    override fun initView() {
        data = ResourceManager.get().getResource(this)
        editorAdapter = EditorAdapter(R.layout.layout_item_editor, data!!)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = editorAdapter
        editorAdapter?.let {
            it.setOnItemClickListener { adapter, view, position ->
                val type = adapter.data[position].toString()
                DialogManager.get().showDialog(
                    this,
                    dialogData,
                    type
                ) { dialog, index, text ->
                    kotlin.run {
                        this.type = type
                        when (index) {
                            0 -> takePhoto()
                            1 -> choosePhoto()
                        }
                    }
                }
            }
        }
    }

    private fun takePhoto() {
        PictureSelector.create(this)
            .openCamera(PictureMimeType.ofImage())
            .imageEngine(GlideImageEngine())
            .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    private fun choosePhoto() {
        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())
            .isCamera(false)
            .maxSelectNum(1)
            .imageEngine(GlideImageEngine())
            .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            val result = PictureSelector.obtainMultipleResult(data)
            result.apply {
                val path = this[0].path
                val i = Intent(this@EditorActivity, CropActivity::class.java)
                type?.apply {
                    i.putExtra("type", this.substring(this.lastIndexOf('/') + 1))
                }
                i.putExtra("data", path)
                startActivity(i)
            }
        }
    }
}