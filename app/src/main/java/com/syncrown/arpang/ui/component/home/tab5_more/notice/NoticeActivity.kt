package com.syncrown.arpang.ui.component.home.tab5_more.notice

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.arpang.databinding.ActivityNoticeBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.home.tab5_more.notice.adapter.NoticeListAdapter

class NoticeActivity:BaseActivity() {
    private lateinit var binding: ActivityNoticeBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }
        binding.actionbar.actionTitle.text = "공지사항"

        setNoticeList()
    }

    private fun setNoticeList() {
        val arrayList = ArrayList<String>()
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")

        binding.recyclerNotice.layoutManager = LinearLayoutManager(this)
        binding.recyclerNotice.adapter = NoticeListAdapter(this, arrayList)
    }
}