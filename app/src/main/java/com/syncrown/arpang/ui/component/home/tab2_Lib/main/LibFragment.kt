package com.syncrown.arpang.ui.component.home.tab2_Lib.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.BottomSheetCartridgeBinding
import com.syncrown.arpang.databinding.BottomSheetFilterBinding
import com.syncrown.arpang.databinding.BottomSheetTypeBinding
import com.syncrown.arpang.databinding.FragmentLibBinding
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import com.syncrown.arpang.ui.component.home.tab2_Lib.detail.LibDetailActivity
import com.syncrown.arpang.ui.component.home.tab2_Lib.main.adapter.GridItem
import com.syncrown.arpang.ui.component.home.tab2_Lib.main.adapter.LibGridItemAdapter
import com.syncrown.arpang.ui.component.home.tab2_Lib.main.adapter.MultiSelectAdapter


class LibFragment : Fragment(), LibGridItemAdapter.OnItemClickListener,
    MultiSelectAdapter.OnItemSelectedListener {
    private lateinit var binding: FragmentLibBinding

    private val itemList = listOf("전체", "AR 영상", "인생네컷", "자유인쇄", "라벨 스티커", "행사 스티커")
    private lateinit var categoryAdapter: MultiSelectAdapter

    companion object {
        private const val GRID_SPAN_COUNT = 3
        private const val LINEAR_SPAN_COUNT = 1
        private const val SPACE = 3
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterBtn.setOnClickListener {
            // 필터팝업
            showFilterBottomSheet()
        }

        binding.paperBtn.setOnClickListener {
            // 용지팝업
            showCartridgeBottomSheet()
        }

        categoryAdapter = MultiSelectAdapter(requireContext(), itemList, this)
        binding.contentsBtn.text = itemList[0]
        binding.contentsBtn.setOnClickListener {
            // 컨텐츠팝업
            showTypeBottomSheet()
        }

        showGridStyle(GRID_SPAN_COUNT, SPACE)
        binding.gridBtn.isSelected = true
        binding.linearBtn.isSelected = false

        binding.gridBtn.setOnClickListener {
            binding.gridBtn.isSelected = true
            binding.linearBtn.isSelected = false
            showGridStyle(GRID_SPAN_COUNT, SPACE)
        }

        binding.linearBtn.setOnClickListener {
            binding.gridBtn.isSelected = false
            binding.linearBtn.isSelected = true
            showGridStyle(LINEAR_SPAN_COUNT, SPACE)
        }
    }

    private fun showFilterBottomSheet() {
        val binding = BottomSheetFilterBinding.inflate(layoutInflater)
        val bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
        bottomSheetDialog.window?.setDimAmount(0.7f)
        bottomSheetDialog.setContentView(binding.root)

        binding.closeBtn.setOnClickListener { bottomSheetDialog.dismiss() }

        bottomSheetDialog.show()
    }

    private fun showCartridgeBottomSheet() {
        val binding = BottomSheetCartridgeBinding.inflate(layoutInflater)
        val bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
        bottomSheetDialog.window?.setDimAmount(0.7f)
        bottomSheetDialog.setContentView(binding.root)

        binding.concentration1.setOnClickListener {
            binding.concentration1.isSelected = true
            binding.concentration2.isSelected = false
            binding.concentration3.isSelected = false
        }

        binding.concentration2.setOnClickListener {
            binding.concentration1.isSelected = false
            binding.concentration2.isSelected = true
            binding.concentration3.isSelected = false
        }

        binding.concentration3.setOnClickListener {
            binding.concentration1.isSelected = false
            binding.concentration2.isSelected = false
            binding.concentration3.isSelected = true
        }

        binding.closeBtn.setOnClickListener { bottomSheetDialog.dismiss() }

        bottomSheetDialog.show()
    }

    private fun showTypeBottomSheet() {
        val sheetBinding = BottomSheetTypeBinding.inflate(layoutInflater)
        val bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
        bottomSheetDialog.window?.setDimAmount(0.7f)
        bottomSheetDialog.setContentView(sheetBinding.root)

        sheetBinding.closeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        sheetBinding.recyclerCate.layoutManager = LinearLayoutManager(requireContext())
        sheetBinding.recyclerCate.adapter = categoryAdapter

        sheetBinding.submitBtn.setOnClickListener {
            val selectedCategories = categoryAdapter.getSelectedItems()
            Log.e("jung", "size : " + selectedCategories.size)
            updateSelectedCategories(selectedCategories)

            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun showGridStyle(spanCount: Int, spacing: Int) {
        val items = listOf(
            GridItem(R.drawable.sample_img_1, "00:00"),
            GridItem(R.drawable.sample_img_1, "00:00"),
            GridItem(R.drawable.sample_img_1, "00:00"),
            GridItem(R.drawable.sample_img_1, "00:00"),
            GridItem(R.drawable.sample_img_1, "00:00"),
            GridItem(R.drawable.sample_img_1, "00:00"),
            GridItem(R.drawable.sample_img_1, "00:00"),
        )

        clearItemDecorations(binding.recyclerLib)

        binding.recyclerLib.layoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.recyclerLib.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                CommonFunc.dpToPx(spacing, requireContext()),
                false
            )
        )

        binding.recyclerLib.adapter = LibGridItemAdapter(items, spanCount, this)
    }

    private fun clearItemDecorations(recyclerView: RecyclerView) {
        val decorationCount = recyclerView.itemDecorationCount
        for (i in decorationCount - 1 downTo 0) {
            recyclerView.removeItemDecorationAt(i)
        }
    }

    // 보관함 목록의 아이템을 선택
    override fun onItemClick(position: Int) {
        Log.e("jung", "click pos : $position")
        goDetail()
    }

    // 전체 컨텐츠 팝업화면의 리스트 아이템 선택
    override fun onItemSelected(position: Int, isSelected: Boolean) {

    }

    private fun updateSelectedCategories(selectedCategories: List<Int>) {
        binding.contentsBtn.text = itemList[selectedCategories[0]]

        if (categoryAdapter.getSelectedItemCount() > 1) {
            binding.selectContentCnt.visibility = View.VISIBLE
        } else {
            binding.selectContentCnt.visibility = View.GONE
        }
        binding.selectContentCnt.text = categoryAdapter.getSelectedItemCount().toString()
    }

    private fun goDetail() {
        val intent = Intent(requireContext(), LibDetailActivity::class.java)
        startActivity(intent)
    }
}