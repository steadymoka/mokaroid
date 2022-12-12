package moka.land.ui.main

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import com.facebook.stetho.dumpapp.GlobalOptions
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import moka.land.R
import moka.land.base.log
import moka.land.databinding.NavigationMainBinding
import moka.land.ui.home.HomeLayout
import moka.land.ui.profile.ProfileLayout
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.Exception
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

class MainNavGraph : AppCompatActivity() {

    private val _view: NavigationMainBinding by lazy { NavigationMainBinding.inflate(layoutInflater) }

//    private val host by lazy { supportFragmentManager.findFragmentById(R.id.host) as NavHostFragment }
//    private val navController by lazy { host.navController }

    private val viewModel by lazy { getViewModel() as MainViewModel }
    private val adapter by lazy { MainPagerAdapter(this) }

    private val homeLayout by lazy { HomeLayout() }
    private val profileLayout by lazy { ProfileLayout() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_view.root)

        initView()
    }

    private fun initView() {
        adapter.addItems(homeLayout, profileLayout)

        _view.viewPager.run {
            adapter = this@MainNavGraph.adapter
            offscreenPageLimit = 1
            isUserInputEnabled = false
        }

        _view.bottomNavigation.setOnNavigationItemSelectedListener {
            if (_view.viewPager.currentItem == adapter.getItemInt(it.itemId)) {
                when (_view.viewPager.currentItem) {
                    0 -> {

                    }
                    1 -> {

                    }
                    2 -> {

                    }
                }
            }
            _view.viewPager.setCurrentItem(adapter.getItemInt(it.itemId), false)
            true
        }
    }

}
