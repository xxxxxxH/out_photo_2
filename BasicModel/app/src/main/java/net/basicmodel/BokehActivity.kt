package net.basicmodel

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import jp.co.cyberagent.android.gpuimage.GPUImage
import kotlinx.android.synthetic.main.layout_activity_bokeh.*
import kotlinx.android.synthetic.main.layout_activity_bokeh.clear
import kotlinx.android.synthetic.main.layout_activity_bokeh.colorTv
import kotlinx.android.synthetic.main.layout_activity_bokeh.img_main
import kotlinx.android.synthetic.main.layout_activity_bokeh.main
import kotlinx.android.synthetic.main.layout_activity_bokeh.recycler_effect
import kotlinx.android.synthetic.main.layout_activity_bokeh.recycler_res
import kotlinx.android.synthetic.main.layout_activity_bokeh.stickerView
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

class BokehActivity : BaseActivity() {

    var bokehData: ArrayList<ResourceEntity> = ArrayList()
    var stickerData: ArrayList<ResourceEntity> = ArrayList()
    var effectData: ArrayList<Bitmap> = ArrayList()
    var gpuImage: GPUImage? = null
    var optionAdapter: OptionAdapter? = null
    var bitmapAdapter: EffectAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.layout_activity_bokeh
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        gpuImage = GPUImage(this)
        Glide.with(this).load(Constant.bitmap).into(img_main)
        initTitleBar()
        initBokehData()
        initStickerData()
        initEffectData()
        initResAdapter()
        initBitmapAdapter()
        initRadio()
        initCloseBtn()
    }

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
        bitmapAdapter = EffectAdapter(this, R.layout.layout_item_pip, null)
        recycler_effect.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_effect.adapter = bitmapAdapter
        bitmapAdapter!!.setOnItemClickListener { adapter, view, position ->
            img_main.invalidate()
            val b = adapter.data[position] as Bitmap
            img_main.setImageBitmap(b)
        }
    }

    private fun initRadio() {
        rgBokeh.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rbb -> {
                    recycler_res.visibility = View.VISIBLE
                    recycler_effect.visibility = View.INVISIBLE
                    optionAdapter!!.setNewInstance(bokehData)
                }
                R.id.rbe -> {
                    recycler_res.visibility = View.INVISIBLE
                    recycler_effect.visibility = View.VISIBLE
                    bitmapAdapter!!.setNewInstance(effectData)
                }
                R.id.rbt -> {
                    rgBokeh.clearCheck()
                    startActivity(Intent(this, TextActivity::class.java))
                }
                R.id.rbs -> {
                    recycler_res.visibility = View.VISIBLE
                    recycler_effect.visibility = View.INVISIBLE
                    optionAdapter!!.setNewInstance(stickerData)
                }
            }
        }
    }

    private fun initCloseBtn() {
        clear.setOnClickListener {
            recycler_res.visibility = View.INVISIBLE
            recycler_effect.visibility = View.INVISIBLE
            rgBokeh.clearCheck()
        }
    }

    private fun initTitleBar() {
        leftTv.text = "back"
        titleTv.text = "Bokeh"
        rightTv.text = "done"
        leftTv.setOnClickListener {
            finish()
        }
        rightTv.setOnClickListener {
            saveImg(main)
            startActivity(Intent(this, AllImageActivity::class.java))
        }
    }

    private fun initBokehData(): ArrayList<ResourceEntity> {
        if (bokehData.size == 0) {
            bokehData = ResourceManager.get()
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "blend")
        }
        return bokehData
    }

    private fun initStickerData(): ArrayList<ResourceEntity> {
        if (stickerData.size == 0) {
            stickerData = ResourceManager.get()
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "sticker_")
        }
        return stickerData
    }

    private fun initEffectData(): ArrayList<Bitmap> {
        if (effectData.size == 0) {
            effectData.add(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.mipmap.ic_no_effect
                )
            )
            for (index in 1 until 19) {
                gpuImage!!.setImage(Constant.bitmap)
                gpuImage!!.setFilter(GUPUtil.createFilterForType(GUPUtil.getFilters().filters[index]))
                effectData.add(gpuImage!!.bitmapWithFilterApplied)
            }
        }
        return effectData
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