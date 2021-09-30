package net.basicmodel

import android.content.Intent
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.layout_activity_editor.*
import net.adapter.EditorAdapter
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
                startActivity(Intent(this,CropActivity::class.java))
            }
        }
    }

}