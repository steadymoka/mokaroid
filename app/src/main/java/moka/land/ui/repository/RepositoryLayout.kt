package moka.land.ui.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import moka.land.databinding.LayoutImagePickerSampleBinding
import moka.land.databinding.LayoutRepositoryBinding
import moka.land.imagehelper.picker.builder.ImagePicker
import moka.land.util.load

class RepositoryLayout : Fragment() {

    private val _view by lazy { LayoutRepositoryBinding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bindEvent()
        return _view.root
    }

    private fun bindEvent() {
        _view.imageViewBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}
