package com.syncrown.toasty.ui.component.home.tab1_home.search

import android.os.Bundle
import com.syncrown.toasty.databinding.ActivitySearchBinding
import com.syncrown.toasty.ui.base.BaseActivity

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