package com.syncrown.arpang.ui.component.home.tab1_home.empty_cartridge.preview

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityPreviewCartridgeBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videoselect.VideoSelectActivity
import com.syncrown.arpang.ui.component.home.tab1_home.empty_cartridge.EmptyCartridgeActivity
import com.syncrown.arpang.ui.component.home.tab1_home.empty_cartridge.EmptyCartridgeActivity.Companion
import com.syncrown.arpang.ui.component.home.tab1_home.festival_sticker.EditFestivalActivity
import com.syncrown.arpang.ui.component.home.tab1_home.free_print.EditFreePrintActivity
import com.syncrown.arpang.ui.component.home.tab1_home.label_sticker.EditLabelStickerActivity
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.image_select.ImageSelectActivity

class PreViewCartridgeActivity : BaseActivity() {
    private lateinit var binding: ActivityPreviewCartridgeBinding
    companion object {
        private var actionTitle: String = ""
    }

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityPreviewCartridgeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionTitle = intent.getStringExtra("FROM_HOME_CATEGORY").toString()

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.cartridge_preview_title)
        binding.actionbar.actionEtc.text = getString(R.string.cartridge_preview_start)
        binding.actionbar.actionEtc.setOnClickListener {
            when(actionTitle) {
                getString(R.string.cartridge_empty_action_text_1) -> {
                    // ar 영상 인쇄
                    goSelectVideo()
                }

                getString(R.string.cartridge_empty_action_text_2) -> {
                    // 인생 두컷
                    goImageSelect()
                }

                getString(R.string.cartridge_empty_action_text_4) -> {
                    // 라벨 스티커
                    goLabelSticker()
                }

                getString(R.string.cartridge_empty_action_text_5) -> {
                    // 행사 스트커
                    goFestivalSticker()
                }

                getString(R.string.cartridge_empty_action_text_3) -> {
                    // 자유 인쇄
                    goFreePrint()
                }

            }
        }
    }

    private fun goSelectVideo() {
        val intent = Intent(this, VideoSelectActivity::class.java)
        intent.putExtra(
            "FROM_HOME_CATEGORY",
            getString(R.string.cartridge_empty_action_text_1)
        )
        startActivity(intent)
        finish()
    }

    private fun goImageSelect() {
        val intent = Intent(this, ImageSelectActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goFestivalSticker() {
        val intent = Intent(this, EditFestivalActivity::class.java)
        startActivity(intent)
    }

    private fun goLabelSticker() {
        val intent = Intent(this, EditLabelStickerActivity::class.java)
        startActivity(intent)
    }

    private fun goFreePrint() {
        val intent = Intent(this, EditFreePrintActivity::class.java)
        startActivity(intent)
    }
}