package moka.land.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import moka.moka.dialog.R

class LoadingDialog(var dim: Boolean = true) : AppCompatDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_loading_dialog, container, false)
            .apply {
                dialog?.setCanceledOnTouchOutside(false)
                dialog?.setCancelable(false)
                dialog?.window?.setBackgroundDrawableResource(R.color.transparency)
                if (!dim) {
                    dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                }
            }
    }

    fun show(manager: FragmentManager) {
        show(manager, "")
    }

}
