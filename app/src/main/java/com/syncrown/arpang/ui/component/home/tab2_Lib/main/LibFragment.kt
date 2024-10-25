package com.syncrown.arpang.ui.component.home.tab2_Lib.main

import android.content.Context
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.BottomSheetCartridgeBinding
import com.syncrown.arpang.databinding.BottomSheetCartridgeSelectBinding
import com.syncrown.arpang.databinding.BottomSheetFilterBinding
import com.syncrown.arpang.databinding.BottomSheetTypeBinding
import com.syncrown.arpang.databinding.FragmentLibBinding
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import com.syncrown.arpang.ui.component.home.tab2_Lib.detail.LibDetailActivity
import com.syncrown.arpang.ui.component.home.tab2_Lib.main.adapter.CartridgeMultiSelectAdapter
import com.syncrown.arpang.ui.component.home.tab2_Lib.main.adapter.FilterCategoryAdapter
import com.syncrown.arpang.ui.component.home.tab2_Lib.main.adapter.FilterChildAdapter
import com.syncrown.arpang.ui.component.home.tab2_Lib.main.adapter.FilterSelectAdapter
import com.syncrown.arpang.ui.component.home.tab2_Lib.main.adapter.GridItem
import com.syncrown.arpang.ui.component.home.tab2_Lib.main.adapter.LibGridItemAdapter
import com.syncrown.arpang.ui.component.home.tab2_Lib.main.adapter.MultiSelectAdapter
import java.io.IOException


