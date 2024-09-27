package com.syncrown.arpang.ui.component.home.tab1_home.search

import android.os.Bundle
import com.syncrown.arpang.databinding.ActivitySearchBinding
import com.syncrown.arpang.ui.base.BaseActivity

class SearchActivity : BaseActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}