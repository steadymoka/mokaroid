package moka.land.app.main

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import moka.moka.dialog._BaseDialog
import moka.land.R
import moka.land.databinding.DialogTestBinding

class TestDialog : _BaseDialog() {

    var onClickOk: ((test: String) -> Unit)? = null

    private lateinit var binding: DialogTestBinding

    override fun getContentView(): View {
        return DataBindingUtil.inflate<DialogTestBinding>(LayoutInflater.from(context), R.layout.dialog_test, null, false)
            .apply { binding = this }.root
    }

    override fun init() {
        onClickPositive = { onClickOk?.invoke("test") }
        binding.textViewDream.text = "teeeeeeeeest"
    }

    override fun getPositiveText(): CharSequence = "프리미엄 구매"

    override fun isNeutralText(): Boolean = false

    override fun isNegativeText(): Boolean = false

}
