package com.syncrown.arpang.ui.component.home.tab5_more.account.withdrawal

import android.os.Bundle
import android.text.SpannableString
import android.text.style.LeadingMarginSpan
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityWithdrawalBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestUserProfileDto
import com.syncrown.arpang.network.model.RequestWithdrawalDto
import com.syncrown.arpang.network.model.ResponseUserProfileDto
import com.syncrown.arpang.ui.base.BaseActivity
import kotlinx.coroutines.launch

class WithdrawalActivity : BaseActivity() {
    private lateinit var binding: ActivityWithdrawalBinding
    private val withdrawalViewModel: WithdrawalViewModel by viewModels()

    override fun observeViewModel() {
        lifecycleScope.launch {
            withdrawalViewModel.getUserProfileResponseLiveData()
                .observe(this@WithdrawalActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            result.data.let { data ->
                                when (data?.msgCode) {
                                    "SUCCESS" -> {
                                        updateUI(data)
                                    }
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
                            Log.e("jung", "오류 : ${result.message}")
                        }
                    }
                }

            withdrawalViewModel.withdrawalResponseLiveData()
                .observe(this@WithdrawalActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            result.data.let { data ->
                                when (data?.msgCode) {
                                    "SUCCESS" -> {
                                        AppDataPref.clear(this@WithdrawalActivity)
                                        goLogin()
                                    }

                                    "FAIL" -> {}
                                    "NONE_ID" -> {}
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
                            Log.e("jung", "오류 : ${result.message}")
                        }
                    }
                }
        }
    }

    override fun initViewBinding() {
        binding = ActivityWithdrawalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getUserProfile()

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.more_account_withdrawal)

        val text = getString(R.string.account_withdrawal_polish)
        val spannableString = SpannableString(text)
        spannableString.setSpan(
            LeadingMarginSpan.Standard(0, 50),
            text.indexOf("1."),
            text.length,
            0
        )
        spannableString.setSpan(
            LeadingMarginSpan.Standard(0, 0),
            text.indexOf("2."),
            text.length,
            0
        )
        spannableString.setSpan(
            LeadingMarginSpan.Standard(0, 0),
            text.indexOf("3."),
            text.length,
            0
        )
        binding.contentTextView.text = spannableString

        binding.withdrawalCheckView.setOnCheckedChangeListener { _, isChecked ->
            binding.withdrawalBtn.isSelected = isChecked
        }

        binding.withdrawalBtn.isSelected = false
        binding.withdrawalBtn.setOnClickListener {
            val requestWithdrawalDto = RequestWithdrawalDto()
            requestWithdrawalDto.user_id = AppDataPref.userId

            withdrawalViewModel.setWithdrawal(requestWithdrawalDto)
        }
    }

    private fun getUserProfile() {
        val requestUserProfileDto = RequestUserProfileDto()
        requestUserProfileDto.user_id = AppDataPref.userId

        withdrawalViewModel.getUserProfile(requestUserProfileDto)
    }

    private fun updateUI(data: ResponseUserProfileDto) {
        //TODO 사용자 이름
        if (data.nick_nm == null || data.nick_nm.toString().isEmpty()) {
            binding.nameView.text = data.user_id
        } else {
            binding.nameView.text = data.nick_nm
        }

        //TODO 메일 주소
        if (data.email == null || data.email.toString().isEmpty()) {
            binding.idView.text = getString(R.string.from_empty_email_account)
        } else {
            binding.idView.text = data.email
        }
    }
}