package com.syncrown.arpang.ui.component.home.tab5_more.account.pw

import android.os.Bundle
import com.syncrown.arpang.databinding.ActivityChangePwBinding
import com.syncrown.arpang.ui.base.BaseActivity

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