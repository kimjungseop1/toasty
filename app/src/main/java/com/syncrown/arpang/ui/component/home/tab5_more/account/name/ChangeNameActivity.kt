package com.syncrown.arpang.ui.component.home.tab5_more.account.name

import android.os.Bundle
import com.syncrown.arpang.R
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

        binding.actionbar.actionTitle.text = getString(R.string.account_change_name)

        binding.inputName.requestFocus()

        binding.changeBtn.setOnClickListener {
            val customToast = CustomToast()
            customToast.showToastMessage(
                supportFragmentManager,
                getString(R.string.account_change_name_desc_3),
                CustomToastType.BLUE
            ) {
                //close
            }
        }
    }
}