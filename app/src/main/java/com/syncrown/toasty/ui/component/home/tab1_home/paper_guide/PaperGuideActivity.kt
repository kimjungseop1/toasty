package com.syncrown.toasty.ui.component.home.tab1_home.paper_guide

import android.content.Intent
import android.os.Bundle
import com.syncrown.toasty.databinding.ActivityPaperGuideBinding
import com.syncrown.toasty.ui.base.BaseActivity

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

        binding.connectStoreView.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("result_message", "GO_STORE")
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}