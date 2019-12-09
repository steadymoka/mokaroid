package moka.land.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import moka.land.databinding.LayoutProfileBinding
import moka.land.util.load
import org.koin.android.ext.android.inject

class ProfileLayout : Fragment() {

    private val _view by lazy { LayoutProfileBinding.inflate(layoutInflater) }
    private val viewModel by inject<ProfileViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bindViewModel()

        lifecycleScope.launch {
            viewModel.loadProfileData()
        }
        return _view.root
    }

    private fun bindViewModel() {
        viewModel.loading.observe(viewLifecycleOwner, Observer {

        })

        viewModel.profile.observe(viewLifecycleOwner, Observer {
            _view.textViewName.text = it.name()
            _view.textViewBio.text = it.bio()
            _view.textViewStatus.text = "\"${it.status()?.message()}\""
            _view.imageViewProfileImage.load(activity!!, "${it.avatarUrl()}")
        })

        viewModel.pinnedRepository.observe(viewLifecycleOwner, Observer {

        })
    }

}
