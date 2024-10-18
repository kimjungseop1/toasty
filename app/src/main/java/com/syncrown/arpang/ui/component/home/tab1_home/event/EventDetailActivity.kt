package com.syncrown.arpang.ui.component.home.tab1_home.event

import android.os.Bundle
import com.syncrown.arpang.databinding.ActivityEventGuideBinding
import com.syncrown.arpang.ui.base.BaseActivity

class EventDetailActivity : BaseActivity() {
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