package net.adapter

import android.app.Activity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import net.basicmodel.R
import net.entity.ResourceEntity
import net.utils.ScreenManager

class OptionAdapter(val activity: Activity, layoutResId: Int, data: ArrayList<ResourceEntity>?) :
    BaseQuickAdapter<ResourceEntity, BaseViewHolder>(layoutResId, data) {
    override fun convert(holder: BaseViewHolder, item: ResourceEntity) {
        holder.getView<ImageView>(R.id.itemPip).let {
            it.layoutParams = it.layoutParams.apply {
                width = ScreenManager.get().getScreenSize(activity)[1] / 6
                height = ScreenManager.get().getScreenSize(activity)[1] / 6
            }
            Glide.with(activity).load(item.id).into(it)
        }
    }
}