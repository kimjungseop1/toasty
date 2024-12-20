package com.syncrown.arpang.ui.component.join.consent

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.databinding.ActivityConsentBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestCheckNickNameDto
import com.syncrown.arpang.network.model.RequestJoinDto
import com.syncrown.arpang.network.model.RequestLoginDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.join.term_privacy.PolishWebActivity
import com.syncrown.arpang.ui.component.join.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class JoinConsentActivity : BaseActivity() {
    private lateinit var binding: ActivityConsentBinding
    private val joinConsentViewModel: JoinConsentViewModel by viewModels()

    private var isCanceled = false

    private var isNickNameCheck = false

    override fun observeViewModel() {
        lifecycleScope.launch {
            //TODO 가입신청 옵져브
            joinConsentViewModel.joinMemberResponseLiveData()
                .observe(this@JoinConsentActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            result.data.let { data ->
                                when (data?.msgCode) {
                                    "SUCCESS" -> {
                                        AppDataPref.save(this@JoinConsentActivity)
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
                            Log.e("jung", "실패 : ${result.message}")
                        }

                        is NetworkResult.Error -> {
                            Log.e(TAG, "오류 : ${result.message}")
                        }
                    }
                }
        }

        lifecycleScope.launch {
            joinConsentViewModel.loginResponseLiveData()
                .observe(this@JoinConsentActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            result.data.let { data ->
                                when (data?.msgCode) {
                                    "SUCCESS" -> {
                                        AppDataPref.save(this@JoinConsentActivity)
                                        goWelcome()
                                    }

                                    "FAIL" -> {
                                        Log.e("jung", "Consent FAIL")
                                    }

                                    "NONE_ID" -> {
                                        Log.e("jung", "Consent NONE_ID")
                                    }

                                    else -> {}
                                }
                            }
                        }

                        is NetworkResult.NetCode -> {
                            Log.e("jung", "실패 : ${result.message}")
                        }

                        is NetworkResult.Error -> {
                            Log.e(TAG, "오류 : ${result.message}")
                        }
                    }
                }
        }

        lifecycleScope.launch {
            joinConsentViewModel.checkNickNameResponseLiveData()
                .observe(this@JoinConsentActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.msgCode
                            if (data.equals("SUCCESS")) {
                                //성공
                                binding.dupleToast.visibility = View.GONE

                                isNickNameCheck = true
                            } else {
                                //중복
                                showTemporaryView()

                                isNickNameCheck = false
                            }
                        }

                        is NetworkResult.NetCode -> {
                            Log.e("jung", "실패 : ${result.message}")
                        }

                        is NetworkResult.Error -> {
                            Log.e(TAG, "오류 : ${result.message}")
                        }
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

        binding.inputNick.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.isNotEmpty() == true) {
                    inputNickName()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.allConsentBtn.setOnClickListener {
            binding.consentBtn1.isChecked = binding.allConsentBtn.isChecked
            binding.consentBtn2.isChecked = binding.allConsentBtn.isChecked
            binding.consentBtn3.isChecked = binding.allConsentBtn.isChecked
            binding.joinBtn.isSelected = isValid()
        }

        binding.consentBtn1.setOnCheckedChangeListener { _, isChecked ->
            binding.joinBtn.isSelected = isValid()
            if (isChecked) {
                binding.consentBtn1.typeface = Typeface.DEFAULT_BOLD
            } else {
                binding.consentBtn1.typeface = Typeface.DEFAULT
            }
        }

        binding.consentBtn2.setOnCheckedChangeListener { _, isChecked ->
            binding.joinBtn.isSelected = isValid()
            if (isChecked) {
                binding.consentBtn2.typeface = Typeface.DEFAULT_BOLD
            } else {
                binding.consentBtn2.typeface = Typeface.DEFAULT
            }
        }

        binding.consentBtn3.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.consentBtn3.typeface = Typeface.DEFAULT_BOLD
            } else {
                binding.consentBtn3.typeface = Typeface.DEFAULT
            }
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
            requestJoinDto.nick_nm = ""
            requestJoinDto.login_connt_accnt = AppDataPref.login_connect_site
            requestJoinDto.userid_se = 0
            requestJoinDto.use_se = 1
            requestJoinDto.markt_recptn_agre = 1
            requestJoinDto.app_id = "APP_ARPANG"

            joinConsentViewModel.joinMember(requestJoinDto)
        }

    }

    private fun inputNickName() {
        val requestCheckNickNameDto = RequestCheckNickNameDto().apply {
            user_id = AppDataPref.userId
            app_id = "APP_ARPANG"
            nick_nm = binding.inputNick.text.toString()
        }

        joinConsentViewModel.checkNickName(requestCheckNickNameDto)
    }

    private fun showTemporaryView() {
        binding.dupleToast.visibility = View.VISIBLE
        binding.dupleToast.postDelayed({
            binding.dupleToast.visibility = View.GONE
        }, 3000)
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

        if (!isNickNameCheck) {
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