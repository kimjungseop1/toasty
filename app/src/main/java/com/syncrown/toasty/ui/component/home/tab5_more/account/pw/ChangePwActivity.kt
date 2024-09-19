package com.syncrown.toasty.ui.component.home.tab5_more.account.pw

import android.os.Bundle
import com.syncrown.toasty.databinding.ActivityChangePwBinding
import com.syncrown.toasty.ui.base.BaseActivity

class ChangePwActivity : BaseActivity() {
    private lateinit var binding: ActivityChangePwBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityChangePwBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = "비밀번호 변경"
    }
}