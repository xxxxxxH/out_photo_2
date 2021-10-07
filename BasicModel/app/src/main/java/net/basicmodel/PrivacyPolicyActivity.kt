package net.basicmodel

import android.view.View
import kotlinx.android.synthetic.main.layout_activity_privacy.*
import kotlinx.android.synthetic.main.layout_title_bar.*

class PrivacyPolicyActivity:BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.layout_activity_privacy
    }

    override fun initView() {
        initTitleBar()
        initWebView()
    }

    private fun initTitleBar(){
        leftTv.text = "back"
        titleTv.text = "privacy policy"
        rightTv.visibility = View.GONE
        leftTv.setOnClickListener {
            finish()
        }
    }

    private fun initWebView(){
        web.loadUrl("file:///android_asset/privacyPolicy.html")
    }
}