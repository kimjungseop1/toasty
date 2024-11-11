package com.syncrown.arpang.ui.component.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.facebook.CallbackManager
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.databinding.ActivtyLoginBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.home.MainActivity
import com.syncrown.arpang.ui.component.join.consent.JoinConsentActivity


class LoginActivity : BaseActivity(), AppleSignInDialog.Interaction {
    private lateinit var binding: ActivtyLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    private val callbackManager = CallbackManager.Factory.create()

    override fun observeViewModel() {
        //TODO 회원여부 체크 옵져브
        loginViewModel.checkMemberResponseLiveData().observe(this) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    goMain()
                }

                is NetworkResult.Error -> {
                    result.data?.let { data ->
                        when (data.msgCode) {
                            "NONE_ID" -> {
                                goJoinConsent()
                            }

                            "FAIL" -> {
                                Log.e(TAG, "오류")
                            }

                            else -> {}
                        }
                    }
                }
            }
        }

        //TODO 구글 로그인 관련
        loginViewModel.initialize(this)
        loginViewModel.googleAccount.observe(this, Observer { account ->
            if (account != null) {
                val userId = "g-" + account.id.toString()

                loginViewModel.checkMember(userId)
            }
        })
    }

    override fun initViewBinding() {
        binding = ActivtyLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.googleBtn.setOnClickListener {
            loginViewModel.signOut()
            val signInIntent = loginViewModel.getGoogleSignInClient().signInIntent
            signInLauncher.launch(signInIntent)
        }

        binding.facebookBtn.setOnClickListener {
            loginViewModel.facebookLogin(this, callbackManager)
        }

        binding.kakaoBtn.setOnClickListener {
            loginViewModel.kakaoLogin(this)
        }

        binding.naverBtn.setOnClickListener {
            loginViewModel.naverLogin(this)
        }

        binding.appleBtn.setOnClickListener {
            val dialog = AppleSignInDialog(this, this)
            dialog.show()
        }

        binding.goMainBtn.setOnClickListener {
            goMain()
        }

    }

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            loginViewModel.handleSignInResult(result.data)
        } else {
            loginViewModel.handleSignInResult(null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onAppleIdSuccess(sub: String) {
        val userId = "a-$sub"
        loginViewModel.checkMember(userId)
    }

    private fun goJoinConsent() {
        val intent = Intent(this, JoinConsentActivity::class.java)
        startActivity(intent)
    }

    private fun goMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}