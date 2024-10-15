package com.syncrown.arpang.ui.component.home.tab1_home.ar_print.preview

import android.os.Bundle
import com.bumptech.glide.Glide
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.databinding.ActivityArPrintPreviewBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.ArImageStorage

class ArPrintPreviewActivity : BaseActivity() {
    private lateinit var binding: ActivityArPrintPreviewBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityArPrintPreviewBinding.inflate(layoutInflater)
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

        Glide.with(this)
            .load(ArImageStorage.bitmap)
            .into(binding.resultImg)

        binding.previewBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppDataPref.isArPrintPreView = binding.previewBtn.isChecked
                AppDataPref.save(this)
            }
        }

        binding.previewBtn.isChecked = AppDataPref.isArPrintPreView
    }
}