package moka.land.ui.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import moka.land.base.log
import moka.land.databinding.LayoutImagePickerSampleBinding
import moka.land.databinding.LayoutRepositoryBinding
import moka.land.imagehelper.picker.builder.ImagePicker
import moka.land.util.load
import org.koin.android.ext.android.inject

class RepositoryLayout : Fragment() {

    private val _view by lazy { LayoutRepositoryBinding.inflate(layoutInflater) }
    private val viewModel by inject<RepositoryViewModel>()
    private val args: RepositoryLayoutArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bindEvent()
        bindViewModel()

        lifecycleScope.launch {
            viewModel.loadRepository(args.name)
        }
        return _view.root
    }

    private fun bindEvent() {
        _view.imageViewBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun bindViewModel() {
        viewModel.repository.observe(viewLifecycleOwner, Observer { repo ->
            if (null != repo) {
                _view.textViewName.text = "\uD83D\uDCD3 ${repo.name()}"
                _view.textViewDescription.text = repo.description()
            }
            else {
                _view.textViewName.text = "삭제된 저장소입니다."
                _view.textViewDescription.text = "-"
            }
        })
    }

}
