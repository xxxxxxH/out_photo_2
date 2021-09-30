package net.basicmodel

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weeboos.permissionlib.PermissionRequest
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

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

    private fun requestPermissions(){
        PermissionRequest.getInstance().build(this).requestPermission(object :PermissionRequest.PermissionListener{
            override fun permissionGranted() {
            initView()
            }

            override fun permissionDenied(permissions: ArrayList<String>?) {
                finish()
            }

            override fun permissionNeverAsk(permissions: ArrayList<String>?) {
                finish()
            }
        },permissions)
    }

    private fun initView(){
        start.setOnClickListener {
            startActivity(Intent(this,EditorActivity::class.java))
        }
    }
}