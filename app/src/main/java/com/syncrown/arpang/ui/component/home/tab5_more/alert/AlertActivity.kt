package com.syncrown.arpang.ui.component.home.tab5_more.alert

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.arpang.databinding.ActivityAlertMainBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.home.tab5_more.alert.adapter.AlertListAdapter

class AlertActivity:BaseActivity() {
    private lateinit var binding: ActivityAlertMainBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityAlertMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = "알림"

        setTodayList()

        setWeekList()
    }

    private fun setTodayList() {
        val arrayList = ArrayList<String>()
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")

        binding.recyclerToday.layoutManager = LinearLayoutManager(this)
        binding.recyclerToday.adapter = AlertListAdapter(this, arrayList)
    }

    private fun setWeekList() {
        val arrayList = ArrayList<String>()
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")

        binding.recyclerWeek.layoutManager = LinearLayoutManager(this)
        binding.recyclerWeek.adapter = AlertListAdapter(this, arrayList)
    }
}