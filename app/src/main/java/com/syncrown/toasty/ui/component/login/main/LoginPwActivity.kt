package com.syncrown.toasty.ui.component.login.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.syncrown.toasty.databinding.ActivityLoginPwBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.component.home.MainActivity
import kotlinx.coroutines.launch

class LoginPwActivity:BaseActivity() {
    private lateinit var binding: ActivityLoginPwBinding
    private val loginPwViewModel: LoginPwViewModel by viewModels()

    override fun observeViewModel() {
        lifecycleScope.launch {

        }
    }

    override fun initViewBinding() {
        binding = ActivityLoginPwBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.loginBtn.setOnClickListener {
            goMain()
        }

        binding.findPwBtn.setOnClickListener {
            loginPwViewModel.findPassWord()
        }
    }

    private fun goMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}