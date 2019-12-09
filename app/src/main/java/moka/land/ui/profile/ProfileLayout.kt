package moka.land.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch
import moka.land.base.goneFadeOut
import moka.land.base.visibleFadeIn
import moka.land.component.widget.EndlessRecyclerViewScrollListener
import moka.land.databinding.LayoutProfileBinding
import moka.land.ui.profile.adapter.PinnedAdapter
import moka.land.ui.profile.adapter.RepositoryAdapter
import moka.land.util.load
import org.koin.android.ext.android.inject

enum class Tab {
    Overview, Repositories;

    companion object {
        fun get(index: Int): Tab = values().filter { it.ordinal == index }[0]
    }
}

class ProfileLayout : Fragment() {

    private val _view by lazy { LayoutProfileBinding.inflate(layoutInflater) }

    private val viewModel by inject<ProfileViewModel>()
    private val overviewAdapter by lazy { PinnedAdapter() }
    private val repositoryAdapter by lazy { RepositoryAdapter() }

    private var loadMore: EndlessRecyclerViewScrollListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        bindEvent()
        bindViewModel()

        lifecycleScope.launch {
            viewModel.loadProfileData()
        }
        return _view.root
    }

    private fun init() {
        viewModel.selectedTab.value = Tab.Overview

        _view.recyclerViewOverview.adapter = overviewAdapter
        _view.recyclerViewRepositories.adapter = repositoryAdapter
    }

    private fun bindEvent() {
        _view.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (Tab.get(tab?.position ?: 0)) {
                    Tab.Overview -> {
                        viewModel.selectedTab.value = Tab.Overview
                    }
                    Tab.Repositories -> {
                        lifecycleScope.launch {
                            viewModel.selectedTab.value = Tab.Repositories

                            _view.recyclerViewRepositories.scrollToPosition(0)
                            loadMore?.resetState()
                            viewModel.reloadRepositories()
                        }
                    }
                }
            }
        })

        loadMore = object : EndlessRecyclerViewScrollListener(_view.recyclerViewRepositories.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                lifecycleScope.launch {
                    viewModel.loadRepositories()
                }
            }
        }
        _view.recyclerViewRepositories.addOnScrollListener(loadMore!!)
    }

    private fun bindViewModel() {
        viewModel.loading.observe(viewLifecycleOwner, Observer {

        })

        viewModel.selectedTab.observe(viewLifecycleOwner, Observer {
            when (it) {
                Tab.Overview -> {
                    _view.recyclerViewOverview.visibleFadeIn(150)
                    _view.recyclerViewRepositories.goneFadeOut(150)
                }
                Tab.Repositories -> {
                    _view.recyclerViewOverview.goneFadeOut(150)
                    _view.recyclerViewRepositories.visibleFadeIn(150)
                }
            }
        })

        viewModel.profile.observe(viewLifecycleOwner, Observer {
            _view.textViewName.text = it.name()
            _view.textViewBio.text = it.bio()
            _view.textViewStatus.text = "\"${it.status()?.message()}\""
            _view.imageViewProfileImage.load(activity!!, "${it.avatarUrl()}")
        })

        viewModel.pinnedRepository.observe(viewLifecycleOwner, Observer {
            overviewAdapter.setItems(it.map { PinnedAdapter.Data(it) })
        })

        viewModel.myRepository.observe(viewLifecycleOwner, Observer {
            repositoryAdapter.setItems(it.map { RepositoryAdapter.Data(it) })
        })

        viewModel.footerLoading.observe(viewLifecycleOwner, Observer {
            repositoryAdapter.showLoading = it
        })
    }

}
