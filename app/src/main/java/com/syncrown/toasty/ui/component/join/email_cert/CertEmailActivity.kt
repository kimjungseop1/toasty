package com.syncrown.toasty.ui.component.join.email_cert

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.ActivityCertEmailBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.commons.CustomToast
import com.syncrown.toasty.ui.commons.CustomToastType
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

        //TODO 인증번호 오류 토스트
        val customToast = CustomToast(this)
        customToast.showToast(getString(R.string.cert_email_fail_code_num), CustomToastType.FAIL)

        //TODO 인증번호 재전송 토스트
        customToast.showToast(getString(R.string.cert_email_success_code_num), CustomToastType.SUCCESS)
    }
}