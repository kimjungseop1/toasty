package com.syncrown.arpang.ui.component.home.tab1_home.event

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.arpang.databinding.ActivityEventGuideBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.home.tab1_home.event.adapter.HorizontalEventListAdapter

class EventDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityEventGuideBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityEventGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        setEventList()
    }

    private fun setEventList() {
        val arrayList = ArrayList<String>()
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")

        val horizontalEventListAdapter = HorizontalEventListAdapter(arrayList, object : HorizontalEventListAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Log.e("jung","position : $position")
            }
        })
        binding.recyclerEventList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerEventList.adapter = horizontalEventListAdapter
    }
}