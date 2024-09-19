package com.syncrown.toasty.ui.component.home.tab5_more.account.name

import android.os.Bundle
import com.syncrown.toasty.databinding.ActivityChangeNameBinding
import com.syncrown.toasty.ui.base.BaseActivity

class ChangeNameActivity : BaseActivity() {
    private lateinit var binding: ActivityChangeNameBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityChangeNameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = "이름 변경"
    }
}