package com.syncrown.arpang.ui.component.home.tab1_home.preview

import android.content.Intent
import android.os.Bundle
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

        binding.actionbar.actionTitle.text = "용지 미리보기"
        binding.actionbar.actionEtc.text = "시작하기"
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