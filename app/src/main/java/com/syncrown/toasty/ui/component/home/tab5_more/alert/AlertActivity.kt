package com.syncrown.toasty.ui.component.home.tab5_more.alert

import android.os.Bundle
import com.syncrown.toasty.databinding.ActivityAlertMainBinding
import com.syncrown.toasty.ui.base.BaseActivity

class AlertActivity:BaseActivity() {
    private lateinit var binding: ActivityAlertMainBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityAlertMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}