package moka.land.ui.samples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moka.land.databinding.LayoutDialogsSampleBinding
import moka.land.dialog.LoadingDialog
import moka.land.ui.dialogs.ButtonTestDialog
import moka.land.ui.dialogs.TestDialog

class DialogsSampleLayout : Fragment() {

    private val _view by lazy { LayoutDialogsSampleBinding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView()
        bindEvent()
        return _view.root
    }

    private fun initView() {
    }

    private fun bindEvent() {
        _view.imageViewBack.setOnClickListener {
            findNavController().navigateUp()
        }

        _view.textViewTestButton.setOnClickListener {
            TestDialog().show(activity!!.supportFragmentManager)
        }

        _view.textViewTestButton2.setOnClickListener {
            lifecycleScope.launch {
                val dialog = LoadingDialog(false)
                dialog.show(activity!!.supportFragmentManager)
                delay(2000)
                dialog.dismiss()
            }
        }

        _view.textViewTestButton3.setOnClickListener {
            ButtonTestDialog().show(requireActivity().supportFragmentManager)
        }
    }

}
