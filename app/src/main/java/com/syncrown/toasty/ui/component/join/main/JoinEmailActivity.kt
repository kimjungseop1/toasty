package com.syncrown.toasty.ui.component.join.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.syncrown.toasty.databinding.ActivityEmailJoinBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.component.join.term_privacy.PolishWebActivity
import kotlinx.coroutines.launch

class JoinEmailActivity : BaseActivity() {
    private lateinit var binding: ActivityEmailJoinBinding
    private val joinEmailViewModel: JoinEmailViewModel by viewModels()

    override fun observeViewModel() {
        lifecycleScope.launch {

        }
    }

    override fun initViewBinding() {
        binding = ActivityEmailJoinBinding.inflate(layoutInflater)
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

        }
    }

    private fun goWebPolish(extra: String) {
        val intent = Intent(this, PolishWebActivity::class.java)
        intent.putExtra("CONSENT_EXTRA", extra)
        startActivity(intent)
    }
}