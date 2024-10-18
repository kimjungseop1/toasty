package com.syncrown.arpang.ui.component.home.tab5_more.event

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.databinding.ActivityEventAllBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.VerticalSpaceItemDecoration
import com.syncrown.arpang.ui.component.home.tab5_more.event.adapter.EventAllListAdapter

class EventAllActivity : BaseActivity() {
    private lateinit var binding: ActivityEventAllBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityEventAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }
        binding.actionbar.actionTitle.text = "전체 이벤트"

        setEventList()
    }

    private fun setEventList() {
        clearItemDecorations(binding.recyclerEvent)

        val arrayList = ArrayList<String>()
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")

        val eventAllListAdapter = EventAllListAdapter(this, arrayList)
        binding.recyclerEvent.layoutManager = LinearLayoutManager(this)
        binding.recyclerEvent.adapter = eventAllListAdapter
        eventAllListAdapter.setOnItemClickListener(object :
            EventAllListAdapter.OnItemClickListener {
            override fun onClick(position: Int) {

            }
        })

        binding.recyclerEvent.addItemDecoration(
            VerticalSpaceItemDecoration(
                CommonFunc.dpToPx(
                    7,
                    this
                )
            )
        )
    }

    private fun clearItemDecorations(recyclerView: RecyclerView) {
        val decorationCount = recyclerView.itemDecorationCount
        for (i in decorationCount - 1 downTo 0) {
            recyclerView.removeItemDecorationAt(i)
        }
    }
}