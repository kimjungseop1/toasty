package com.syncrown.toasty.ui.component.home.tab1_home.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.toasty.databinding.FragmentCase1Binding
import com.syncrown.toasty.ui.commons.CommonFunc
import com.syncrown.toasty.ui.commons.HorizontalSpaceItemDecoration
import com.syncrown.toasty.ui.component.home.tab1_home.main.adapter.CaseListAdapter
import kotlinx.coroutines.launch

class Case1Fragment : Fragment() {
    private lateinit var binding: FragmentCase1Binding
    private lateinit var arrayList: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCase1Binding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            arrayList = ArrayList()

            setContentList()
            updateView()
        }
    }

    private fun setContentList() {
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")


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