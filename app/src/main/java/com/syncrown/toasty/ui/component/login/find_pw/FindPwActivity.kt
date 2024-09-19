package com.syncrown.toasty.ui.component.login.find_pw

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.ActivityFindPwBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.commons.CustomToast
import com.syncrown.toasty.ui.commons.CustomToastType
import kotlinx.coroutines.launch

class FindPwActivity : BaseActivity() {
    private lateinit var binding: ActivityFindPwBinding
    private val findPwViewModel: FindPwViewModel by viewModels()

    override fun observeViewModel() {
        lifecycleScope.launch {

        }
    }

    override fun initViewBinding() {
        binding = ActivityFindPwBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener { finish() }

        binding.inputEmailView.text = intent.getStringExtra("INPUT_EMAIL_ADDRESS").toString()

        binding.sendBtn.setOnClickListener {
            //이메일 재전송

        }
    }

    private fun showToast() {
        val customToast = CustomToast(this)
        customToast.showToast(getString(R.string.password_re_send_success), CustomToastType.SUCCESS)
    }
}