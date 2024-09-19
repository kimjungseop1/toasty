package com.syncrown.toasty.ui.component.login.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.syncrown.toasty.databinding.ActivityLoginPwBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.component.home.MainActivity
import com.syncrown.toasty.ui.component.login.find_pw.FindPwActivity
import kotlinx.coroutines.launch

class LoginPwActivity : BaseActivity() {
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

        binding.emailAddressTxt.text = intent.getStringExtra("INPUT_EMAIL_ADDRESS").toString()

        binding.inputPwView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.loginBtn.isSelected = binding.inputPwView.text.toString().isNotEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.loginBtn.isSelected = false
        binding.loginBtn.setOnClickListener {
            if (binding.loginBtn.isSelected) {
                Log.e("jung","활성화")

            } else {
                Log.e("jung","비활성화")

            }
        }

        binding.findPwBtn.setOnClickListener {
            goFindPw()
        }
    }

    private fun goMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun goFindPw() {
        val intent = Intent(this, FindPwActivity::class.java)
        intent.putExtra("INPUT_EMAIL_ADDRESS", binding.emailAddressTxt.text.toString())
        startActivity(intent)
    }
}