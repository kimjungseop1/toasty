package com.syncrown.arpang.ui.component.home.tab1_home.manual

import android.content.Intent
import android.os.Bundle
import com.syncrown.arpang.databinding.ActivityManualDepth2Binding
import com.syncrown.arpang.ui.base.BaseActivity

class UseManual2DepthActivity : BaseActivity() {
    private lateinit var binding: ActivityManualDepth2Binding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityManualDepth2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }
        binding.actionbar.actionTitle.text = "인쇄하기"

        //TODO 기본가이드
        binding.menuItem1.setOnClickListener {

        }

        //TODO AR영상
        //TODO 인생두컷
        //TODO 자유편집
        //TODO 라벨스티커
        //TODO 행사스티커
    }

    private fun goWebDetail() {
        val intent = Intent(this, UseManualWebDetailActivity::class.java)
        startActivity(intent)
    }
}