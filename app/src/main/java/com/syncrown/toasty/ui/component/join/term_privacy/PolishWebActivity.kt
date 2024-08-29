package com.syncrown.toasty.ui.component.join.term_privacy

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.ActivityWebPolishBinding
import com.syncrown.toasty.ui.base.BaseActivity
import kotlinx.coroutines.launch

class PolishWebActivity : BaseActivity() {
    private lateinit var binding: ActivityWebPolishBinding

    override fun observeViewModel() {
        lifecycleScope.launch {

        }
    }

    override fun initViewBinding() {
        binding = ActivityWebPolishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val type = intent.getStringExtra("CONSENT_EXTRA")

        when (type) {
            "term" -> {
                binding.actionbar.actionTitle.text =
                    ContextCompat.getString(this, R.string.polish_term_title)
                binding.consentBtn.text = ContextCompat.getString(this, R.string.polish_term_btn)
            }

            "privacy" -> {
                binding.actionbar.actionTitle.text =
                    ContextCompat.getString(this, R.string.polish_privacy_title)
                binding.consentBtn.text = ContextCompat.getString(this, R.string.polish_privacy_btn)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.consentBtn.setOnClickListener {

        }
    }
}