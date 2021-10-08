package net.basicmodel

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import jp.co.cyberagent.android.gpuimage.GPUImage
import kotlinx.android.synthetic.main.layout_activity_pip.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import net.adapter.EffectAdapter
import net.adapter.OptionAdapter
import net.entity.ResourceEntity
import net.entity.StickerModel
import net.utils.Constant
import net.utils.GUPUtil
import net.utils.ResourceManager
import net.widget.DrawableSticker

class PipActivity : BaseActivity() {
    var pipData: ArrayList<ResourceEntity> = ArrayList()
    var stickerData: ArrayList<ResourceEntity> = ArrayList()
    var backgroundData: ArrayList<Bitmap> = ArrayList()
    var forcegoundData: ArrayList<Bitmap> = ArrayList()
    var optionAdapter: OptionAdapter? = null
    var bitmapAdapter: EffectAdapter? = null
    var gpuImage: GPUImage? = null
    override fun getLayoutId(): Int {
        return R.layout.layout_activity_pip
    }

    override fun initView() {
        gpuImage = GPUImage(this)
        Glide.with(this).load(Constant.bitmap).into(img_main)
        initTitleBar()
        initCloseBtn()
        initResAdapter()
        initBitmapAdapter()
        initPipData()
        initBackgroundData()
        initForcegoundData()
        initStickerData()
        initRadio()
    }

    private fun initTitleBar() {
        leftTv.text = "back"
        titleTv.text = "pip"
        rightTv.text = "done"
        leftTv.setOnClickListener {
            finish()
        }
        rightTv.setOnClickListener {
            saveImg(main)
            startActivity(Intent(this, AllImageActivity::class.java))
        }
    }

    private fun initCloseBtn() {
        clear.setOnClickListener {
            rgPip.clearCheck()
            recycler_res.visibility = View.INVISIBLE
            recycler_effect.visibility = View.INVISIBLE
        }
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

    private fun initPipData(): ArrayList<ResourceEntity> {
        if (pipData.size == 0) {
            pipData = ResourceManager.get()
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "framepip_")
        }
        return pipData
    }

    private fun initBackgroundData(): ArrayList<Bitmap> {
        if (backgroundData.size == 0) {
            backgroundData.add(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.mipmap.ic_no_effect
                )
            )
            for (index in 1 until 19) {
                gpuImage!!.setImage(Constant.bitmap)
                gpuImage!!.setFilter(GUPUtil.createFilterForType(GUPUtil.getFilters().filters[index]))
                backgroundData.add(gpuImage!!.bitmapWithFilterApplied)
            }
        }
        return backgroundData
    }

    private fun initForcegoundData(): ArrayList<Bitmap> {
        if (forcegoundData.size == 0) {
            forcegoundData.add(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.mipmap.ic_no_effect
                )
            )
            for (index in 1 until 19) {
                gpuImage!!.setImage(Constant.bitmap)
                gpuImage!!.setFilter(GUPUtil.createFilterForType(GUPUtil.getFilters().filters[index]))
                forcegoundData.add(gpuImage!!.bitmapWithFilterApplied)
            }
        }
        return forcegoundData
    }

    private fun initStickerData(): ArrayList<ResourceEntity> {
        if (stickerData.size == 0) {
            stickerData = ResourceManager.get()
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "sticker_")
        }
        return stickerData
    }

    private fun initRadio() {
        rgPip.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rbp -> {
                    recycler_res.visibility = View.VISIBLE
                    recycler_effect.visibility = View.INVISIBLE
                    optionAdapter!!.setNewInstance(pipData)
                }
                R.id.rbb -> {
                    recycler_res.visibility = View.INVISIBLE
                    recycler_effect.visibility = View.VISIBLE
                    bitmapAdapter!!.setNewInstance(backgroundData)
                }
                R.id.rbf -> {
                    recycler_res.visibility = View.INVISIBLE
                    recycler_effect.visibility = View.VISIBLE
                    bitmapAdapter!!.setNewInstance(forcegoundData)
                }
                R.id.rbs -> {
                    recycler_res.visibility = View.VISIBLE
                    recycler_effect.visibility = View.INVISIBLE
                    optionAdapter!!.setNewInstance(stickerData)
                }
            }
        }
    }
}