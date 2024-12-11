package com.syncrown.arpang.ui.component.home.tab5_more.subscribe.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivitySubscribeDetailBinding
import com.syncrown.arpang.databinding.BottomSheetShareBinding
import com.syncrown.arpang.databinding.PopupSubscribeActionDetailBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestSubscribeUpdateDto
import com.syncrown.arpang.network.model.RequestSubscribeUserContentListDto
import com.syncrown.arpang.network.model.RequestTargetUserBlockDto
import com.syncrown.arpang.network.model.RequestTargetUserReportDto
import com.syncrown.arpang.network.model.ResponseSubscribeUserContentListDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.CustomToast
import com.syncrown.arpang.ui.commons.CustomToastType
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import com.syncrown.arpang.ui.component.home.tab3_share.detail.ShareDetailActivity
import com.syncrown.arpang.ui.component.home.tab3_share.main.adapter.ShareMultiSelectAdapter
import com.syncrown.arpang.ui.component.home.tab5_more.subscribe.adapter.SubscribeUserGridItemAdapter
import kotlinx.coroutines.launch

class SubscribeDetailActivity : BaseActivity(),
    SubscribeUserGridItemAdapter.OnItemClickListener {
    private lateinit var binding: ActivitySubscribeDetailBinding
    private val subscribeDetailViewModel: SubscribeDetailViewModel by viewModels()

    private var subUserId = ""

    private val itemList = listOf("전체", "인생 두컷", "자유 인쇄")
    private lateinit var categoryAdapter: ShareMultiSelectAdapter

    private lateinit var adapter: SubscribeUserGridItemAdapter
    private var data: ArrayList<ResponseSubscribeUserContentListDto.Root> = ArrayList()
    private var menuCode = "AR_MENU02,AR_MENU05"
    private var curPage = 1
    private var curPageSize = 18
    private var currentSelectMode = -1

    companion object {
        private const val GRID_SPAN_COUNT = 3
        private const val LINEAR_SPAN_COUNT = 1
        private const val SPACE = 3
    }

    private var isLike = -1

    override fun observeViewModel() {
        lifecycleScope.launch {
            subscribeDetailViewModel.subscribeUpdateResponseLiveData()
                .observe(this@SubscribeDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data
                            if (data?.msgCode.equals("SUCCESS")) {

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

        lifecycleScope.launch {
            subscribeDetailViewModel.subscribeUserContentListResponseLiveData()
                .observe(this@SubscribeDetailActivity) { result ->
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

        lifecycleScope.launch {
            subscribeDetailViewModel.targetUserReportResponseLiveData()
                .observe(this@SubscribeDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.msgCode
                            if (data.equals("SUCCESS")) {
                                val customToast = CustomToast()
                                customToast.showToastMessage(
                                    supportFragmentManager,
                                    getString(R.string.subscribe_toast_desc_1),
                                    CustomToastType.BLUE
                                ) {
                                    //close
                                }
                            } else {
                                Log.e("jung", "신고 실패")
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

        lifecycleScope.launch {
            subscribeDetailViewModel.targetUserBlockResponseLiveData()
                .observe(this@SubscribeDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.msgCode
                            if (data.equals("SUCCESS")) {
                                val customToast = CustomToast()
                                customToast.showToastMessage(
                                    supportFragmentManager,
                                    getString(R.string.subscribe_toast_desc_2),
                                    CustomToastType.BLUE
                                ) {
                                    //close
                                }

                                setResult(RESULT_OK)
                                finish()
                            } else {
                                Log.e("jung", "신고 실패")
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
        binding = ActivitySubscribeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        val titleName = intent.getStringExtra("SUB_USER_NAME")
        binding.actionbar.actionTitle.text = titleName
        binding.actionbar.actionEtc1.text = getString(R.string.subscribe_title)

        subscribeStatusUi()

        binding.actionbar.actionEtc1.setOnClickListener {
            val icon = if (isLike == 1) {
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

            val subscriptionSe: Int = if (isLike == 1) {
                0 //해제
            } else {
                1 //구독
            }

            setSubscribeUpdate(subscriptionSe)
        }

        binding.actionbar.actionMore.setOnClickListener {
            showActionPopupWindow(binding.actionbar.actionMore)
        }

        subUserId = intent.getStringExtra("SUB_USER_ID").toString()

        fetchData()

        setContentList()
    }

    private fun subscribeStatusUi() {
        isLike = intent.getIntExtra("SUB_USER_SUBSCRIBED", -1)
        if (isLike == 1) {
            binding.actionbar.actionEtc1.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.icon_like_sel
                ), null, null, null
            )
        } else {
            binding.actionbar.actionEtc1.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.icon_like_unsel
                ), null, null, null
            )
        }
    }

    private fun setSubscribeUpdate(subscription_se: Int) {
        val requestSubscribeUpdateDto = RequestSubscribeUpdateDto()
        requestSubscribeUpdateDto.user_id = AppDataPref.userId
        requestSubscribeUpdateDto.sub_user_id = subUserId
        requestSubscribeUpdateDto.subscription_se = subscription_se

        subscribeDetailViewModel.subscribeUpdate(requestSubscribeUpdateDto)
    }

    private fun setContentList() {
        categoryAdapter = ShareMultiSelectAdapter(this, itemList, null)
        binding.contentTypeView.text = itemList[0]
        binding.contentTypeView.setOnClickListener {
            showTypeBottomSheet()
        }

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

        bottomSheetDialog.show()
    }

    private fun showGridStyle(spanCount: Int, spacing: Int) {
        clearItemDecorations(binding.recyclerShare)

        binding.recyclerShare.layoutManager = GridLayoutManager(this, spanCount)
        binding.recyclerShare.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                CommonFunc.dpToPx(spacing, this),
                false
            )
        )

        adapter = SubscribeUserGridItemAdapter(this, data, spanCount, this)
        binding.recyclerShare.adapter = adapter
        setupRecyclerViewScrollListener()


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
        val requestDto = RequestSubscribeUserContentListDto().apply {
            if (subUserId.isNotEmpty()) sub_user_id = subUserId
            currPage = curPage
            pageSize = curPageSize
            if (menuCode.isNotEmpty()) menu_code = menuCode
        }

        subscribeDetailViewModel.getSubscribeUserContentList(requestDto)
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

        popBinding.menuItem1.text = getString(R.string.lib_popup_report)
        popBinding.menuItem1.setOnClickListener {
            val dialogCommon = DialogCommon()
            dialogCommon.showSubscribeDetailReport(supportFragmentManager, {
                //닫기
            }, {
                //신고
                moreSetUserReport()
            })
            popupWindow.dismiss()
        }

        popBinding.menuItem2.text = getString(R.string.lib_popup_reject)
        popBinding.menuItem2.setOnClickListener {
            val dialogCommon = DialogCommon()
            dialogCommon.showSubscribeDetailReject(supportFragmentManager, {
                //닫기
            }, {
                //차단
                moreSetUserBlock()
            })
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(anchor, 0, 0)
    }

    private fun moreSetUserReport() {
        val requestTargetUserReportDto = RequestTargetUserReportDto().apply {
            user_id = AppDataPref.userId
            complain_user_id = subUserId
        }

        subscribeDetailViewModel.userReport(requestTargetUserReportDto)
    }

    private fun moreSetUserBlock() {
        val requestTargetUserBlockDto = RequestTargetUserBlockDto().apply {
            user_id = AppDataPref.userId
            block_user_id = subUserId
            block_se = 1
        }

        subscribeDetailViewModel.userBlock(requestTargetUserBlockDto)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun resetData() {
        data.clear()
        adapter.notifyDataSetChanged()
        curPage = 1
    }

    override fun onItemClick(position: Int, cntntsNo: String) {
        Log.e("jung","position : $position,  cntntsNo : $cntntsNo")
        val intent = Intent(this, ShareDetailActivity::class.java)
        intent.putExtra("CONTENT_DETAIL_NO", cntntsNo)
        startActivity(intent)
    }

}