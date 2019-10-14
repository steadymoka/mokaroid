package land.moka.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import land.moka.dialog.databinding.LayoutLoadingDialogBinding

class LoadingDialog : AppCompatDialogFragment() {

    private lateinit var binding: LayoutLoadingDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<LayoutLoadingDialogBinding>(inflater, R.layout.layout_loading_dialog, container, false)
            .apply {
                binding = this
                binding.lifecycleOwner = this@LoadingDialog

                dialog?.setCanceledOnTouchOutside(false)
                dialog?.setCancelable(false)
                dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            }.root
    }

    fun show(manager: FragmentManager) {
        show(manager, "")
    }

}
