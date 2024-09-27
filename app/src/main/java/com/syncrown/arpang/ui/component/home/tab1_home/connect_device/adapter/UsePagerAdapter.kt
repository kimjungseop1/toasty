package com.syncrown.arpang.ui.component.home.tab1_home.connect_device.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.syncrown.arpang.ui.component.home.tab1_home.connect_device.guide.GuideUse1Fragment
import com.syncrown.arpang.ui.component.home.tab1_home.connect_device.guide.GuideUse2Fragment

class UsePagerAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GuideUse1Fragment()
            else -> GuideUse2Fragment()
        }
    }
}