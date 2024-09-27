package com.syncrown.toasty.ui.component.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.facebook.CallbackManager
import com.syncrown.toasty.databinding.ActivtyLoginBinding
import com.syncrown.toasty.network.NetworkResult
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.commons.DialogCommon
import com.syncrown.toasty.ui.component.home.MainActivity


class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivtyLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    private val callbackManager = CallbackManager.Factory.create()

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            loginViewModel.handleSignInResult(result.data)
        }


    override fun observeViewModel() {
        loginViewModel.checkMemberResponseLiveData().observe(this) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.let { data ->
                        when (data.msgCode) {
                            "SUCCESS" -> {
                                if (data.member_check == 1) {
                                    //TODO 회원이면 이메일 입력 화면으로 이동

                                    //TODO 회원이지만 SNS로 기가입 상태일때
                                    val dialogCommon = DialogCommon()
                                    dialogCommon.showAlreadyJoined(supportFragmentManager, {

                                    }, "kakao")
                                } else {
                                    //TODO 비회원이면 이메일 가입신청
                                }
                            }

                            "FAIL" -> {
                                Log.e(TAG, "실패")
                            }

                            "NONE_ID" -> {
                                Log.e(TAG, "아이디가 없음")
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

        loginViewModel.initialize(this)
        loginViewModel.signInResult.observe(this, Observer { result ->
            result.fold(
                onSuccess = { credential ->
                    val email = credential.id
                    Log.e(TAG, "Signed in as $email")
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
            //TODO 개발중
        }

        binding.goMainBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }
}