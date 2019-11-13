package io.haruharu.imagehelper.picker

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import io.haruharu.imagehelper.R

class ImagePicker : AppCompatDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
        super.onCreate(savedInstanceState)
    }

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return if (viewModel.flagOfDirectAlbum) {
//            Handler().postDelayed({ pickImageFromAlbum() }, 600)
//            inflater.inflate(R.layout.dialog_empty, null)
//        }
//        else {
//            inflater.inflate(R.layout.dialog_image_picker, null)
//        }
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        if (!viewModel.flagOfDirectAlbum) {
//            initViews()
//            bindViews()
//        }
//    }

    @SuppressLint("MissingSuperCall")
    override fun onResume() {
        super.onResume()
        dialog?.window!!.setLayout((280 * context!!.resources.displayMetrics.densityDpi / 160.0).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    // -

    private fun initViews() {

    }

    private fun bindViews() {

    }

}
