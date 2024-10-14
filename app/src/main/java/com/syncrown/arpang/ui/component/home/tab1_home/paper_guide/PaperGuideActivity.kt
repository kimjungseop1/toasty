package com.syncrown.arpang.ui.component.home.tab1_home.paper_guide

import android.content.Intent
import android.os.Bundle
import com.syncrown.arpang.databinding.ActivityPaperGuideBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.DialogProgressCommon

class PaperGuideActivity : BaseActivity() {
    private lateinit var binding: ActivityPaperGuideBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityPaperGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.protoTypeLink.setOnClickListener {
            val dialogCommon = DialogProgressCommon()
            dialogCommon.showLoading(supportFragmentManager)
        }

        binding.connectStoreView.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("result_message", "GO_STORE")
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}