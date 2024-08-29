package com.syncrown.toasty.ui.component.join.welcome

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.syncrown.toasty.databinding.ActivityWelcomeBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.component.home.MainActivity
import kotlinx.coroutines.launch

class WelcomeActivity:BaseActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun observeViewModel() {
        lifecycleScope.launch {

        }
    }

    override fun initViewBinding() {
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.welcomeStart.setOnClickListener {
            goMain()
        }

    }

    private fun goMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}