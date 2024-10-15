package com.syncrown.arpang.ui.component.home.tab1_home.festival_sticker.preview

import android.os.Bundle
import com.bumptech.glide.Glide
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.databinding.ActivityFestivalPreviewBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.home.tab1_home.festival_sticker.FestivalImageStorage

class FestivalPreviewActivity : BaseActivity() {
    private lateinit var binding: ActivityFestivalPreviewBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityFestivalPreviewBinding.inflate(layoutInflater)
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
            AppDataPref.isFestivalPreView = binding.previewBtn.isChecked
            AppDataPref.save(this)
        }

        Glide.with(this)
            .load(FestivalImageStorage.bitmap)
            .circleCrop()
            .into(binding.prevImg)

        binding.previewBtn.isChecked = AppDataPref.isFestivalPreView
    }
}