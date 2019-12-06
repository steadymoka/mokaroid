package moka.land.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import moka.land.R
import moka.land.ui.home.HomeLayout
import moka.land.ui.profile.ProfileLayout

class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var items: ArrayList<Fragment> = ArrayList()

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Fragment {
        return items[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun addItems(vararg fragments: Fragment) {
        fragments.forEach {
            if (!items.contains(it)) {
                items.add(it)
            }
        }
        notifyDataSetChanged()
    }

    fun getItemInt(menuId: Int): Int {
        val className =
            when (menuId) {
                R.id.homeLayout -> HomeLayout::class.java.simpleName
                R.id.profileLayout -> ProfileLayout::class.java.simpleName
                else -> ""
            }

        return items.indexOf(items.filter { it.javaClass.simpleName == className }[0])
    }

}
