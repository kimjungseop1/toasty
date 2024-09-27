package com.syncrown.toasty.ui.component.home.tab5_more.cutoff

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.toasty.databinding.ActivityCutoffMainBinding
import com.syncrown.toasty.ui.base.BaseActivity

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

        binding.actionbar.actionTitle.text = "차단관리"

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