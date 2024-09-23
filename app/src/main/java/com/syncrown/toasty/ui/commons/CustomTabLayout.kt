package com.syncrown.toasty.ui.commons

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.syncrown.toasty.R

class CustomTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TabLayout(context, attrs, defStyleAttr) {

    override fun addTab(tab: Tab, setSelected: Boolean) {
        super.addTab(tab, setSelected)
        customizeTabBackground(tab)
    }

    private fun customizeTabBackground(tab: Tab) {
        // 모서리를 둥글게 만들기 위한 GradientDrawable 설정
        val radius = CommonFunc.dpToPx(20f,context)
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.setColor(ContextCompat.getColor(context, R.color.color_FBCF45))
        drawable.cornerRadius = radius

        // 선택되지 않은 상태의 배경
        val unselectedDrawable = GradientDrawable()
        unselectedDrawable.shape = GradientDrawable.RECTANGLE
        unselectedDrawable.setColor(ContextCompat.getColor(context, R.color.color_F0F0F0))
        unselectedDrawable.cornerRadius = radius

        // 선택된 탭의 배경과 그렇지 않은 탭의 배경을 설정
        tab.view.background = if (tab.isSelected) drawable else unselectedDrawable

        // 탭 선택 리스너를 설정하여 선택 상태에 따른 배경 변경
        this.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(selectedTab: Tab) {
                selectedTab.view.background = drawable
            }

            override fun onTabUnselected(unselectedTab: Tab) {
                unselectedTab.view.background = unselectedDrawable
            }

            override fun onTabReselected(reselectedTab: Tab) {}
        })
    }
}