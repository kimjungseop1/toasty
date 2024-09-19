package com.syncrown.toasty.ui.component.login.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.facebook.CallbackManager
import com.syncrown.toasty.databinding.ActivtyLoginBinding
import com.syncrown.toasty.network.NetworkResult
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.commons.DialogCommon
import com.syncrown.toasty.ui.component.join.main.JoinEmailActivity


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

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        binding.inputEmailView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val email = binding.inputEmailView.text?.trim().toString()
                if (email.isEmpty() || email.matches(emailPattern)) {
                    binding.inputEmailView.isActivated = false
                    binding.emailValidTxt.visibility = View.GONE
                } else {
                    binding.inputEmailView.isActivated = true
                    binding.emailValidTxt.visibility = View.VISIBLE
                }

                if (email.isNotEmpty() && email.matches(emailPattern)) {
                    binding.emailStart.isSelected = true
                } else {
                    binding.emailStart.isSelected = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.emailStart.isSelected = false
        binding.emailStart.setOnClickListener {
            if (binding.emailStart.isSelected) {
//            loginViewModel.checkMember(binding.inputEmailView.text.toString())

//            goEmailJoin()
                goPwLogin()
            }
        }

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

    }

    private fun goEmailJoin() {
        val intent = Intent(this, JoinEmailActivity::class.java)
        intent.putExtra("INPUT_EMAIL_ADDRESS", binding.inputEmailView.text.toString())
        startActivity(intent)
    }

    private fun goPwLogin() {
        val intent = Intent(this, LoginPwActivity::class.java)
        intent.putExtra("INPUT_EMAIL_ADDRESS", binding.inputEmailView.text.toString())
        startActivity(intent)
    }
}