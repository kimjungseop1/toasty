package com.syncrown.arpang.ui.component.home.tab5_more.block

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityBlockUserBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.home.tab5_more.block.adapter.BlockUserAdapter

class BlockUserActivity:BaseActivity() {
    private lateinit var binding: ActivityBlockUserBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityBlockUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.cutoff_title)

        val arrayList = ArrayList<String>()
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")

        binding.recyclerCut.layoutManager = LinearLayoutManager(this)
        binding.recyclerCut.adapter = BlockUserAdapter(arrayList) { position ->
            Log.e("jung","position : $position")
        }
        binding.recyclerCut.setHasFixedSize(true)
    }
}