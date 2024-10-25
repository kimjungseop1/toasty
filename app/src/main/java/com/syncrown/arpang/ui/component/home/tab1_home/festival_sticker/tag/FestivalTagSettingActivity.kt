package com.syncrown.arpang.ui.component.home.tab1_home.festival_sticker.tag

import android.os.Bundle
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityFestivalTagSettingBinding
import com.syncrown.arpang.ui.base.BaseActivity

class FestivalTagSettingActivity:BaseActivity() {
    private lateinit var binding: ActivityFestivalTagSettingBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityFestivalTagSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }
        binding.actionbar.actionTitle.text = getString(R.string.actionbar_print_preview)
        binding.actionbar.actionEtc.text = getString(R.string.edit_video_print_print)
        binding.actionbar.actionEtc.setOnClickListener {

        }
    }
}