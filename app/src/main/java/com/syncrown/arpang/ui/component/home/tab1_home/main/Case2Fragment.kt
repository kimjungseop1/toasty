package com.syncrown.arpang.ui.component.home.tab1_home.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.arpang.databinding.FragmentCase2Binding
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.HorizontalSpaceItemDecoration
import com.syncrown.arpang.ui.component.home.tab1_home.main.adapter.CaseListAdapter

class Case2Fragment : Fragment() {
    private lateinit var binding: FragmentCase2Binding
    private lateinit var arrayList: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCase2Binding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrayList = ArrayList()
        setContentList()
        updateView()
    }

    private fun setContentList() {
        binding.recyclerCase.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val caseAdapter = CaseListAdapter(requireContext(), arrayList)
        caseAdapter.setOnItemClickListener(object : CaseListAdapter.OnItemClickListener {
            override fun onClick(position: Int) {
                Log.e("jung", "click $position")
            }
        })
        binding.recyclerCase.adapter = caseAdapter

        binding.recyclerCase.addItemDecoration(
            HorizontalSpaceItemDecoration(
                CommonFunc.dpToPx(
                    12,
                    requireContext()
                )
            )
        )
    }

    private fun updateView() {
        if (arrayList.size == 0) {
            binding.recyclerCase.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.recyclerCase.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
        }
    }
}