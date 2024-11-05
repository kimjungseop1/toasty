package com.syncrown.arpang.ui.component.home.tab5_more.account.withdrawal

import android.os.Bundle
import android.text.SpannableString
import android.text.style.LeadingMarginSpan
import com.syncrown.arpang.R
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

        binding.actionbar.actionTitle.text = getString(R.string.more_account_withdrawal)

        val text = getString(R.string.account_withdrawal_polish)
        val spannableString = SpannableString(text)
        spannableString.setSpan(LeadingMarginSpan.Standard(0, 50), text.indexOf("1."), text.length, 0)
        spannableString.setSpan(LeadingMarginSpan.Standard(0, 0), text.indexOf("2."), text.length, 0)
        spannableString.setSpan(LeadingMarginSpan.Standard(0, 0), text.indexOf("3."), text.length, 0)
        binding.contentTextView.text = spannableString

        binding.withdrawalCheckView.setOnCheckedChangeListener { _, isChecked ->
            binding.withdrawalBtn.isSelected = isChecked
        }

        binding.withdrawalBtn.isSelected = false
        binding.withdrawalBtn.setOnClickListener {

        }
    }
}