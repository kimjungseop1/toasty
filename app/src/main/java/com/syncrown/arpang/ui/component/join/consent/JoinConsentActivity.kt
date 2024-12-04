package com.syncrown.arpang.ui.component.join.consent

import android.content.Intent
import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.databinding.ActivityConsentBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestJoinDto
import com.syncrown.arpang.network.model.RequestLoginDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.join.term_privacy.PolishWebActivity
import com.syncrown.arpang.ui.component.join.welcome.WelcomeActivity

class JoinConsentActivity : BaseActivity() {
    private lateinit var binding: ActivityConsentBinding
    private val joinConsentViewModel: JoinConsentViewModel by viewModels()

    private var isCanceled = false

    override fun observeViewModel() {
        //TODO 가입신청 옵져브
        joinConsentViewModel.joinMemberResponseLiveData().observe(this) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    result.data.let { data ->
                        when (data?.msgCode) {
                            "SUCCESS" -> {
                                Log.e(TAG, "성공 id : " + AppDataPref.userId)
                                AppDataPref.save(this)
                                isCanceled = false

                                val requestLoginDto = RequestLoginDto()
                                requestLoginDto.user_id = AppDataPref.userId
                                joinConsentViewModel.login(requestLoginDto)
                            }

                            "DUPPLE" -> {}

                            else -> {}
                        }
                    }
                }

                is NetworkResult.NetCode -> {
                    Log.e("jung","실패 : ${result.message}")
                }

                is NetworkResult.Error -> {
                    Log.e(TAG, "오류 : ${result.message}")
                }
            }
        }

        joinConsentViewModel.loginResponseLiveData().observe(this) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    result.data.let { data ->
                        when (data?.msgCode) {
                            "SUCCESS" -> {
                                AppDataPref.save(this)
                                goWelcome()
                            }

                            "FAIL" -> { Log.e("jung","Consent FAIL") }
                            "NONE_ID" -> { Log.e("jung","Consent NONE_ID") }
                            else -> {}
                        }
                    }
                }

                is NetworkResult.NetCode -> {
                    Log.e("jung","실패 : ${result.message}")
                }

                is NetworkResult.Error -> {
                    Log.e(TAG, "오류 : ${result.message}")
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
            isCanceled = true
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
            requestJoinDto.email = AppDataPref.userEmail
            requestJoinDto.nick_nm = AppDataPref.userId
            requestJoinDto.login_connt_accnt = AppDataPref.login_connect_site
            requestJoinDto.userid_se = 0
            requestJoinDto.use_se = 1
            requestJoinDto.markt_recptn_agre = 1
            requestJoinDto.app_id = "APP_ARPANG"

            joinConsentViewModel.joinMember(requestJoinDto)
        }

    }

    override fun onStop() {
        super.onStop()
        if (isFinishing || isCanceled) {
            AppDataPref.clear(this)
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
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun goWebPolish(extra: String) {
        val intent = Intent(this, PolishWebActivity::class.java)
        intent.putExtra("CONSENT_EXTRA", extra)
        startActivity(intent)
    }
}