package moka.land.app.main

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import land.moka.dialog.LoadingDialog
import moka.land.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.textView01).setOnClickListener {
            TestDialog()
                .apply {
                    onClickOk = {
                        Log.wtf("tag", "${it}")
                    }
                }
                .show(supportFragmentManager, "")
        }

        findViewById<TextView>(R.id.textView02).setOnClickListener {
            LoadingDialog().show(supportFragmentManager)
        }
    }

}