class LibFragment : Fragment(), LibGridItemAdapter.OnItemClickListener,
    MultiSelectAdapter.OnItemSelectedListener, CartridgeMultiSelectAdapter.OnCartridgeItemSelectedListener {
    private lateinit var binding: FragmentLibBinding

    private lateinit var childAdapter: FilterChildAdapter

    // 선택된 아이템들을 저장하는 리스트
    private val selectedItemsList = mutableListOf<String>()
    private lateinit var selectedItemsAdapter: FilterSelectAdapter
    private val selectedItemsMap = mutableMapOf<Int, MutableList<String>>()
    private val childAdapters = mutableMapOf<Int, FilterChildAdapter>()

    private val cartridgeList = listOf("전체", "마리 앙뜨와네트2세", "다용도 용지", "현상수배 용지", "스튜디오 용지", "사세대 이름이 긴 용지")
    private lateinit var cartridgeMultiSelectAdapter: CartridgeMultiSelectAdapter

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

        cartridgeMultiSelectAdapter = CartridgeMultiSelectAdapter(requireContext(), cartridgeList, this)
        binding.paperBtn.text = cartridgeList[0]
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

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.isDraggable = false
        behavior.isHideable = false

        val categories = loadJsonFromAssets(requireContext(), "filter_list.json")
        //공개설정, 태그
        binding.recyclerParent.layoutManager = LinearLayoutManager(requireContext())
        val categoryAdapter =
            FilterCategoryAdapter(requireContext(), categories) { categoryIndex, childAdapter ->
                this.childAdapter = childAdapter

                childAdapters[categoryIndex] = childAdapter

                this.childAdapter.setOnSelectionChangedListener { selectedPositions ->
                    updateSelectedItemsMap(
                        categories[categoryIndex].parentName,
                        categoryIndex,
                        selectedPositions,
                        childAdapter
                    )
                    updateSelectedItemsList()
                }
                setChildAdapterSelectionListener(
                    categoryIndex,
                    this.childAdapter,
                    categories[categoryIndex].parentName
                )
                binding.recyclerChild.adapter = this.childAdapter
            }
        binding.recyclerParent.adapter = categoryAdapter

        //자식 뷰
        binding.recyclerChild.layoutManager = LinearLayoutManager(requireContext())
        this.childAdapter = categoryAdapter.getChildAdapter(0)
        childAdapters[0] = this.childAdapter

        // 초기 선택 리스너 설정 및 강제 호출
        setChildAdapterSelectionListener(0, this.childAdapter, categories[0].parentName)
        binding.recyclerChild.adapter = this.childAdapter

        //선택된 항목
        binding.recyclerSelect.layoutManager = LinearLayoutManager(requireContext())
        selectedItemsAdapter = FilterSelectAdapter(requireContext(), emptyList()) { item ->
            removeItemFromSelectedItems(item)
        }

        binding.recyclerSelect.adapter = selectedItemsAdapter

        // 처음 로드된 카테고리의 선택 항목도 업데이트
        updateSelectedItemsMap(
            categories[0].parentName,
            0,
            this.childAdapter.getSelectedPositions(),
            this.childAdapter
        )
        updateSelectedItemsList()

        binding.resetBtn.setOnClickListener {
            clearAllSelectedItems()
        }

        binding.submitBtn.setOnClickListener {
            selectFilterCnt()
            bottomSheetDialog.dismiss()
        }

        binding.closeBtn.setOnClickListener {
            clearAllSelectedItems()

            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun clearAllSelectedItems() {
        selectedItemsList.clear()
        selectedItemsMap.clear()

        // 모든 childAdapter에서 선택된 항목을 해제
        childAdapters.values.forEach { adapter ->
            adapter.clearSelections()
        }

        // FilterSelectAdapter에 빈 리스트를 전달하여 업데이트
        selectedItemsAdapter.updateItems(emptyList())
    }


    private fun removeItemFromSelectedItems(item: String) {
        val parts = item.split(" > ")
        if (parts.size < 2) return

        val parentName = parts[0]
        val childName = parts[1]

        val categoryIndex = selectedItemsMap.entries.find { entry ->
            entry.value.any { it.contains(parentName) }
        }?.key

        if (categoryIndex != null) {
            selectedItemsMap[categoryIndex]?.remove("$parentName > $childName")

            childAdapters[categoryIndex]?.let { childAdapter ->
                val position = childAdapter.getPositionOfChild(childName)
                if (position != -1) {
                    childAdapter.deselectPosition(position) // 선택 해제
                }
            }

            updateSelectedItemsList()
        }
    }

    private fun setChildAdapterSelectionListener(
        categoryIndex: Int,
        childAdapter: FilterChildAdapter,
        parentName: String
    ) {
        childAdapter.setOnSelectionChangedListener { selectedPositions ->
            updateSelectedItemsMap(parentName, categoryIndex, selectedPositions, childAdapter)
            updateSelectedItemsList()
        }
    }

    private fun updateSelectedItemsMap(
        parentName: String,
        categoryIndex: Int,
        selectedPositions: Set<Int>,
        childAdapter: FilterChildAdapter
    ) {
        val selectedItemsForCategory = mutableListOf<String>()
        selectedPositions.forEach { position ->
            val childName = childAdapter.getChildName(position)
            selectedItemsForCategory.add("$parentName > $childName")
        }
        selectedItemsMap[categoryIndex] = selectedItemsForCategory
    }

    private fun updateSelectedItemsList() {
        val allSelectedItems = selectedItemsMap.values.flatten()
        selectedItemsAdapter.updateItems(allSelectedItems)
    }

    private fun loadJsonFromAssets(context: Context, fileName: String): List<Category> {
        val json: String
        try {
            json = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return emptyList()
        }

        val wrapper = Gson().fromJson(json, CategoryWrapper::class.java)
        return wrapper.category
    }

    private fun selectFilterCnt() {
        if (selectedItemsAdapter.getSelectItemSize() == 0) {
            binding.selectFilterCnt.visibility = View.GONE
        } else {
            binding.selectFilterCnt.visibility = View.VISIBLE
            binding.selectFilterCnt.text = selectedItemsAdapter.getSelectItemSize().toString()
        }
    }

    private fun showCartridgeBottomSheet() {
        val sheetBinding = BottomSheetCartridgeSelectBinding.inflate(layoutInflater)
        val bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
        bottomSheetDialog.window?.setDimAmount(0.7f)
        bottomSheetDialog.setContentView(sheetBinding.root)

        sheetBinding.closeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        sheetBinding.recyclerCate.layoutManager = LinearLayoutManager(requireContext())
        sheetBinding.recyclerCate.adapter = cartridgeMultiSelectAdapter

        sheetBinding.submitBtn.setOnClickListener {
            val selectedCartridge = cartridgeMultiSelectAdapter.getSelectedItems()
            updateSelectedCartridge(selectedCartridge)

            bottomSheetDialog.dismiss()
        }

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

    // 용지 선택 팝업
    override fun onCartridgeItemSelected(position: Int, isSelected: Boolean) {

    }

    private fun updateSelectedCartridge(selectedCartridge: List<Int>) {
        binding.paperBtn.text = cartridgeList[selectedCartridge[0]]

        if (cartridgeMultiSelectAdapter.getSelectedItemCount() > 1) {
            binding.selectCartridgeCnt.visibility = View.VISIBLE
        } else {
            binding.selectCartridgeCnt.visibility = View.GONE
        }
        binding.selectCartridgeCnt.text = cartridgeMultiSelectAdapter.getSelectedItemCount().toString()
    }

    private fun goDetail() {
        val intent = Intent(requireContext(), LibDetailActivity::class.java)
        startActivity(intent)
    }
}