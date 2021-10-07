package net.adapter

import android.app.Activity
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import net.basicmodel.R
import net.utils.ScreenManager

class EffectAdapter(val activity: Activity,layoutResId: Int, data: ArrayList<Bitmap>?) :
    BaseQuickAdapter<Bitmap, BaseViewHolder>(layoutResId, data) {
    override fun convert(holder: BaseViewHolder, item: Bitmap) {
        holder.getView<ImageView>(R.id.itemPip).let {
            it.layoutParams = it.layoutParams.apply {
                width = ScreenManager.get().getScreenSize(activity)[1] / 6
                height = ScreenManager.get().getScreenSize(activity)[1] / 6
            }
            Glide.with(activity).load(item).into(it)
        }
    }
}