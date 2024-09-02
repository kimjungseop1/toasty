package com.syncrown.toasty.ui.component.home.tab1_home.ar_guide

import android.os.Bundle
import com.syncrown.toasty.databinding.ActivityArprintGuideBinding
import com.syncrown.toasty.ui.base.BaseActivity

class ArGuideActivity:BaseActivity() {
    private lateinit var binding: ActivityArprintGuideBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityArprintGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener { finish() }
    }
}