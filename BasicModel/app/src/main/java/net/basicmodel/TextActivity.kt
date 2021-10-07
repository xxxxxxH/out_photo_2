package net.basicmodel

import android.view.View
import kotlinx.android.synthetic.main.layout_activity_text.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import net.event.MessageEvent
import org.greenrobot.eventbus.EventBus

class TextActivity:BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.layout_activity_text
    }

    override fun initView() {
        leftTv.visibility = View.INVISIBLE
        titleTv.visibility = View.INVISIBLE
        rightTv.text = "done"
        rightTv.setOnClickListener {
            EventBus.getDefault().post(MessageEvent(editText.text.toString()))
            finish()
        }
    }
}