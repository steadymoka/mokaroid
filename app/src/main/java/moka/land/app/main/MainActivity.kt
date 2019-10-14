package moka.land.app.main

import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import land.moka.dialog.LoadingDialog
import land.moka.dialog.TitleDialog
import moka.land.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.textView01).setOnClickListener {
            TitleDialog(this)
                .apply {
                    setCustomTitle(LayoutInflater.from(this@MainActivity).inflate(R.layout.dialog_test, null))
//                    setTitle("Test")
//                    setMessage("Test")
                    setButton(BUTTON_POSITIVE, "OK") { dialogInterface, i ->

                    }
                }
                .show()
        }

        findViewById<TextView>(R.id.textView02).setOnClickListener {
            LoadingDialog().show(supportFragmentManager)
        }
    }

}
