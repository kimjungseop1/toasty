package com.syncrown.toasty.ui.component.home.tab1_home.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.syncrown.toasty.ui.component.home.tab1_home.main.Case1Fragment
import com.syncrown.toasty.ui.component.home.tab1_home.main.Case2Fragment
import com.syncrown.toasty.ui.component.home.tab1_home.main.Case3Fragment

class CasePagerAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Case1Fragment()
            1 -> Case2Fragment()
            else -> Case3Fragment()
        }
    }
}