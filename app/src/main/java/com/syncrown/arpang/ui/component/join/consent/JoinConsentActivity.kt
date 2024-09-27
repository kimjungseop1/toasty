package com.syncrown.arpang.ui.component.join.consent

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.syncrown.arpang.databinding.ActivityConsentBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.join.term_privacy.PolishWebActivity
import com.syncrown.arpang.ui.component.join.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class JoinConsentActivity : BaseActivity() {
    private lateinit var binding: ActivityConsentBinding
    private val joinConsentViewModel:JoinConsentViewModel by viewModels()

    override fun observeViewModel() {
        lifecycleScope.launch {

        }
    }

    override fun initViewBinding() {
        binding = ActivityConsentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.consentDetail1.setOnClickListener {
            goWebPolish("term")
        }

        binding.consentDetail2.setOnClickListener {
            goWebPolish("privacy")
        }

        binding.joinBtn.setOnClickListener {
            goWelcome()
        }

    }

    private fun goWelcome() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }

    private fun goWebPolish(extra: String) {
        val intent = Intent(this, PolishWebActivity::class.java)
        intent.putExtra("CONSENT_EXTRA", extra)
        startActivity(intent)
    }
}