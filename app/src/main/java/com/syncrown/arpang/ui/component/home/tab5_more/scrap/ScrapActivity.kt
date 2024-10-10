package com.syncrown.arpang.ui.component.home.tab5_more.scrap

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityScrapMainBinding
import com.syncrown.arpang.databinding.BottomSheetShareBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import com.syncrown.arpang.ui.component.home.tab3_share.main.adapter.GridItem
import com.syncrown.arpang.ui.component.home.tab3_share.main.adapter.ShareGridItemAdapter
import com.syncrown.arpang.ui.component.home.tab3_share.main.adapter.ShareMultiSelectAdapter

class ScrapActivity : BaseActivity(), ShareGridItemAdapter.OnItemClickListener,
    ShareMultiSelectAdapter.OnItemSelectedListener {
    private lateinit var binding: ActivityScrapMainBinding

    private val itemList = listOf("전체", "인생 두컷", "자유 인쇄")
    private lateinit var categoryAdapter: ShareMultiSelectAdapter

    companion object {
        private const val GRID_SPAN_COUNT = 3
        private const val LINEAR_SPAN_COUNT = 1
        private const val SPACE = 3
    }

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityScrapMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = "스크랩"

        setMenuView()

        setScrapList()
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

        binding.closeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        binding.recyclerCate.layoutManager = LinearLayoutManager(this)
        binding.recyclerCate.adapter = categoryAdapter

        binding.submitBtn.setOnClickListener {
            // 적용
            val selectedCategories = categoryAdapter.getSelectedItems()
            Log.e("jung", "size : " + selectedCategories.size)
            updateSelectedCategories(selectedCategories)

            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
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

    private fun setScrapList() {
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

        clearItemDecorations(binding.recyclerScrap)

        binding.recyclerScrap.layoutManager = GridLayoutManager(this, spanCount)
        binding.recyclerScrap.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                CommonFunc.dpToPx(spacing, this),
                false
            )
        )

        binding.recyclerScrap.adapter = ShareGridItemAdapter(items, spanCount, this)
    }

    private fun clearItemDecorations(recyclerView: RecyclerView) {
        val decorationCount = recyclerView.itemDecorationCount
        for (i in decorationCount - 1 downTo 0) {
            recyclerView.removeItemDecorationAt(i)
        }
    }

    override fun onItemClick(position: Int) {
        //리사이클러뷰 아이템 클릭시

    }

    override fun onItemSelected(position: Int, isSelected: Boolean) {
        //메뉴에서 전체,인생두컷,자유인쇄 선택

    }
}