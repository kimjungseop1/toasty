package com.syncrown.arpang.ui.component.home.tab5_more.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityAccountManageBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestUserProfileDto
import com.syncrown.arpang.network.model.ResponseUserProfileDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.home.tab5_more.account.name.ChangeNameActivity
import com.syncrown.arpang.ui.component.home.tab5_more.account.withdrawal.WithdrawalActivity

class AccountManageActivity : BaseActivity() {
    private lateinit var binding: ActivityAccountManageBinding
    private val accountManageViewModel: AccountManageViewModel by viewModels()

    override fun observeViewModel() {
        accountManageViewModel.getUserProfileResponseLiveData().observe(this) { result ->
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
    }

    override fun initViewBinding() {
        binding = ActivityAccountManageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getUserProfile()

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.account_title)

        binding.clView2.setOnClickListener {
            goChangeName()
        }

        binding.withdrawalTxt.setOnClickListener {
            goWithdrawal()
        }

    }

    private fun getUserProfile() {
        val requestUserProfileDto = RequestUserProfileDto()
        requestUserProfileDto.user_id = AppDataPref.userId

        accountManageViewModel.getUserProfile(requestUserProfileDto)
    }

    private fun updateUI(data: ResponseUserProfileDto) {
        //TODO 사용자 이름
        if (data.nick_nm == null || data.nick_nm.toString().isEmpty()) {
            binding.nameTxt.text = getString(R.string.more_nick_name_empty_default)
        } else {
            binding.nameTxt.text = data.nick_nm
        }

        //TODO 메일 주소
        if (data.email == null || data.email.toString().isEmpty()) {
            binding.mailTxt.text = getString(R.string.from_empty_email_account)
        } else {
            binding.mailTxt.text = data.email
        }

        //TODO 로그인 플랫폼
        if (data.user_id?.startsWith("k") == true) {
            binding.joinRouteTxt.text = getString(R.string.from_kakao_login_account)
        } else if (data.user_id?.startsWith("f") == true) {
            binding.joinRouteTxt.text = getString(R.string.from_facebook_login_account)
        } else if (data.user_id?.startsWith("g") == true) {
            binding.joinRouteTxt.text = getString(R.string.from_google_login_account)
        } else if (data.user_id?.startsWith("n") == true) {
            binding.joinRouteTxt.text = getString(R.string.from_naver_login_account)
        } else if (data.user_id?.startsWith("a") == true) {
            binding.joinRouteTxt.text = getString(R.string.from_apple_login_account)
        }
    }

    private fun goChangeName() {
        val intent = Intent(this, ChangeNameActivity::class.java)
        changeNameLauncher.launch(intent)
    }

    private val changeNameLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.e("jung","changeNameLauncher")
                getUserProfile()
            }
        }

    private fun goWithdrawal() {
        val intent = Intent(this, WithdrawalActivity::class.java)
        startActivity(intent)
    }
}