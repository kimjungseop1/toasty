package com.syncrown.toasty.ui.component.login.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.syncrown.toasty.AppData
import com.syncrown.toasty.databinding.ActivtyLoginBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.component.join.consent.JoinConsentActivity
import com.syncrown.toasty.ui.component.join.main.JoinEmailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

        binding.emailStart.setOnClickListener {
            if (AppData.isEmailJoin) {
                goConsent()
            } else {
                goEmailJoin()
            }
        }

        binding.googleBtn.setOnClickListener {

        }

        binding.facebookBtn.setOnClickListener {

        }

        binding.kakaoBtn.setOnClickListener {

        }

        binding.naverBtn.setOnClickListener {

        }

        binding.appleBtn.setOnClickListener {

        }

    }

    private fun goEmailJoin() {
        val intent = Intent(this, JoinEmailActivity::class.java)
        startActivity(intent)
    }

    private fun goConsent() {
        val intent = Intent(this, JoinConsentActivity::class.java)
        startActivity(intent)
    }
}