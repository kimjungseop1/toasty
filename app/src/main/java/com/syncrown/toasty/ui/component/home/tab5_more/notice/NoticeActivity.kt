package com.syncrown.toasty.ui.component.home.tab5_more.notice

import android.os.Bundle
import com.syncrown.toasty.databinding.ActivityNoticeBinding
import com.syncrown.toasty.ui.base.BaseActivity

class NoticeActivity:BaseActivity() {
    private lateinit var binding: ActivityNoticeBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}