package com.syncrown.arpang.ui.component.home.tab1_home.empty_cartridge.preview

import android.content.Intent
import android.os.Bundle
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityPreviewCartridgeBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videoselect.VideoSelectActivity

class PreViewCartridgeActivity : BaseActivity() {
    private lateinit var binding: ActivityPreviewCartridgeBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityPreviewCartridgeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.cartridge_preview_title)
        binding.actionbar.actionEtc.text = getString(R.string.cartridge_preview_start)
        binding.actionbar.actionEtc.setOnClickListener {
            goStart()
        }
    }

    private fun goStart() {
        val intent = Intent(this, VideoSelectActivity::class.java)
        startActivity(intent)
        finish()
    }
}