package com.syncrown.arpang.ui.component.home.tab5_more.account.name

import android.os.Bundle
import com.syncrown.arpang.databinding.ActivityChangeNameBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CustomToast
import com.syncrown.arpang.ui.commons.CustomToastType

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

        binding.changeBtn.setOnClickListener {
            val customToast = CustomToast(this)
            customToast.showToast("이름이 변경되었습니다.", CustomToastType.BLUE)
        }
    }
}