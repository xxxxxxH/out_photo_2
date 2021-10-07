package net.basicmodel

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.layout_activity_all.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import net.adapter.ViewPagerAdapter
import net.entity.ImgEntity
import net.utils.Utils
import java.io.File

class AllImageActivity : BaseActivity() {

    var data: ArrayList<ImgEntity> = ArrayList()
    var pagerAdapter: ViewPagerAdapter? = null
    override fun getLayoutId(): Int {
        return R.layout.layout_activity_all
    }

    override fun initView() {
        initTitleBar()
        initViewpager()
        initViewClick()
    }

    private fun initViewClick() {
        iv_facebook_share.setOnClickListener {
            try {
                if (isInstalledApp("com.facebook.katana")) {
                    share("com.facebook.katana")
                } else {
                    val appPackageName = "com.facebook.katana"
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW, Uri.parse(
                                    "market://details?id=$appPackageName"
                                )
                            )
                        )
                    } catch (anfe: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW, Uri.parse(
                                    "https://play.google.com/store/apps/details?id=$appPackageName"
                                )
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        iv_instagram_share.setOnClickListener {
            try {
                if (isInstalledApp("com.instagram.android")) {
                    share("com.instagram.android")
                } else {
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=com.instagram.android")
                            )
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=com.instagram.android")
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        iv_email_share.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:") // only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
                intent.putExtra(
                    Intent.EXTRA_STREAM,
                    Uri.fromFile(File(Environment.getExternalStorageDirectory().path + File.separator + data[pager.currentItem].key + ".png"))
                )

                intent.putExtra(
                    Intent.EXTRA_TEXT, """Make more pics with app link 
     https://play.google.com/store/apps/details?id=$packageName"""
                )
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(Intent.createChooser(intent, "Share Picture"))
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Mail app have not been installed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        iv_whatsup_share.setOnClickListener {
            try {
                if (isInstalledApp("com.whatsapp")) {
                    share("com.whatsapp")
                } else Toast.makeText(
                    applicationContext,
                    "Whatsapp have not been installed",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        iv_share_image.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
                intent.type = "image/jpeg"
                intent.putExtra(
                    Intent.EXTRA_TEXT, """Make more pics with app link 
     https://play.google.com/store/apps/details?id=$packageName"""
                )
                intent.putExtra(
                    Intent.EXTRA_STREAM,
                    Uri.fromFile(File(Environment.getExternalStorageDirectory().path + File.separator + data[pager.currentItem].key + ".png"))
                )
                startActivity(Intent.createChooser(intent, "Share Picture"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initTitleBar() {
        leftTv.text = "back"
        titleTv.text = "all"
        rightTv.text = "delete"
        leftTv.setOnClickListener {
            finish()
        }
        rightTv.setOnClickListener {
            if (data.size > 0) {
                MMKV.defaultMMKV()!!.remove(data[pager.currentItem].key)
                data.removeAt(pager.currentItem)
                pagerAdapter!!.notifyDataSetChanged()
                pager.adapter = pagerAdapter
                if (data.size > 0) {
                    pager.setCurrentItem(0, true)
                    countTv.text = "1 / ${data.size}"
                } else {
                    countTv.text = ""
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViewpager() {
        val keySet = MMKV.defaultMMKV()!!.decodeStringSet("keys") as HashSet?
        if (keySet != null) {
            for (item in keySet) {
                MMKV.defaultMMKV()!!.decodeBytes(item)?.let {
                    val entity = ImgEntity(item, Utils.BytesToBitmap(it))
                    data.add(entity)
                }
            }
        }
        if (data.size > 0) {
            countTv.text = "1 / ${data.size}"
            pagerAdapter = ViewPagerAdapter(this, data)
            pager.adapter = pagerAdapter
            pager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    countTv.text = "${(position + 1)} / ${data.size}"
                }

                override fun onPageScrollStateChanged(state: Int) {

                }

            })
        }
    }

    private fun isInstalledApp(uri: String): Boolean {
        val pm = packageManager
        return try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun share(packageNames: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.setPackage(packageNames)
        val uri =
            Uri.fromFile(File(Environment.getExternalStorageDirectory().path + File.separator + data[pager.currentItem].key + ".png"))
        share.putExtra(
            Intent.EXTRA_STREAM,
            uri
        )
        share.putExtra(
            Intent.EXTRA_TEXT, """Make more pics with app link 
                                         https://play.google.com/store/apps/details?id=$packageName"""
        )
        share.type = "image/jpeg"
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(share, "Share Picture"))
    }
}