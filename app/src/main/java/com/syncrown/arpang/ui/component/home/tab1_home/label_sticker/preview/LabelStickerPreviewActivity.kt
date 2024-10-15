package com.syncrown.arpang.ui.component.home.tab1_home.label_sticker.preview

import android.os.Bundle
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.databinding.ActivityLabelStickerPreviewBinding
import com.syncrown.arpang.ui.base.BaseActivity

class LabelStickerPreviewActivity : BaseActivity() {
    private lateinit var binding: ActivityLabelStickerPreviewBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityLabelStickerPreviewBinding.inflate(layoutInflater)
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
            AppDataPref.isLabelPreView = binding.previewBtn.isChecked
            AppDataPref.save(this)
        }



        binding.previewBtn.isChecked = AppDataPref.isLabelPreView
    }

}