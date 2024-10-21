package com.syncrown.arpang.ui.component.home.tab5_more.notice.detail

import android.os.Bundle
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityNoticeDetailBinding
import com.syncrown.arpang.ui.base.BaseActivity

class NoticeDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityNoticeDetailBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityNoticeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.notice_title)


    }
}