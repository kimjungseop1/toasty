package com.syncrown.toasty.ui.component.home.tab2_Lib.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.BottomSheetCartridgeBinding
import com.syncrown.toasty.databinding.BottomSheetFilterBinding
import com.syncrown.toasty.databinding.BottomSheetTypeBinding
import com.syncrown.toasty.databinding.FragmentLibBinding
import com.syncrown.toasty.ui.commons.CommonFunc
import com.syncrown.toasty.ui.commons.GridSpacingItemDecoration
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.videoselect.adapter.GridItemAdapter
import com.syncrown.toasty.ui.component.home.tab2_Lib.main.adapter.GridItem
import com.syncrown.toasty.ui.component.home.tab2_Lib.main.adapter.LibLinearItemAdapter

class LibFragment : Fragment() {
    private lateinit var binding: FragmentLibBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibBinding.inflate(layoutInflater)

        binding.btn1.setOnClickListener {
            // 필터팝업
            showFilterBottomSheet()
        }

        binding.btn2.setOnClickListener {
            // 용지팝업
            showCartridgeBottomSheet()
        }

        binding.btn3.setOnClickListener {
            // 유형팝업
            showTypeBottomSheet()
        }

        binding.btn4.isSelected = true
        updateRecyclerUi()

        binding.btn4.setOnClickListener {
            binding.btn4.isSelected = !binding.btn4.isSelected
            updateRecyclerUi()
        }

        return binding.root
    }

    private fun updateRecyclerUi() {
        if (binding.btn4.isSelected) {
            showGridStyle()
        } else {
            showLinearStyle()
        }
    }

    private fun showFilterBottomSheet() {
        val binding = BottomSheetFilterBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.window?.setDimAmount(0.7f)
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setContentView(binding.root)

        binding.closeBtn.setOnClickListener { bottomSheetDialog.dismiss() }

        bottomSheetDialog.show()
    }

    private fun showCartridgeBottomSheet() {
        val binding = BottomSheetCartridgeBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.window?.setDimAmount(0.7f)
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setContentView(binding.root)

        binding.closeBtn.setOnClickListener { bottomSheetDialog.dismiss() }

        bottomSheetDialog.show()
    }

    private fun showTypeBottomSheet() {
        val binding = BottomSheetTypeBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.window?.setDimAmount(0.7f)
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setContentView(binding.root)

        binding.closeBtn.setOnClickListener { bottomSheetDialog.dismiss() }

        bottomSheetDialog.show()
    }

    private fun showGridStyle() {
        val items = listOf(
            GridItem(R.drawable.icon_home, "00:00"),
            GridItem(R.drawable.icon_home, "00:00"),
            GridItem(R.drawable.icon_home, "00:00"),
            GridItem(R.drawable.icon_home, "00:00")
        )

        val spanCount = 3 // 3 열 그리드 레이아웃
        val spacing = 3 // dp로 설정된 아이템 간 간격
        val includeEdge = false // 아이템의 가장자리에 간격 포함 여부

        binding.recyclerLib.layoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.recyclerLib.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                CommonFunc.dpToPx(spacing, requireContext()),
                includeEdge
            )
        )

        //binding.recyclerLib.adapter = GridItemAdapter(items)
    }

    private fun showLinearStyle() {
        val arrayList = ArrayList<String>()
        arrayList.add("1")
        arrayList.add("2")
        arrayList.add("3")
        arrayList.add("4")

        binding.recyclerLib.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerLib.adapter = LibLinearItemAdapter(arrayList)
    }
}