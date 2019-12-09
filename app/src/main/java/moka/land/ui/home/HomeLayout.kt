package moka.land.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import moka.land.databinding.LayoutHomeBinding
import moka.land.imagehelper.picker.builder.ImagePicker

class HomeLayout : Fragment() {

    private val _view by lazy { LayoutHomeBinding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return _view.root
    }

    fun aa() {
        ImagePicker
            .with(this)
            .setConfig {
                camera = true
            }
            .showSingle { uri ->

            }
    }

}
