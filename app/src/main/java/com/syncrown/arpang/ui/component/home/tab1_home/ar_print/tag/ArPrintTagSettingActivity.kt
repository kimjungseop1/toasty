package com.syncrown.arpang.ui.component.home.tab1_home.ar_print.tag

import android.os.Bundle
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityArPrintTagSettingBinding
import com.syncrown.arpang.ui.base.BaseActivity

class ArPrintTagSettingActivity : BaseActivity() {
    private lateinit var binding: ActivityArPrintTagSettingBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityArPrintTagSettingBinding.inflate(layoutInflater)
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