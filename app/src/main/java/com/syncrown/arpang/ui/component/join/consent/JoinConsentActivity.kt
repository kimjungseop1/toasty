package com.syncrown.arpang.ui.component.join.consent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.databinding.ActivityConsentBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestJoinDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.join.term_privacy.PolishWebActivity
import com.syncrown.arpang.ui.component.join.welcome.WelcomeActivity

class JoinConsentActivity : BaseActivity() {
    private lateinit var binding: ActivityConsentBinding
    private val joinConsentViewModel: JoinConsentViewModel by viewModels()

    override fun observeViewModel() {
        //TODO 가입신청 옵져브
        joinConsentViewModel.joinMemberResponseLiveData().observe(this) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.let { data ->
                        when (data.msgCode) {
                            "DUPPLE" -> {
                                Log.e(TAG, "아이디 중복")
                            }

                            "SUCCESS" -> {
                                Log.e(TAG, "성공")
                                goWelcome()
                            }

                            else -> {
                                Log.e(TAG, "알수없는 메시지 코드 : ${data.msgCode} ")
                            }
                        }
                    }
                }

                is NetworkResult.Error -> {
                    Log.e(TAG, "네트워크 오류")
                }
            }
        }

    }

    override fun initViewBinding() {
        binding = ActivityConsentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.allConsentBtn.setOnClickListener {
            binding.consentBtn1.isChecked = binding.allConsentBtn.isChecked
            binding.consentBtn2.isChecked = binding.allConsentBtn.isChecked
            binding.consentBtn3.isChecked = binding.allConsentBtn.isChecked
            binding.joinBtn.isSelected = isValid()
        }

        binding.consentBtn1.setOnCheckedChangeListener { _, _ ->
            binding.joinBtn.isSelected = isValid()
        }

        binding.consentBtn2.setOnCheckedChangeListener { _, _ ->
            binding.joinBtn.isSelected = isValid()
        }

        binding.consentDetail1.setOnClickListener {

            goWebPolish("term")
        }

        binding.consentDetail2.setOnClickListener {
            goWebPolish("privacy")
        }

        binding.joinBtn.isSelected = false
        binding.joinBtn.setOnClickListener {
            val requestJoinDto = RequestJoinDto()
            requestJoinDto.user_id = AppDataPref.userId

            joinConsentViewModel.joinMember(requestJoinDto)
        }

    }

    private fun isValid(): Boolean {
        if (!binding.consentBtn1.isChecked) {
            return false
        }

        if (!binding.consentBtn2.isChecked) {
            return false
        }

        return true
    }

    private fun goWelcome() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }

    private fun goWebPolish(extra: String) {
        val intent = Intent(this, PolishWebActivity::class.java)
        intent.putExtra("CONSENT_EXTRA", extra)
        startActivity(intent)
    }
}