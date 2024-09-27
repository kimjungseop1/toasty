package com.syncrown.toasty.ui.component.home.tab1_home.event

import android.os.Bundle
import com.syncrown.toasty.databinding.ActivityEventGuideBinding
import com.syncrown.toasty.ui.base.BaseActivity

class EventGuideActivity : BaseActivity() {
    private lateinit var binding: ActivityEventGuideBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityEventGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener { finish() }
    }
}