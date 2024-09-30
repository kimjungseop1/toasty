package com.syncrown.arpang.ui.component.home.manual

import android.os.Bundle
import com.syncrown.arpang.databinding.ActivityManualBinding
import com.syncrown.arpang.ui.base.BaseActivity

class ManualActivity:BaseActivity() {
    private lateinit var binding: ActivityManualBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityManualBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}