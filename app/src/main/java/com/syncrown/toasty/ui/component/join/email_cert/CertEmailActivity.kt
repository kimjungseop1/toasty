package com.syncrown.toasty.ui.component.join.email_cert

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.syncrown.toasty.databinding.ActivityCertEmailBinding
import com.syncrown.toasty.ui.base.BaseActivity
import kotlinx.coroutines.launch

class CertEmailActivity : BaseActivity() {
    private lateinit var binding: ActivityCertEmailBinding
    private val certEmailViewModel: CertEmailViewModel by viewModels()

    override fun observeViewModel() {
        lifecycleScope.launch {

        }
    }

    override fun initViewBinding() {
        binding = ActivityCertEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }


    }
}