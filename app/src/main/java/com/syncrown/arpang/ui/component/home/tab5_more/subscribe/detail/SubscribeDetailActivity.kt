package com.syncrown.arpang.ui.component.home.tab5_more.subscribe.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivitySubscribeDetailBinding
import com.syncrown.arpang.databinding.BottomSheetShareBinding
import com.syncrown.arpang.databinding.PopupSubscribeActionDetailBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.CustomToast
import com.syncrown.arpang.ui.commons.CustomToastType
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import com.syncrown.arpang.ui.component.home.tab3_share.main.adapter.GridItem
import com.syncrown.arpang.ui.component.home.tab3_share.main.adapter.ShareGridItemAdapter
import com.syncrown.arpang.ui.component.home.tab3_share.main.adapter.ShareMultiSelectAdapter

class SubscribeDetailActivity : BaseActivity(), ShareMultiSelectAdapter.OnItemSelectedListener,
    ShareGridItemAdapter.OnItemClickListener {
    private lateinit var binding: ActivitySubscribeDetailBinding
    private val itemList = listOf("전체", "인생 두컷", "자유 인쇄")
    private lateinit var categoryAdapter: ShareMultiSelectAdapter

    companion object {
        private const val GRID_SPAN_COUNT = 3
        private const val LINEAR_SPAN_COUNT = 1
        private const val SPACE = 3
    }

    var isLike = false

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivitySubscribeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }
        binding.actionbar.actionTitle.text = "이름.."
        binding.actionbar.actionEtc1.text = "구독"
        binding.actionbar.actionEtc1.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this,
                R.drawable.icon_like_unsel
            ), null, null, null
        )
        binding.actionbar.actionEtc1.setOnClickListener {
            isLike = !isLike

            val icon = if (isLike) {
                R.drawable.icon_like_sel
            } else {
                R.drawable.icon_like_unsel
            }

            binding.actionbar.actionEtc1.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(
                    this,
                    icon
                ), null, null, null
            )
        }

        binding.actionbar.actionMore.setOnClickListener {
            showActionPopupWindow(binding.actionbar.actionMore)
        }

        setMenuView()

    }

    private fun setMenuView() {
        categoryAdapter = ShareMultiSelectAdapter(this, itemList, this)
        binding.contentTypeView.text = itemList[0]
        binding.contentTypeView.setOnClickListener {
            showTypeBottomSheet()
        }

        showGridStyle(GRID_SPAN_COUNT, SPACE)
        binding.gridBtn.isSelected = true
        binding.linearBtn.isSelected = false

        binding.gridBtn.setOnClickListener {
            binding.gridBtn.isSelected = true
            binding.linearBtn.isSelected = false
            showGridStyle(
                GRID_SPAN_COUNT,
                SPACE
            )
        }

        binding.linearBtn.setOnClickListener {
            binding.gridBtn.isSelected = false
            binding.linearBtn.isSelected = true
            showGridStyle(
                LINEAR_SPAN_COUNT,
                SPACE
            )
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

        clearItemDecorations(binding.recyclerShare)

        binding.recyclerShare.layoutManager = GridLayoutManager(this, spanCount)
        binding.recyclerShare.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                CommonFunc.dpToPx(spacing, this),
                false
            )
        )

        binding.recyclerShare.adapter = ShareGridItemAdapter(items, spanCount, this)
    }

    private fun clearItemDecorations(recyclerView: RecyclerView) {
        val decorationCount = recyclerView.itemDecorationCount
        for (i in decorationCount - 1 downTo 0) {
            recyclerView.removeItemDecorationAt(i)
        }
    }

    private fun updateSelectedCategories(selectedCategories: List<Int>) {
        binding.contentTypeView.text = itemList[selectedCategories[0]]

        if (categoryAdapter.getSelectedItemCount() > 1) {
            binding.selectCnt.visibility = View.VISIBLE
        } else {
            binding.selectCnt.visibility = View.GONE
        }
        binding.selectCnt.text = categoryAdapter.getSelectedItemCount().toString()
    }

    private fun showActionPopupWindow(anchor: View) {
        val popBinding = PopupSubscribeActionDetailBinding.inflate(LayoutInflater.from(this))

        val popupWindow = PopupWindow(
            popBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true
        )

        popBinding.menuItem1.text = "신고하기"
        popBinding.menuItem1.setOnClickListener {
            val dialogCommon = DialogCommon()
            dialogCommon.showSubscribeDetailReport(supportFragmentManager, {
                //닫기
            }, {
                //신고
                val customToast = CustomToast(this)
                customToast.showToast("사용자를 신고하였습니다.", CustomToastType.BLUE)
            })
            popupWindow.dismiss()
        }

        popBinding.menuItem2.text = "차단하기"
        popBinding.menuItem2.setOnClickListener {
            val dialogCommon = DialogCommon()
            dialogCommon.showSubscribeDetailReject(supportFragmentManager, {
                //닫기
            }, {
                //차단
                val customToast = CustomToast(this)
                customToast.showToast("사용자를 차단하였습니다.", CustomToastType.BLUE)
            })
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(anchor, 0, 0)
    }

    override fun onItemSelected(position: Int, isSelected: Boolean) {
        Log.e("jung", "pos : $position, isSelected : $isSelected")
    }

    override fun onItemClick(position: Int) {
        //goDetail()
    }

    private fun goDetail() {

    }
}