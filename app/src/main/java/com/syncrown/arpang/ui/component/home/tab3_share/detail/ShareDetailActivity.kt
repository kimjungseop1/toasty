package com.syncrown.arpang.ui.component.home.tab3_share.detail

import android.os.Bundle
import com.syncrown.arpang.databinding.ActivityShareDetailBinding
import com.syncrown.arpang.ui.base.BaseActivity

class ShareDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityShareDetailBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityShareDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = "공유공간"

        binding.actionbar.actionScrap.setOnClickListener {

        }

        binding.actionbar.actionPrint.setOnClickListener {

        }

        binding.actionbar.actionMore.setOnClickListener {

        }

        showContentView()
    }

    private fun showContentView() {
        
    }
}