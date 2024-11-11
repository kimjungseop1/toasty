package com.syncrown.arpang.ui.component.home.search.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.syncrown.arpang.ui.component.home.search.SearchShareTabFragment
import com.syncrown.arpang.ui.component.home.search.SearchStorageTabFragment
import com.syncrown.arpang.ui.component.home.search.SearchUserTabFragment
import com.syncrown.arpang.ui.component.home.tab1_home.connect_device.GuideUse1Fragment
import com.syncrown.arpang.ui.component.home.tab1_home.connect_device.GuideUse2Fragment

class SearchResultPagerAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SearchUserTabFragment()
            1 -> SearchShareTabFragment()
            else -> SearchStorageTabFragment()
        }
    }
}