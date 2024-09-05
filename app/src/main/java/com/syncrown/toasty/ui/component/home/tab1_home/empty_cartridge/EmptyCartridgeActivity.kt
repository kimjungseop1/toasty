package com.syncrown.toasty.ui.component.home.tab1_home.empty_cartridge

import android.os.Bundle
import com.syncrown.toasty.databinding.ActivityEmptyCartridgeBinding
import com.syncrown.toasty.ui.base.BaseActivity

class EmptyCartridgeActivity : BaseActivity() {
    private lateinit var binding: ActivityEmptyCartridgeBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityEmptyCartridgeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionTitle: String = intent.getStringExtra("FROM_HOME_CATEGORY").toString()
        binding.actionbar.actionTitle.text = actionTitle
        binding.actionbar.actionBack.setOnClickListener { finish() }


    }
}