package com.syncrown.arpang.ui.component.home.tab1_home.manual

import android.os.Bundle
import com.syncrown.arpang.databinding.ActivityManualWebBinding
import com.syncrown.arpang.ui.base.BaseActivity

class UseManualWebDetailActivity: BaseActivity() {
    private lateinit var binding: ActivityManualWebBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityManualWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }


    }
}