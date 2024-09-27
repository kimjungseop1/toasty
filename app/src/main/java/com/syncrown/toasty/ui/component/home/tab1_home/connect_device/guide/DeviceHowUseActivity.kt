package com.syncrown.toasty.ui.component.home.tab1_home.connect_device.guide

import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.syncrown.toasty.databinding.ActivityHowUseBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.commons.DialogProgressCommon
import com.syncrown.toasty.ui.component.home.tab1_home.connect_device.adapter.UsePagerAdapter

class DeviceHowUseActivity : BaseActivity() {
    private lateinit var binding: ActivityHowUseBinding

    private val dialogLoading = DialogProgressCommon()

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityHowUseBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = "사용방법"

        setUsePager()


    }

    private fun setUsePager() {
        binding.viewPagerView.adapter = UsePagerAdapter(this)
        binding.viewPagerView.isUserInputEnabled = false
        TabLayoutMediator(binding.customTabView, binding.viewPagerView) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "본체"
                }

                1 -> {
                    tab.text = "용지"
                }
            }
        }.attach()
    }

    private fun showLoadingDialog() {
        dialogLoading.showLoading(supportFragmentManager)
    }

    private fun hideLoadingDialog() {
        dialogLoading.dismiss()
    }
}