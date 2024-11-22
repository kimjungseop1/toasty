package com.syncrown.arpang.ui.component.home.tab5_more.account.withdrawal

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.LeadingMarginSpan
import android.util.Log
import android.view.View
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

        //탈퇴사유
        binding.check1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.clView3.visibility = View.GONE

                binding.check2.isChecked = false
                binding.check3.isChecked = false
                binding.check4.isChecked = false
            }
            binding.withdrawalBtn.isSelected = isValid()
        }

        binding.check2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.clView3.visibility = View.GONE

                binding.check1.isChecked = false
                binding.check3.isChecked = false
                binding.check4.isChecked = false
            }
            binding.withdrawalBtn.isSelected = isValid()
        }

        binding.check3.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.clView3.visibility = View.GONE

                binding.check1.isChecked = false
                binding.check2.isChecked = false
                binding.check4.isChecked = false
            }
            binding.withdrawalBtn.isSelected = isValid()
        }

        binding.check4.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.clView3.visibility = View.VISIBLE

                binding.check1.isChecked = false
                binding.check2.isChecked = false
                binding.check3.isChecked = false
            }
            binding.withdrawalBtn.isSelected = isValid()
        }

        binding.inputEtc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let {
                    // 한글과 영문 길이 계산
                    var length = 0
                    it.forEach { char ->
                        length += if (char.toInt() > 0x007F) 2 else 1 // 한글은 2, 영문은 1로 계산
                    }

                    // 글자 수 카운트 UI 업데이트
                    binding.etcLengthCnt.text = "$length/100"
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    var length = 0
                    val validText = StringBuilder()

                    // 입력된 텍스트를 한글/영문 길이에 따라 계산
                    for (char in it) {
                        val charLength = if (char.toInt() > 0x007F) 2 else 1 // 한글은 2, 영문은 1로 계산
                        if (length + charLength >= 100) break // 100을 넘으면 중단
                        validText.append(char)
                        length += charLength
                    }

                    // 초과된 경우 텍스트 수정
                    if (validText.toString() != it.toString()) {
                        binding.inputEtc.removeTextChangedListener(this) // 리스너 제거
                        binding.inputEtc.setText(validText.toString()) // 제한된 텍스트 설정
                        binding.inputEtc.setSelection(validText.length) // 커서를 끝에 위치
                        binding.inputEtc.addTextChangedListener(this) // 리스너 다시 추가
                    }
                }
            }
        })

        //기타사유 -> 입력
        binding.withdrawalCheckView.setOnCheckedChangeListener { _, _ ->
            binding.withdrawalBtn.isSelected = isValid()
        }

        binding.withdrawalBtn.isSelected = false
        binding.withdrawalBtn.setOnClickListener {
            val requestWithdrawalDto = RequestWithdrawalDto()
            requestWithdrawalDto.user_id = AppDataPref.userId
            requestWithdrawalDto.etc_reson = reasonType()
            if (binding.check4.isChecked) {
                requestWithdrawalDto.break_reson = binding.inputEtc.text.toString()
            }

            withdrawalViewModel.setWithdrawal(requestWithdrawalDto)
        }
    }

    private fun reasonType() :String {
        var type = ""
        if (binding.check1.isChecked) {
            type = "B1"
        } else if (binding.check2.isChecked) {
            type = "B2"
        } else if (binding.check3.isChecked) {
            type = "B3"
        } else if (binding.check4.isChecked) {
            type = "B4"
        }

        return type
    }

    private fun isValid(): Boolean {
        val checkCount = listOf(
            binding.check1.isChecked,
            binding.check2.isChecked,
            binding.check3.isChecked,
            binding.check4.isChecked
        ).count { it }

        if (checkCount != 1) {
            return false
        }

        if (!binding.withdrawalCheckView.isChecked) {
            return false
        }

        if (binding.check4.isChecked && binding.inputEtc.text.toString().isEmpty()) {
            return false
        }

        return true
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