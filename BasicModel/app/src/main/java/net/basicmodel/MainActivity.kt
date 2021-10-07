package net.basicmodel

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weeboos.permissionlib.PermissionRequest
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import kotlinx.android.synthetic.main.activity_main.*
import net.utils.GlideImageEngine
import net.utils.NativeShareUtil
import java.util.*

class MainActivity : AppCompatActivity() {

    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions()
    }

    private fun requestPermissions() {
        PermissionRequest.getInstance().build(this)
            .requestPermission(object : PermissionRequest.PermissionListener {
                override fun permissionGranted() {
                    initView()
                }

                override fun permissionDenied(permissions: ArrayList<String>?) {
                    finish()
                }

                override fun permissionNeverAsk(permissions: ArrayList<String>?) {
                    finish()
                }
            }, permissions)
    }

    private fun initView() {
        start.setOnClickListener {
            startActivity(Intent(this, EditorActivity::class.java))
        }
        myphoto.setOnClickListener {
            startActivity(Intent(this, AllImageActivity::class.java))
        }
        options.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
        }
        share.setOnClickListener {
            share()
        }
        camera.setOnClickListener {
            takePhoto()
        }
    }

    private fun takePhoto() {
        PictureSelector.create(this)
            .openCamera(PictureMimeType.ofImage())
            .imageEngine(GlideImageEngine())
            .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    fun share() {
        var shareMessage = "Photo Editor" + "" + "\n\nLet me recommend you this application\n\n"
        shareMessage =
            "${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        NativeShareUtil.share(this, getString(R.string.app_name), shareMessage)
    }
}