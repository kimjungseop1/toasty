package com.syncrown.arpang.ui.component.home.tab5_more.scrap

import android.os.Bundle
import com.syncrown.arpang.databinding.ActivityScrapMainBinding
import com.syncrown.arpang.ui.base.BaseActivity

class ScrapActivity : BaseActivity() {
    private lateinit var binding: ActivityScrapMainBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityScrapMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}