package com.syncrown.arpang.ui.component.home.tab5_more.account

import android.content.Intent
import android.os.Bundle
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityAccountManageBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.home.tab5_more.account.name.ChangeNameActivity
import com.syncrown.arpang.ui.component.home.tab5_more.account.withdrawal.WithdrawalActivity

class AccountManageActivity : BaseActivity() {
    private lateinit var binding: ActivityAccountManageBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityAccountManageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.account_title)

        binding.clView2.setOnClickListener {
            goChangeName()
        }

        binding.withdrawalTxt.setOnClickListener {
            goWithdrawal()
        }

    }

    private fun goChangeName() {
        val intent = Intent(this, ChangeNameActivity::class.java)
        startActivity(intent)
    }

    private fun goWithdrawal() {
        val intent = Intent(this, WithdrawalActivity::class.java)
        startActivity(intent)
    }
}