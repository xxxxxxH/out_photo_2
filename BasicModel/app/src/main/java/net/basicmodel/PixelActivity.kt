package net.basicmodel

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import jp.co.cyberagent.android.gpuimage.GPUImage
import kotlinx.android.synthetic.main.layout_activity_pix.*
import kotlinx.android.synthetic.main.layout_activity_pix.clear
import kotlinx.android.synthetic.main.layout_activity_pix.colorTv
import kotlinx.android.synthetic.main.layout_activity_pix.img_main
import kotlinx.android.synthetic.main.layout_activity_pix.main
import kotlinx.android.synthetic.main.layout_activity_pix.recycler_effect
import kotlinx.android.synthetic.main.layout_activity_pix.recycler_res
import kotlinx.android.synthetic.main.layout_activity_pix.stickerView
import kotlinx.android.synthetic.main.layout_title_bar.*
import net.adapter.ColorAdapter
import net.adapter.OptionAdapter
import net.entity.ResourceEntity
import net.entity.StickerModel
import net.event.MessageEvent
import net.utils.Constant
import net.utils.ResourceManager
import net.widget.DrawableSticker
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class PixelActivity : BaseActivity() {

    var effectData: ArrayList<ResourceEntity> = ArrayList()
    var stickerData: ArrayList<ResourceEntity> = ArrayList()
    var colorData:ArrayList<String> = ArrayList()
    var gpuImage: GPUImage? = null
    var optionAdapter: OptionAdapter? = null
    var colorAdapter:ColorAdapter?=null

    override fun getLayoutId(): Int {
        return R.layout.layout_activity_pix
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        gpuImage = GPUImage(this)
        Glide.with(this).load(Constant.bitmap).into(img_main)
        initTitleBar()
        initEffectData()
        initColorData()
        initStickerData()
        initResAdapter()
        initColorAdapter()
        initCloseBtn()
        initRadio()
    }


    private fun initTitleBar() {
        leftTv.text = "back"
        titleTv.text = "Pixel"
        rightTv.text = "done"
        leftTv.setOnClickListener {
            finish()
        }
        rightTv.setOnClickListener {
            saveImg(main)
            startActivity(Intent(this, AllImageActivity::class.java))
        }
    }

    private fun initEffectData(): ArrayList<ResourceEntity> {
        if (effectData.size == 0) {
            effectData = ResourceManager.get()
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "pixel_")
        }
        return effectData
    }

    private fun initColorData():ArrayList<String> {
        if (colorData.size == 0){
            colorData = this.resources.getStringArray(R.array.color).toList() as ArrayList<String>
        }
        return colorData
    }

    private fun initStickerData(): ArrayList<ResourceEntity> {
        if (stickerData.size == 0) {
            stickerData = ResourceManager.get()
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "sticker_")
        }
        return stickerData
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

    private fun initColorAdapter(){
        colorAdapter = ColorAdapter(this,R.layout.layout_item_pip,null)
        colorAdapter!!.setOnItemClickListener { adapter, view, position ->
            stickerView.changeBackgroundColor(adapter.data[position] as String?)
        }
        recycler_effect.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_effect.adapter = colorAdapter
    }

    private fun initRadio() {
        rgPix.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rbe -> {
                    recycler_res.visibility = View.VISIBLE
                    recycler_effect.visibility = View.INVISIBLE
                    optionAdapter!!.setNewInstance(effectData)
                }
                R.id.rbc -> {
                    recycler_res.visibility = View.INVISIBLE
                    recycler_effect.visibility = View.VISIBLE
                    colorAdapter!!.setNewInstance(colorData)
                }
                R.id.rbt -> {
                    rgPix.clearCheck()
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
            rgPix.clearCheck()
            recycler_res.visibility = View.INVISIBLE
            recycler_effect.visibility = View.INVISIBLE

        }
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