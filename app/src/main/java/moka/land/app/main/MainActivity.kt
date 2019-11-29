package moka.land.app.main

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import moka.land.webview.WebViewActivity
import moka.moka.dialog.LoadingDialog
import moka.land.R
import moka.land.imagehelper.picker.builder.ImagePicker
import moka.land.permissionmanager.PermissionManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.textView01).setOnClickListener {
            PermissionManager
                .with(this)
                .setPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check { isGranted, deniedPermissions ->
                    Log.wtf("aaaaa", "isGranted: ${isGranted}, deniedPermissions: ${deniedPermissions}")
                }

//            ImagePicker
//                .with(this)
//                .showSingle {
//                    Log.wtf("moka", "it: $it")
//                }
        }

        findViewById<TextView>(R.id.textView02).setOnClickListener {
            LoadingDialog().show(supportFragmentManager)
        }

        findViewById<TextView>(R.id.textView03).setOnClickListener {
            WebViewActivity.goWebView(this, "https://naver.com", "WEB")
        }

    }

}
