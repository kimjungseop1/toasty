package com.syncrown.arpang.ui.component.home.tab1_home.manual

import android.content.Intent
import android.os.Bundle
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityManualDepth1Binding
import com.syncrown.arpang.ui.base.BaseActivity

class UseManual1DepthActivity : BaseActivity() {
    private lateinit var binding: ActivityManualDepth1Binding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityManualDepth1Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.guide_title)

        //TODO 장치연결
        binding.menuItem1.setOnClickListener {

        }

        //TODO 장치구매
        binding.menuItem2.setOnClickListener {

        }

        //TODO 인쇄하기
        binding.menuItem3.setOnClickListener {
            goDepth2()
        }
    }

    private fun goDepth2() {
        val intent = Intent(this, UseManual2DepthActivity::class.java)
        startActivity(intent)
    }

    private fun goWebDetail() {
        val intent = Intent(this, UseManualWebDetailActivity::class.java)
        startActivity(intent)
    }
}