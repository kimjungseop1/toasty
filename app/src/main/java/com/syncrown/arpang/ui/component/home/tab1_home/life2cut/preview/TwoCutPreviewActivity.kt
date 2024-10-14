package com.syncrown.arpang.ui.component.home.tab1_home.life2cut.preview

import android.os.Bundle
import com.bumptech.glide.Glide
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.databinding.ActivityTwoCutPreviewBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.TwoCutImageStorage

class TwoCutPreviewActivity : BaseActivity() {
    private lateinit var binding: ActivityTwoCutPreviewBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityTwoCutPreviewBinding.inflate(layoutInflater)
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
            AppDataPref.isTwoCutPreView = binding.previewBtn.isChecked
            AppDataPref.save(this)
        }

        Glide.with(this)
            .load(TwoCutImageStorage.bitmap)
            .into(binding.resultImg)

    }
}