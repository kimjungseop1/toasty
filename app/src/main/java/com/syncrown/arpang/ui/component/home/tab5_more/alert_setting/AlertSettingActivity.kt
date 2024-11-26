package com.syncrown.arpang.ui.component.home.tab5_more.alert_setting

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityAlertSettingBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestUserAlertListDto
import com.syncrown.arpang.network.model.RequestUserAlertSettingDto
import com.syncrown.arpang.ui.base.BaseActivity
import kotlinx.coroutines.launch

class AlertSettingActivity : BaseActivity() {
    private lateinit var binding: ActivityAlertSettingBinding
    private val alertSettingViewModel: AlertSettingViewModel by viewModels()

    override fun observeViewModel() {
        lifecycleScope.launch {
            alertSettingViewModel.userAlertListResponseLiveData()
                .observe(this@AlertSettingActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.root

                            if (data != null) {
                                if (data.noti_event_se == 0 && data.subscrip_se == 0 && data.favor_se == 0 && data.comment_se == 0) {
                                    binding.allAlertSwitch.isChecked = false

                                    binding.eventSwitch.isChecked = false
                                    binding.subscribeSwitch.isChecked = false
                                    binding.likeSwitch.isChecked = false
                                    binding.commentSwitch.isChecked = false

                                } else {
                                    binding.allAlertSwitch.isChecked = true

                                    binding.eventSwitch.isChecked = data.noti_event_se == 1
                                    binding.subscribeSwitch.isChecked = data.subscrip_se == 1
                                    binding.likeSwitch.isChecked = data.favor_se == 1
                                    binding.commentSwitch.isChecked = data.comment_se == 1
                                }
                            }
                        }

                        is NetworkResult.NetCode -> {
                            Log.e("jung", "실패 : ${result.message}")
                            if (result.message.equals("403")) {
                                goLogin()
                            }
                        }

                        is NetworkResult.Error -> {
                            Log.e(TAG, "오류 : ${result.message}")
                        }
                    }
                }
        }

        lifecycleScope.launch {
            alertSettingViewModel.userAlertSettingResponseLiveData()
                .observe(this@AlertSettingActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            result.data.let { data ->
                                when (data?.msgCode) {
                                    "SUCCESS" -> {
                                        Log.e("jung", "성공")
                                    }

                                    "FAIL" -> {
                                        Log.e("jung", "실패")
                                    }

                                    else -> {}
                                }
                            }
                        }

                        is NetworkResult.NetCode -> {
                            Log.e("jung", "실패 : ${result.message}")
                            if (result.message.equals("403")) {
                                goLogin()
                            }
                        }

                        is NetworkResult.Error -> {
                            Log.e(TAG, "오류 : ${result.message}")
                        }
                    }
                }
        }
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

        val requestUserAlertListDto = RequestUserAlertListDto()
        requestUserAlertListDto.user_id = AppDataPref.userId
        alertSettingViewModel.userAlertList(requestUserAlertListDto)

    }

    private fun setAlarmSetting() {
        binding.allAlertSwitch.setOnCheckedChangeListener { _, isChecked ->
            enableState(isChecked)
        }

        binding.eventSwitch.setOnCheckedChangeListener { _, _ ->
            sendAlertSetting()
        }

        binding.subscribeSwitch.setOnCheckedChangeListener { _, _ ->
            sendAlertSetting()
        }

        binding.likeSwitch.setOnCheckedChangeListener { _, _ ->
            sendAlertSetting()
        }

        binding.commentSwitch.setOnCheckedChangeListener { _, _ ->
            sendAlertSetting()
        }
    }

    private fun sendAlertSetting() {
        val requestUserAlertSettingDto = RequestUserAlertSettingDto()
        requestUserAlertSettingDto.user_id = AppDataPref.userId
        if (binding.eventSwitch.isChecked) {
            requestUserAlertSettingDto.noti_event_se = 1
        } else {
            requestUserAlertSettingDto.noti_event_se = 0
        }

        if (binding.subscribeSwitch.isChecked) {
            requestUserAlertSettingDto.subscrip_se = 1
        } else {
            requestUserAlertSettingDto.subscrip_se = 0
        }

        if (binding.likeSwitch.isChecked) {
            requestUserAlertSettingDto.favor_se = 1
        } else {
            requestUserAlertSettingDto.favor_se = 0
        }

        if (binding.commentSwitch.isEnabled) {
            requestUserAlertSettingDto.comment_se = 1
        } else {
            requestUserAlertSettingDto.comment_se = 0
        }

        alertSettingViewModel.userAlertSetting(requestUserAlertSettingDto)
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