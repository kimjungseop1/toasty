package com.syncrown.arpang.ui.component.home.tab5_more.cutoff

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityCutoffMainBinding
import com.syncrown.arpang.ui.base.BaseActivity

class CutOffActivity:BaseActivity() {
    private lateinit var binding: ActivityCutoffMainBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityCutoffMainBinding.inflate(layoutInflater)
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

        binding.recyclerCut.layoutManager = LinearLayoutManager(this)
        binding.recyclerCut.adapter = CutOffAdapter(arrayList) { position ->
            Log.e("jung","position : $position")
        }
        binding.recyclerCut.setHasFixedSize(true)
    }
}