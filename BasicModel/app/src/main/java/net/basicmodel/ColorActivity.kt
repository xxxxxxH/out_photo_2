package net.basicmodel

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import jp.co.cyberagent.android.gpuimage.GPUImage
import kotlinx.android.synthetic.main.layout_activity_color.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import net.adapter.EffectAdapter
import net.adapter.OptionAdapter
import net.entity.ResourceEntity
import net.entity.StickerModel
import net.event.MessageEvent
import net.utils.Constant
import net.utils.GUPUtil
import net.utils.ResourceManager
import net.widget.DrawableSticker
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ColorActivity : BaseActivity() {
    var effectData: ArrayList<ResourceEntity> = ArrayList()
    var stickerData: ArrayList<ResourceEntity> = ArrayList()
    var filterData: ArrayList<Bitmap> = ArrayList()
    var optionAdapter: OptionAdapter? = null
    var effectAdapter: EffectAdapter? = null
    var gpuImage: GPUImage? = null
    override fun getLayoutId(): Int {
        return R.layout.layout_activity_color
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        gpuImage = GPUImage(this)
        Glide.with(this).load(Constant.bitmap).into(img_main)
        initTitleBar()
        initCloseBtn()
        initEffectData()
        initFilterData()
        initStickData()
        initResAdapter()
        initBitmapAdapter()
        initRadio()
    }

    private fun initTitleBar() {
        leftTv.text = "back"
        titleTv.text = "Color"
        rightTv.text = "done"
        leftTv.setOnClickListener {
            finish()
        }
        rightTv.setOnClickListener {
            saveImg(main)
            startActivity(Intent(this,AllImageActivity::class.java))
        }
    }

    private fun initCloseBtn() {
        clear.setOnClickListener {
            rgColor.clearCheck()
            recycler_res.visibility = View.INVISIBLE
            recycler_effect.visibility = View.INVISIBLE

        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initResAdapter() {
        optionAdapter = OptionAdapter(this, R.layout.layout_item_pip, null)
        optionAdapter!!.setOnItemClickListener { adapter, view, position ->
            val entity = adapter.data[position] as ResourceEntity
            val s = StickerModel(this.resources.getDrawable(entity.id))
            val d = DrawableSticker(s.drawable)
            stickerView.addSticker(d)
        }
        recycler_res.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_res.adapter = optionAdapter
    }

    private fun initBitmapAdapter() {
        effectAdapter = EffectAdapter(this, R.layout.layout_item_pip, null)
        recycler_effect.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_effect.adapter = effectAdapter
        effectAdapter!!.setOnItemClickListener { adapter, view, position ->
            img_main.invalidate()
            val b = adapter.data[position] as Bitmap
            img_main.setImageBitmap(b)
        }
    }


    private fun initRadio() {
        rgColor.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rbe -> {
                    recycler_res.visibility = View.VISIBLE
                    recycler_effect.visibility = View.INVISIBLE
                    optionAdapter!!.setNewInstance(effectData)
                }
                R.id.rbf -> {
                    recycler_res.visibility = View.INVISIBLE
                    recycler_effect.visibility = View.VISIBLE
                    effectAdapter!!.setNewInstance(filterData)
                }
                R.id.rbt -> {
                    rgColor.clearCheck()
                    startActivity(Intent(this, TextActivity::class.java))
                }
                R.id.rbo -> {

                }
                R.id.rbs -> {
                    recycler_res.visibility = View.VISIBLE
                    recycler_effect.visibility = View.INVISIBLE
                    optionAdapter!!.setNewInstance(stickerData)
                }
            }
        }
    }

    private fun initEffectData(): ArrayList<ResourceEntity> {
        if (effectData.size == 0) {
            effectData = ResourceManager.get()
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "overlay_")
        }
        return effectData
    }

    private fun initFilterData(): ArrayList<Bitmap> {
        if (filterData.size == 0) {
            filterData.add(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.mipmap.ic_no_effect
                )
            )
            for (index in 1 until 19) {
                gpuImage!!.setImage(Constant.bitmap)
                gpuImage!!.setFilter(GUPUtil.createFilterForType(GUPUtil.getFilters().filters[index]))
                filterData.add(gpuImage!!.bitmapWithFilterApplied)
            }
        }
        return filterData
    }

    private fun initStickData(): ArrayList<ResourceEntity> {
        if (stickerData.size == 0) {
            stickerData = ResourceManager.get()
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "sticker_")
        }
        return stickerData
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MessageEvent) {
        event.getMessage()[0].let {
            colorTv.text = it.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}