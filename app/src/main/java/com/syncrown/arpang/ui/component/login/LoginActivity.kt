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
                    result.data?.let { data ->
                        when (data.msgCode) {
                            "SUCCESS" -> {
                                goMain()
                            }

                            "FAIL" -> {
                                Log.e(TAG, "오류")
                            }

                            "NONE_ID" -> {
                                goJoinConsent()
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

        //TODO 구글 로그인 관련
        loginViewModel.initialize(this)
        loginViewModel.signInResult.observe(this, Observer { result ->
            result.fold(
                onSuccess = { credential ->
                    val userId = credential.id
                    AppDataPref.userId = userId.toString()
                    AppDataPref.save(this)

                    Log.e(TAG, "Signed in as $userId")
                    loginViewModel.checkMember(userId.toString())
                },
                onFailure = { exception ->
                    Log.e(TAG, "Sign-in failed: ${exception.message}")
                }
            )
        })
    }

    override fun initViewBinding() {
        binding = ActivtyLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.googleBtn.setOnClickListener {
            loginViewModel.initiateSignIn(this, signInLauncher)
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
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            loginViewModel.handleSignInResult(result.data)
        }

    override fun onAppleIdSuccess(sub: String) {
        val userId = "a $sub"

        AppDataPref.userId = userId
        AppDataPref.save(this)

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