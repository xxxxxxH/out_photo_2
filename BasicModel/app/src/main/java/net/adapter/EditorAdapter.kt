package net.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import net.basicmodel.R

/**
 * Copyright (C) 2021,2021/9/30, a Tencent company. All rights reserved.
 *
 * User : v_xhangxie
 *
 * Desc :
 */
class EditorAdapter(layoutResId: Int, data: ArrayList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {
    override fun convert(holder: BaseViewHolder, item: String) {
        Glide.with(context).load(item).into(holder.getView(R.id.itemEditor))
    }
}