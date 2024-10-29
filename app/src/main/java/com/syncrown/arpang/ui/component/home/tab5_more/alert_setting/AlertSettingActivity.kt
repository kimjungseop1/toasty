package com.syncrown.arpang.ui.component.home.tab5_more.alert_setting

import android.os.Bundle
import com.google.firebase.messaging.FirebaseMessaging
import com.syncrown.arpang.R
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

        binding.actionbar.actionTitle.text = getString(R.string.alert_setting_title)

        enableState(false)
        setAlarmSetting()

    }

    private fun setAlarmSetting() {
        binding.allAlertSwitch.setOnCheckedChangeListener { _, isChecked ->
            enableState(isChecked)

            if (isChecked) {
                FirebaseMessaging.getInstance().subscribeToTopic("all")
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("all")
            }
        }

        binding.eventSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                FirebaseMessaging.getInstance().subscribeToTopic("event")
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("event")
            }
        }

        binding.subscribeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                FirebaseMessaging.getInstance().subscribeToTopic("subscribe")
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("subscribe")
            }
        }

        binding.likeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                FirebaseMessaging.getInstance().subscribeToTopic("like")
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("like")
            }
        }

        binding.commentSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                FirebaseMessaging.getInstance().subscribeToTopic("comment")
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("comment")
            }
        }
    }

    private fun enableState(isChecked: Boolean) {
        binding.eventSwitch.isEnabled = isChecked
        binding.subscribeSwitch.isEnabled = isChecked
        binding.likeSwitch.isEnabled = isChecked
        binding.commentSwitch.isEnabled = isChecked

        binding.eventView.isEnabled = isChecked
        binding.subscribeView.isEnabled = isChecked
        binding.likeView.isEnabled = isChecked
        binding.commentView.isEnabled = isChecked
    }
}