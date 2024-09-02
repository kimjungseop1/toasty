package com.syncrown.toasty.ui.component.login.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.syncrown.toasty.AppDataPref
import com.syncrown.toasty.databinding.ActivtyLoginBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.component.join.main.JoinEmailActivity
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivtyLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun observeViewModel() {
        lifecycleScope.launch {

        }
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
            loginViewModel.googleLogin()
        }

        binding.facebookBtn.setOnClickListener {
            loginViewModel.facebookLogin()
        }

        binding.kakaoBtn.setOnClickListener {
            loginViewModel.kakaoLogin()
        }

        binding.naverBtn.setOnClickListener {
            loginViewModel.naverLogin()
        }

        binding.appleBtn.setOnClickListener {
            loginViewModel.appleLogin()
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