package moka.land.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moka.land.R
import moka.land.databinding.LayoutHomeBinding
import moka.land.dialog.LoadingDialog
import moka.land.ui.dialogs.TestDialog

class HomeLayout : Fragment() {

    private val _view by lazy { LayoutHomeBinding.inflate(layoutInflater) }
    private val nav by lazy { findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bindEvent()
        return _view.root
    }

    private fun bindEvent() {
        _view.textViewImagePicker.setOnClickListener {
            if (nav.currentDestination?.label == "start") {
                nav.navigate(R.id.goImagePickerSample)
            }
        }

        _view.textViewImageViewer.setOnClickListener {
            if (nav.currentDestination?.label == "start") {
                nav.navigate(R.id.goImageViewerSample)
            }
        }

        _view.textViewDialogs.setOnClickListener {
            lifecycleScope.launch {
                TestDialog()
                    .show(activity!!.supportFragmentManager)

//                val dialog = LoadingDialog(false)
//                dialog.show(activity!!.supportFragmentManager)
//                delay(2000)
//                dialog.dismiss()
            }
        }

        _view.textViewAdHelper.setOnClickListener {
            if (nav.currentDestination?.label == "start") {
                nav.navigate(R.id.goAdHelperSample)
            }
        }
    }

}
