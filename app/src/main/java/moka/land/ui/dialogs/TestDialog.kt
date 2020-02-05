package moka.land.ui.dialogs

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import moka.land.R
import moka.land.dialog._BaseDialog

class TestDialog : _BaseDialog() {

    override fun getPositiveText() = "닫기"

    override fun getWidthRatio(): Float? = 0.9f

    override fun isNoButton(): Boolean = true

    override fun getContentView(): View {
        return LayoutInflater.from(context).inflate(R.layout.dialog_test, null)
    }

    override fun init() {
    }

    fun showDialog(manager: FragmentManager, listener: () -> Unit) {
        show(manager, "TestDialog")
    }

}
