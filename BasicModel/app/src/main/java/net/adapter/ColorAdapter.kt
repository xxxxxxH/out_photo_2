package net.adapter

import android.app.Activity
import android.graphics.Color
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import net.basicmodel.R
import net.utils.ScreenManager

class ColorAdapter(val activity: Activity, layoutResId: Int, data: ArrayList<String>?) :
    BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.getView<ImageView>(R.id.itemPip).let {
            it.layoutParams = it.layoutParams.apply {
                width = ScreenManager.get().getScreenSize(activity)[1] / 6
                height = ScreenManager.get().getScreenSize(activity)[1] / 6
            }
            it.setBackgroundColor(Color.parseColor(item))
        }
    }
}