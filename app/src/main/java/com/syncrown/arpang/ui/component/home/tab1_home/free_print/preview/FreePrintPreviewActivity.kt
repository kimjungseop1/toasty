package com.syncrown.arpang.ui.component.home.tab1_home.free_print.preview

import android.os.Bundle
import com.syncrown.arpang.databinding.ActivityFreePrintPreviewBinding
import com.syncrown.arpang.ui.base.BaseActivity

class FreePrintPreviewActivity : BaseActivity() {
    private lateinit var binding: ActivityFreePrintPreviewBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityFreePrintPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }
        binding.actionbar.actionTitle.text = "미리보기"
        binding.actionbar.actionEtc.text = "인쇄"
        binding.actionbar.actionEtc.setOnClickListener {

        }

    }
}