package com.syncrown.arpang.ui.component.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.facebook.CallbackManager
import com.syncrown.arpang.databinding.ActivtyLoginBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.home.MainActivity


class LoginActivity : BaseActivity(), AppleSignInDialog.Interaction {
    private lateinit var binding: ActivtyLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    private val callbackManager = CallbackManager.Factory.create()

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            loginViewModel.handleSignInResult(result.data)
        }


    override fun observeViewModel() {
        //TODO 회원여부 체크 옵져브
        loginViewModel.checkMemberResponseLiveData().observe(this) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.let { data ->
                        when (data.msgCode) {
                            "SUCCESS" -> {
                                Log.e(TAG, "성공")
                            }

                            "FAIL" -> {
                                Log.e(TAG, "실패")
                            }

                            "NONE_ID" -> {
                                Log.e(TAG, "아이디가 없음 회원가입으로")

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

        //TODO 가입신청 옵져브
        loginViewModel.joinMemberResponseLiveData().observe(this) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.let { data ->
                        when (data.msgCode) {
                            "DUPPLE" -> {
                                Log.e(TAG, "아이디 중복")
                            }

                            "SUCCESS" -> {
                                Log.e(TAG, "성공")
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
            val dialog = AppleSignInDialog(this, this)
            dialog.show()
        }

        binding.goMainBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }

    override fun onAppleEmailSuccess(email: String) {
        Toast.makeText(this, "Apple 로그인 성공! 이메일: $email", Toast.LENGTH_SHORT).show()

    }
}