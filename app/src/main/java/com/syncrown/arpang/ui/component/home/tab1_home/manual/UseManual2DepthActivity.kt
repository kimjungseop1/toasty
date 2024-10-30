package com.syncrown.arpang.ui.component.home.tab1_home.manual

import android.content.Intent
import android.os.Bundle
import com.syncrown.arpang.R
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
        binding.actionbar.actionTitle.text = getString(R.string.manual_guide_3)

        //TODO 기본가이드
        binding.menuItem1.setOnClickListener {

        }

        //TODO AR영상
        binding.menuItem2.setOnClickListener {

        }

        //TODO 인생두컷
        binding.menuItem3.setOnClickListener {

        }

        //TODO 자유편집
        binding.menuItem4.setOnClickListener {

        }

        //TODO 라벨스티커
        binding.menuItem5.setOnClickListener {

        }

        //TODO 행사스티커
        binding.menuItem6.setOnClickListener {

        }
    }

    private fun goWebDetail() {
        val intent = Intent(this, UseManualWebDetailActivity::class.java)
        startActivity(intent)
    }
}