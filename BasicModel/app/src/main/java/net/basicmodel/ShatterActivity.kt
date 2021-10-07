package net.basicmodel

import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import kotlinx.android.synthetic.main.layout_activity_shatter.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import kotlinx.android.synthetic.main.layout_tools.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.adapter.OptionAdapter
import net.entity.ResourceEntity
import net.entity.StickerModel
import net.event.MessageEvent
import net.utils.Constant
import net.utils.ResourceManager
import net.widget.DrawableSticker
import net.widget.Filter
import net.widget.Shatter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ShatterActivity : BaseActivity() {
    var funRotate = true
    var funDRotate = true
    var stickerData: ArrayList<ResourceEntity> = ArrayList()
    var frameData: ArrayList<ResourceEntity> = ArrayList()
    var optionAdapter: OptionAdapter? = null
    var completeBitmap: Bitmap? = null
    var bitmap: Bitmap? = Constant.bitmap
    private var processOfCount = 0
    private var processOfX = 0

    companion object {
        val filter = Shatter()
        val scope = CoroutineScope(Dispatchers.Default)
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_activity_shatter
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        Glide.with(this).load(Constant.bitmap).into(img_main)
        initTitleBar()
        initCloseBtn()
        initStickerData()
        initFrameData()
        initResAdapter()
        initRadio()
        initTools()
    }

    private fun initTitleBar() {
        leftTv.text = "back"
        titleTv.text = "shatter"
        rightTv.text = "done"
        leftTv.setOnClickListener {
            finish()
        }
        rightTv.setOnClickListener {
            saveImg(main)
            startActivity(Intent(this, AllImageActivity::class.java))
        }
    }


    private fun initRadio() {
        rgShatter.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rbt -> {
                    recycler_res.visibility = View.INVISIBLE
                    tools.visibility = View.VISIBLE
                }
                R.id.rbf -> {
                    recycler_res.visibility = View.VISIBLE
                    tools.visibility = View.INVISIBLE
                    optionAdapter!!.setNewInstance(frameData)
                    optionAdapter!!.setOnItemClickListener { adapter, view, position ->
                        val entity = adapter.data[position] as ResourceEntity
                        Glide.with(this).load(entity.id).into(img_frame)
                    }
                }
                R.id.rbtxt -> {
                    rgShatter.clearCheck()
                    startActivity(Intent(this, TextActivity::class.java))
                }
                R.id.rbs -> {
                    recycler_res.visibility = View.VISIBLE
                    tools.visibility = View.INVISIBLE
                    optionAdapter!!.setNewInstance(stickerData)
                    optionAdapter!!.setOnItemClickListener { adapter, view, position ->
                        val entity = adapter.data[position] as ResourceEntity
                        val s = StickerModel(this.resources.getDrawable(entity.id))
                        val d = DrawableSticker(s.drawable)
                        stickerView.addSticker(d)

                    }
                }
            }
        }
    }

    private fun initStickerData(): ArrayList<ResourceEntity> {
        if (stickerData.size == 0) {
            stickerData = ResourceManager.get()
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "sticker_")
        }
        return stickerData
    }

    private fun initFrameData(): ArrayList<ResourceEntity> {
        if (frameData.size == 0) {
            frameData = ResourceManager.get()
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "frameshatter_")
        }
        return frameData
    }

    private fun initResAdapter() {
        optionAdapter = OptionAdapter(this, R.layout.layout_item_pip, null)

        recycler_res.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_res.adapter = optionAdapter
    }

    private fun initCloseBtn() {
        clear.setOnClickListener {
            rgShatter.clearCheck()
            recycler_res.visibility = View.INVISIBLE
            tools.visibility = View.INVISIBLE
        }
    }

    private fun initTools() {
        rotateLayout.setOnClickListener {
            if (funRotate) {
                img_rotate.setImageResource(R.mipmap.box)
                funRotate = false
                filter.boolPar[0] =
                    Filter.BoolParameter("Rotate Blocks", java.lang.Boolean.FALSE)
                changeStyleAsyncTask()
            } else {
                funRotate = true
                img_rotate.setImageResource(R.mipmap.rotate)
                filter.boolPar[0] =
                    Filter.BoolParameter("Rotate Blocks", java.lang.Boolean.TRUE)
                changeStyleAsyncTask()
            }
        }
        threed_rotateLayout.setOnClickListener {
            if (funDRotate) {
                img_threerotate.setImageResource(R.mipmap.box)
                funDRotate = false
                filter.boolPar[1] =
                    Filter.BoolParameter("Shattered Blocks", java.lang.Boolean.FALSE)
                changeStyleAsyncTask()
            } else {
                funDRotate = true
                img_threerotate.setImageResource(R.mipmap.rotate)
                filter.boolPar[1] = Filter.BoolParameter("Shattered Blocks", java.lang.Boolean.TRUE)
                changeStyleAsyncTask()
            }
        }
        findViewById<IndicatorSeekBar>(R.id.seekbarcount).onSeekChangeListener =
            object : OnSeekChangeListener {
                override fun onSeeking(seekParams: SeekParams) {
                    processOfCount = seekParams.progress
                }

                override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}
                override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {
                    filter.intPar[0] = Filter.IntParameter("Count", processOfCount, 2, 100)
                    changeStyleAsyncTask()
                }
            }
        findViewById<IndicatorSeekBar>(R.id.seekbarx).onSeekChangeListener =
            object : OnSeekChangeListener {
                override fun onSeeking(seekParams: SeekParams) {
                    processOfX = seekParams.progress
                }

                override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}
                override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {
                    filter.intPar[1] = Filter.IntParameter("X", "%", processOfX, 0, 100)
                    changeStyleAsyncTask()
                }
            }
    }

    private fun changeStyleAsyncTask() {
        Thread(Runnable {
            runOnUiThread {
                completeBitmap = filter.Apply(bitmap)

                img_main.setImageBitmap(completeBitmap)
            }

        }).start()
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