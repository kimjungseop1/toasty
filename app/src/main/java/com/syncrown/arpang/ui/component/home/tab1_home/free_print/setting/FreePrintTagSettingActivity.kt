package com.syncrown.arpang.ui.component.home.tab1_home.free_print.setting

import android.os.Bundle
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityFreePrintTagSettingBinding
import com.syncrown.arpang.ui.base.BaseActivity

class FreePrintTagSettingActivity : BaseActivity() {
    private lateinit var binding: ActivityFreePrintTagSettingBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityFreePrintTagSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }
        binding.actionbar.actionTitle.text = getString(R.string.tag_popup_input)
        binding.actionbar.actionEtc.text = getString(R.string.tag_popup_submit)
        binding.actionbar.actionEtc.setOnClickListener {

        }
    }
}