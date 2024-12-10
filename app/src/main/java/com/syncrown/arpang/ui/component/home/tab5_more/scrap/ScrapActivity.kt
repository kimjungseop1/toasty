package com.syncrown.arpang.ui.component.home.tab5_more.scrap

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityScrapMainBinding
import com.syncrown.arpang.databinding.BottomSheetShareBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestScrapContentDto
import com.syncrown.arpang.network.model.ResponseScrapContentDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import com.syncrown.arpang.ui.component.home.tab3_share.detail.ShareDetailActivity
import com.syncrown.arpang.ui.component.home.tab3_share.main.adapter.ShareMultiSelectAdapter
import com.syncrown.arpang.ui.component.home.tab5_more.scrap.adapter.ScrapGridItemAdapter
import kotlinx.coroutines.launch

class ScrapActivity : BaseActivity(), ScrapGridItemAdapter.OnItemClickListener,
    ShareMultiSelectAdapter.OnItemSelectedListener {
    private lateinit var binding: ActivityScrapMainBinding
    private val scrapViewModel: ScrapViewModel by viewModels()

    private val itemList = listOf("전체", "인생 두컷", "자유 인쇄")
    private lateinit var categoryAdapter: ShareMultiSelectAdapter

    private var curPage = 1
    private var curPageSize = 18
    private var menuCode = "AR_MENU02,AR_MENU05"
    private var currentSelectMode = -1

    private lateinit var adapter: ScrapGridItemAdapter
    private val data = ArrayList<ResponseScrapContentDto.Root>()

    companion object {
        private const val GRID_SPAN_COUNT = 3
        private const val LINEAR_SPAN_COUNT = 1
        private const val SPACE = 3
    }

    override fun observeViewModel() {
        lifecycleScope.launch {
            scrapViewModel.scrapContentListResponseLiveData().observe(this@ScrapActivity) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val data = result.data?.root ?: ArrayList()
                        if (curPage > 1 && data.isEmpty()) {
                            curPage = 1
                        } else {
                            adapter.addMoreData(data)
                            curPage++
                        }
                    }

                    is NetworkResult.NetCode -> {
                        Log.e("jung", "실패 : ${result.message}")
                        if (result.message.equals("403")) {
                            goLogin()
                        }
                    }

                    is NetworkResult.Error -> {
                        Log.e("jung", "오류 : ${result.message}")
                    }
                }
            }
        }
    }

    override fun initViewBinding() {
        binding = ActivityScrapMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoryAdapter = ShareMultiSelectAdapter(this, itemList, this)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.scrap_title)

        setMenuView()

        setupUI()
    }

    private fun fetchData() {
        val requestScrapContentDto = RequestScrapContentDto()
        requestScrapContentDto.user_id = AppDataPref.userId
        requestScrapContentDto.currPage = curPage
        requestScrapContentDto.pageSize = curPageSize
        if (menuCode.isNotEmpty()) {
            requestScrapContentDto.menu_code = menuCode
        }

        scrapViewModel.scrapContentList(requestScrapContentDto)
    }

    private fun setMenuView() {
        categoryAdapter = ShareMultiSelectAdapter(this, itemList, this)
        binding.contentTypeView.text = itemList[0]
        binding.contentTypeView.setOnClickListener {
            //유형팝업
            showTypeBottomSheet()
        }
    }

    private fun showTypeBottomSheet() {
        val binding = BottomSheetShareBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        bottomSheetDialog.window?.setDimAmount(0.7f)
        bottomSheetDialog.setContentView(binding.root)

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.isDraggable = false
        behavior.isHideable = false

        binding.closeBtn.setOnClickListener {
            categoryAdapter.restoreLastSubmittedSelection()
            bottomSheetDialog.dismiss()
        }

        binding.recyclerCate.layoutManager = LinearLayoutManager(this)
        binding.recyclerCate.adapter = categoryAdapter

        binding.submitBtn.setOnClickListener {
            // 적용
            handleCategorySubmit(bottomSheetDialog)
        }

        bottomSheetDialog.show()
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

        updateSelectedCategories(selectedCategories)
        resetData()
        fetchData()
        bottomSheetDialog.dismiss()
    }

    private fun updateSelectedCategories(selectedCategories: List<Int>) {
        binding.contentTypeView.text = itemList[selectedCategories[0]] //selectedCategories[0].toString()

        if (categoryAdapter.getSelectedItemCount() > 1) {
            binding.selectCnt.visibility = View.VISIBLE
        } else {
            binding.selectCnt.visibility = View.GONE
        }
        binding.selectCnt.text = categoryAdapter.getSelectedItemCount().toString()
    }

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

    private fun showGridStyle(spanCount: Int, spacing: Int) {
        clearItemDecorations(binding.recyclerScrap)

        binding.recyclerScrap.layoutManager = GridLayoutManager(this, spanCount)
        binding.recyclerScrap.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                CommonFunc.dpToPx(spacing, this),
                false
            )
        )

        adapter = ScrapGridItemAdapter(this, data, spanCount, this)
        binding.recyclerScrap.adapter = adapter
        setupRecyclerViewScrollListener()

        fetchData()
    }

    private fun setupRecyclerViewScrollListener() {
        binding.recyclerScrap.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!binding.recyclerScrap.canScrollVertically(1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fetchData()
                }
            }
        })
    }

    private fun clearItemDecorations(recyclerView: RecyclerView) {
        val decorationCount = recyclerView.itemDecorationCount
        for (i in decorationCount - 1 downTo 0) {
            recyclerView.removeItemDecorationAt(i)
        }
    }

    override fun onItemClick(position: Int, cntntsNo: String) {
        //리사이클러뷰 아이템 클릭시
        goDetail(cntntsNo)
    }

    override fun onItemSelected(position: Int, isSelected: Boolean) {
        //메뉴에서 전체,인생두컷,자유인쇄 선택

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun resetData() {
        data.clear()
        adapter.notifyDataSetChanged()
        curPage = 1
    }

    private fun goDetail(cntntsNo: String) {
        val intent = Intent(this, ShareDetailActivity::class.java)
        intent.putExtra("CONTENT_DETAIL_NO", cntntsNo)
        startActivity(intent)
    }
}