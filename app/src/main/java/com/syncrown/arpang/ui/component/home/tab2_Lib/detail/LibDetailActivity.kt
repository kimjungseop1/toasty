package com.syncrown.arpang.ui.component.home.tab2_Lib.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityLibDetailBinding
import com.syncrown.arpang.databinding.PopupMenuDetailBinding
import com.syncrown.arpang.ui.base.BaseActivity

class LibDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityLibDetailBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityLibDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.home_nav_title_2)

        binding.actionbar.actionPrint.setOnClickListener {

        }

        binding.actionbar.actionContext.setOnClickListener {
            showPopupWindow(binding.actionbar.actionContext)
        }
    }

    private fun showPopupWindow(anchor: View) {
        val popBinding = PopupMenuDetailBinding.inflate(LayoutInflater.from(this))

        val popupWindow = PopupWindow(popBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true)

        popBinding.switchMenu1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 스위치가 켜졌을 때
            } else {
                // 스위치가 꺼졌을 때
            }
        }

        popBinding.menuItem2.setOnClickListener {
            popupWindow.dismiss()
        }

        popBinding.menuItem3.setOnClickListener {
            popupWindow.dismiss()
        }

        popBinding.menuItem4.setOnClickListener {
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(anchor, 0, 0)
    }
}