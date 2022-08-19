package moka.land.ui.dialogs

import android.view.View
import androidx.core.content.ContextCompat
import moka.land.R
import moka.land.databinding.DialogTestBinding
import moka.land.dialog._BaseDialog

class ButtonTestDialog : _BaseDialog() {

    private val _view by lazy { DialogTestBinding.inflate(layoutInflater) }

    override fun getPositiveText() = "확인"

    override fun getWidthRatio(): Float = 0.9f

    override fun getPositiveTextColor(): Int = ContextCompat.getColor(requireActivity(), R.color.design_default_color_error)

    override fun getContentView(): View = _view.root

}
