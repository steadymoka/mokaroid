package land.moka.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager

class LoadingDialog : AppCompatDialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_loading_dialog, container, false)
            .apply {
                dialog?.setCanceledOnTouchOutside(false)
                dialog?.setCancelable(false)
                dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            }
    }

    fun show(manager: FragmentManager) {
        show(manager, "")
    }

}
