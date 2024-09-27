package com.syncrown.arpang.ui.component.home.tab5_more.account.withdrawal

import android.os.Bundle
import com.syncrown.arpang.databinding.ActivityWithdrawalBinding
import com.syncrown.arpang.ui.base.BaseActivity

class WithdrawalActivity : BaseActivity() {
    private lateinit var binding: ActivityWithdrawalBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityWithdrawalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = "회원 탈퇴"
    }
}