package moka.land.ui.dialogs

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moka.land.R
import moka.land.dialog._BaseDialog

class TestDialog : _BaseDialog() {

    override fun getPositiveText() = "닫기"

    override fun getWidthRatio(): Float? = 0.9f

    override fun hideOnClick(): Boolean = true

    override fun getContentView(): View {
        return LayoutInflater.from(context).inflate(R.layout.dialog_test, null)
    }

    override fun init() {
        lifecycleScope.launch {
            setPositiveEnabled(false)
            delay(4000)
            setPositiveEnabled(true)
        }
    }

}
