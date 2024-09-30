package com.syncrown.arpang.ui.component.home.tab5_more.alert_setting

import android.os.Bundle
import com.syncrown.arpang.databinding.ActivityAlertSettingBinding
import com.syncrown.arpang.ui.base.BaseActivity

class AlertSettingActivity : BaseActivity() {
    private lateinit var binding: ActivityAlertSettingBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityAlertSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = "알림 설정"


    }
}