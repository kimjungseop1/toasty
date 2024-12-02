package com.syncrown.arpang.ui.component.home.tab3_share.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.BottomSheetShareBinding
import com.syncrown.arpang.databinding.FragmentShareBinding
import com.syncrown.arpang.network.model.RequestShareContentAllOpenListDto
import com.syncrown.arpang.network.model.ResponseShareContentAllOpenListDto
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import com.syncrown.arpang.ui.component.home.MainViewModel
import com.syncrown.arpang.ui.component.home.tab3_share.detail.ShareDetailActivity
import com.syncrown.arpang.ui.component.home.tab3_share.main.adapter.ShareGridItemAdapter
import com.syncrown.arpang.ui.component.home.tab3_share.main.adapter.ShareMultiSelectAdapter

class ShareFragment : Fragment(),
    ShareGridItemAdapter.OnItemClickListener,
    ShareMultiSelectAdapter.OnItemSelectedListener {

    // View Binding
    private lateinit var binding: FragmentShareBinding

    // ViewModel
    private val shareViewModel: MainViewModel by activityViewModels()

    // Variables
    private var currentPage = 1
    private var menuCode = ""
    private var isLoading = false
    private var hasMoreData = true
    private var currentSelectMode = -1

    private lateinit var adapter: ShareGridItemAdapter
    private val data = ArrayList<ResponseShareContentAllOpenListDto.Root>()

    // Constants
    private val itemList = listOf("전체", "인생 두컷", "자유 인쇄")
    private lateinit var categoryAdapter: ShareMultiSelectAdapter

    companion object {
        private const val GRID_SPAN_COUNT = 3
        private const val LINEAR_SPAN_COUNT = 1
        private const val SPACE = 3
    }

    @OptIn(UnstableApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Category Adapter 설정
        categoryAdapter = ShareMultiSelectAdapter(requireContext(), itemList, this)
        setupUI()

        observeShareList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        shareViewModel.clearShareListData()
    }

    // UI 초기화
    private fun setupUI() {
        binding.contentTypeView.text = itemList[0]
        binding.contentTypeView.setOnClickListener { showTypeBottomSheet() }

        binding.gridBtn.setOnClickListener {
            setLayoutStyle(GRID_SPAN_COUNT, true)
        }

        binding.linearBtn.setOnClickListener {
            setLayoutStyle(LINEAR_SPAN_COUNT, false)
        }

        binding.gridBtn.performClick()
    }

    private fun setLayoutStyle(spanCount: Int, isGridSelected: Boolean) {
        binding.gridBtn.isSelected = isGridSelected
        binding.linearBtn.isSelected = !isGridSelected

        currentSelectMode = spanCount
        showGridStyle(currentSelectMode, SPACE)
    }

    private fun showTypeBottomSheet() {
        val bottomSheetBinding = BottomSheetShareBinding.inflate(layoutInflater)
        val bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme).apply {
                window?.setDimAmount(0.7f)
                setContentView(bottomSheetBinding.root)
            }

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.isDraggable = false
        behavior.isHideable = false

        setupBottomSheetActions(bottomSheetBinding, bottomSheetDialog)
        bottomSheetDialog.show()
    }

    private fun setupBottomSheetActions(
        bottomSheetBinding: BottomSheetShareBinding,
        bottomSheetDialog: BottomSheetDialog
    ) {
        bottomSheetBinding.closeBtn.setOnClickListener {
            categoryAdapter.restoreLastSubmittedSelection()
            bottomSheetDialog.dismiss()
        }

        bottomSheetBinding.recyclerCate.layoutManager = LinearLayoutManager(requireContext())
        bottomSheetBinding.recyclerCate.adapter = categoryAdapter

        bottomSheetBinding.submitBtn.setOnClickListener {
            handleCategorySubmit(bottomSheetDialog)
        }
    }

    private fun handleCategorySubmit(bottomSheetDialog: BottomSheetDialog) {
        val selectedCategories = categoryAdapter.getSelectedItems()

        menuCode = when {
            selectedCategories.contains(0) || selectedCategories.containsAll(listOf(1, 2)) -> ""
            selectedCategories.contains(1) -> "AR_MENU02"
            selectedCategories.contains(2) -> "AR_MENU05"
            else -> ""
        }

        updateSelectedCategories(selectedCategories)
        resetData()
        fetchData()
        bottomSheetDialog.dismiss()
    }

    private fun showGridStyle(spanCount: Int, spacing: Int) {
        shareViewModel.clearShareListData()
        clearItemDecorations(binding.recyclerShare)

        binding.recyclerShare.layoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.recyclerShare.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                CommonFunc.dpToPx(spacing, requireContext()),
                false
            )
        )

        adapter = ShareGridItemAdapter(requireContext(), data, spanCount, this)
        binding.recyclerShare.adapter = adapter
        setupRecyclerViewScrollListener()

        fetchData()
    }

    private fun setupRecyclerViewScrollListener() {
        binding.recyclerShare.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!binding.recyclerShare.canScrollVertically(1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                    && !isLoading
                    && hasMoreData
                ) {
                    fetchData()
                }
            }
        })
    }

    private fun fetchData() {
        if (isLoading) return
        isLoading = true

        val requestDto = RequestShareContentAllOpenListDto().apply {
            currPage = currentPage
            pageSize = 18
            if (menuCode.isNotEmpty()) menu_code = menuCode
        }

        shareViewModel.getShareAllList(requestDto)
    }

    private fun observeShareList() {
        shareViewModel.shareAllContentListResponseLiveData()
            .observe(viewLifecycleOwner) { result ->
                isLoading = false

                if (result == null) return@observe

                val newData = result.data?.root ?: ArrayList()

                if (newData.isEmpty()) {
                    hasMoreData = false
                    return@observe
                }

                // 데이터 추가 및 페이지 증가
                adapter.addMoreData(newData)
                currentPage++
            }
    }

    private fun clearItemDecorations(recyclerView: RecyclerView) {
        for (i in recyclerView.itemDecorationCount - 1 downTo 0) {
            recyclerView.removeItemDecorationAt(i)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun resetData() {
        data.clear()
        adapter.notifyDataSetChanged()
        currentPage = 1
        hasMoreData = true
    }

    override fun onItemClick(position: Int, cntntsNo: String) {
        val intent = Intent(requireContext(), ShareDetailActivity::class.java)
        intent.putExtra("CONTENT_DETAIL_NO", cntntsNo)
        startActivity(intent)
    }

    override fun onItemSelected(position: Int, isSelected: Boolean) {
        Log.e("ShareFragment", "Selected position: $position, isSelected: $isSelected")
    }

    private fun updateSelectedCategories(selectedCategories: List<Int>) {
        binding.contentTypeView.text = itemList[selectedCategories[0]]
        binding.selectCnt.apply {
            visibility = if (selectedCategories.size > 1) View.VISIBLE else View.GONE
            text = selectedCategories.size.toString()
        }
    }
}
