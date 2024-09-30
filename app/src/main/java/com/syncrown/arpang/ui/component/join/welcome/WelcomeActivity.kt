package com.syncrown.arpang.ui.component.join.welcome

import android.content.Intent
import android.os.Bundle
import com.syncrown.arpang.databinding.ActivityWelcomeBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.home.MainActivity

class WelcomeActivity:BaseActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun observeViewModel() {

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
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}