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
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.BottomSheetShareBinding
import com.syncrown.arpang.databinding.FragmentShareBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestShareContentAllOpenListDto
import com.syncrown.arpang.network.model.ResponseShareContentAllOpenListDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import com.syncrown.arpang.ui.component.home.MainViewModel
import com.syncrown.arpang.ui.component.home.tab3_share.detail.ShareDetailActivity
import com.syncrown.arpang.ui.component.home.tab3_share.main.adapter.ShareGridItemAdapter
import com.syncrown.arpang.ui.component.home.tab3_share.main.adapter.ShareMultiSelectAdapter
import kotlinx.coroutines.launch

class ShareFragment : Fragment(),
    ShareGridItemAdapter.OnItemClickListener,
    ShareMultiSelectAdapter.OnItemSelectedListener {

    // View Binding
    private lateinit var binding: FragmentShareBinding

    // ViewModel
    private val shareViewModel: MainViewModel by activityViewModels()

    // Variables
    private var currentPage = 1
    private var menuCode = "AR_MENU02,AR_MENU05"
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

        setEventView()

        categoryAdapter = ShareMultiSelectAdapter(requireContext(), itemList, this)
        setupUI()

        observeShareList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        shareViewModel.clearShareListData()
    }

    private fun setEventView() {
        // 이벤트 이미지
        Glide.with(requireContext())
            .load(R.drawable.sample_img_1)
            .circleCrop()
            .into(binding.bannerImg)

        // 이벤트 제목
        binding.bannerTitle.text = ""

        // 이벤트 내용
        binding.bannerSubTitle.text = ""

        // 이벤트 페이지 이동
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
            selectedCategories.containsAll(listOf(1, 2)) -> "AR_MENU02,AR_MENU05"
            selectedCategories.contains(0) -> "AR_MENU02,AR_MENU05"
            selectedCategories.contains(1) -> "AR_MENU02"
            selectedCategories.contains(2) -> "AR_MENU05"
            else -> "AR_MENU02,AR_MENU05"
        }

        Log.e("jung", "menuCode : $menuCode,  selectedCategories : $selectedCategories")

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
                ) {
                    fetchData()
                }
            }
        })
    }

    private fun fetchData() {
        val requestDto = RequestShareContentAllOpenListDto().apply {
            currPage = currentPage
            pageSize = 18
            if (menuCode.isNotEmpty()) menu_code = menuCode
        }

        shareViewModel.getShareAllList(requestDto)
    }

    private fun observeShareList() {
        lifecycleScope.launch {
            shareViewModel.shareAllContentListResponseLiveData().observe(viewLifecycleOwner) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val data = result.data?.root ?: ArrayList()
                        if (currentPage > 1 && data.isEmpty()) {
                            currentPage = 1
                        } else {
                            adapter.addMoreData(data)
                            currentPage++
                        }
                    }

                    is NetworkResult.NetCode -> {
                        Log.e("jung", "실패 : ${result.message}")
                        if (result.message.equals("403")) {
                            (activity as? BaseActivity)?.goLogin()
                        }
                    }

                    is NetworkResult.Error -> {
                        Log.e("jung", "오류 : ${result.message}")
                    }
                }
            }
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
