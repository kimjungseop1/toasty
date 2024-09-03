package com.syncrown.toasty.ui.component.login.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.syncrown.toasty.AppDataPref
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.ActivtyLoginBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.component.join.main.JoinEmailActivity
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivtyLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            loginViewModel.handleSignInResult(result.data)
        }


    override fun observeViewModel() {
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
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.emailStart.setOnClickListener {
            if (AppDataPref.isEmailJoin) {
                goPwLogin()
            } else {
                goEmailJoin()
            }
        }

        binding.googleBtn.setOnClickListener {
            loginViewModel.initiateSignIn(this, signInLauncher)
        }

        binding.facebookBtn.setOnClickListener {
            //TODO 개발중
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
        startActivity(intent)
    }

    private fun goPwLogin() {
        val intent = Intent(this, LoginPwActivity::class.java)
        startActivity(intent)
    }
}