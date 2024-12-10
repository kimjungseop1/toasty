package com.syncrown.arpang.ui.component.home.tab2_Lib.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityLibDetailBinding
import com.syncrown.arpang.databinding.BottomSheetAnotherPaperBinding
import com.syncrown.arpang.databinding.BottomSheetCartridgeBinding
import com.syncrown.arpang.databinding.BottomSheetPrinterDisconnectBinding
import com.syncrown.arpang.databinding.PopupLibDeleteBinding
import com.syncrown.arpang.databinding.PopupLibReportBinding
import com.syncrown.arpang.databinding.PopupMenuDetailBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestAddCommentDto
import com.syncrown.arpang.network.model.RequestCommentListDto
import com.syncrown.arpang.network.model.RequestCommentReportDto
import com.syncrown.arpang.network.model.RequestDelCommentDto
import com.syncrown.arpang.network.model.RequestDeleteStorageDto
import com.syncrown.arpang.network.model.RequestDetailContentHashTagDto
import com.syncrown.arpang.network.model.RequestEditContentHashTagDto
import com.syncrown.arpang.network.model.RequestPublicContentSettingDto
import com.syncrown.arpang.network.model.RequestStorageDetailDto
import com.syncrown.arpang.network.model.ResponseCommentListDto
import com.syncrown.arpang.network.model.ResponseDetailContentHashTagDto
import com.syncrown.arpang.network.model.ResponseStorageDetailDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CustomDynamicTagView
import com.syncrown.arpang.ui.commons.CustomToast
import com.syncrown.arpang.ui.commons.CustomToastType
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.commons.DialogToastingCommon
import com.syncrown.arpang.ui.component.home.input_tag.InputTagActivity
import com.syncrown.arpang.ui.component.home.input_tag.TagResultListStorage
import com.syncrown.arpang.ui.component.home.tab3_share.detail.adapter.DetailCommentListAdapter
import kotlinx.coroutines.launch

class LibDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityLibDetailBinding
    private val libDetailViewModel: LibDetailViewModel by viewModels()

    private lateinit var data: ArrayList<ResponseCommentListDto.Root>
    private var cntntsNo = ""
    private var cateName = ""

    private var currentPage = 1
    private var pageSize = 10

    private lateinit var detailCommentListAdapter: DetailCommentListAdapter

    override fun observeViewModel() {
        lifecycleScope.launch {
            //TODO 상세페이지
            libDetailViewModel.libContentDetailResponseLiveData()
                .observe(this@LibDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.root
                            setupUI(data)

                            setCommentList(data?.cntnts_no.toString())
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
            //TODO 공개 비공개 전환
            libDetailViewModel.publicContentSettingLiveData()
                .observe(this@LibDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data

                            if (data?.msgCode.equals("SUCCESS")) {
                                getDetailContent()
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
            //TODO 상세 해시태그 리스트
            libDetailViewModel.hashTagListResponseLiveData()
                .observe(this@LibDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.root ?: ArrayList()

                            setHashTagListView(data)
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
            //TODO 해시태그 편집
            libDetailViewModel.editHashTagResponseLiveData().observe(this@LibDetailActivity) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val data = result.data?.msgCode

                        if (data.equals("SUCCESS")) {
                            getHashTagList()
                        } else if (data.equals("FAIL")) {
                            Log.e("jung","실패")
                        } else if (data.equals("PROHIBITION_TAG")) {
                            Log.e("jung","금지 태그 있음")
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
            //TODO 코멘트 리스트 옵져브
            libDetailViewModel.commentListResponseLiveData()
                .observe(this@LibDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.root ?: ArrayList()

                            if (currentPage > 1 && data.isEmpty()) {
                                currentPage = 1
                            } else {
                                detailCommentListAdapter.updateData(data)
                                currentPage++

                                //댓글전체수 카운트
                                binding.commentView.text = data[0].comment_cnt.toString()
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
            //TODO 코멘트 추가 옵져브
            libDetailViewModel.addCommentResponseLiveData()
                .observe(this@LibDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.msgCode
                            if (data.equals("SUCCESS")) {
                                binding.inputComment.text?.clear()
                                hideKeyBoard()

                                currentPage = 1
                                setCommentList(cntntsNo)
                            } else {
                                Log.e("jung", "댓글 등록에 실패하였습니다.")
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
            //TODO 코멘트 삭제 옵져브
            libDetailViewModel.deleteCommentResponseLiveData()
                .observe(this@LibDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.msgCode
                            if (data.equals("SUCCESS")) {
                                binding.inputComment.text?.clear()
                                hideKeyBoard()

                                currentPage = 1
                                setCommentList(cntntsNo)
                            } else {
                                Log.e("jung", "댓글 삭제에 실패하였습니다.")
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
            //TODO 보관함 컨텐츠 삭제
            libDetailViewModel.deleteLibContentResponseLiveData().observe(this@LibDetailActivity) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val data = result.data?.msgCode
                        if (data.equals("SUCCESS")) {
                            setResult(RESULT_OK)
                            finish()
                        } else {
                            Log.e("jung", "댓글 삭제에 실패하였습니다.")
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
            //TODO 댓글 신고
            libDetailViewModel.reportCommentResponseLiveData().observe(this@LibDetailActivity) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val data = result.data?.msgCode
                        if (data.equals("SUCCESS")) {
                            val customToast = CustomToast()
                            customToast.showToastMessage(
                                supportFragmentManager,
                                getString(R.string.lib_popup_report_comment),
                                CustomToastType.WHITE
                            ) {
                                //close
                            }
                        } else {
                            Log.e("jung", "신고에 실패하였습니다.")
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
        binding = ActivityLibDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cntntsNo = intent.getStringExtra("CONTENT_DETAIL_NO").toString()

        getDetailContent()

        data = ArrayList()
        showCommentView()

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.home_nav_title_2)

        binding.actionbar.actionEtc1.text = getString(R.string.actionbar_print_text)
        binding.actionbar.actionEtc1.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this,
                R.drawable.icon_action_print
            ), null, null, null
        )
        binding.actionbar.actionEtc1.setOnClickListener {
            //TODO 프린터 연결 + 용지 장착
            setPrinterAndPaper()

            //TODO 프린터 연결 + 용지 불일치
            setPrinterAndNotPaper()

            //TODO 프린터 미연동
            setDisconnectPrinter()

            //TODO 프린터 연결 + 용지 미장착
            setPrinterAndAnotherPaper()
        }

        //상세 페이지 태그 리스트 호출
        getHashTagList()
    }

    private fun getHashTagList() {
        val requestDetailContentHashTagDto = RequestDetailContentHashTagDto()
        requestDetailContentHashTagDto.cntnts_no = cntntsNo

        libDetailViewModel.getHashTagList(requestDetailContentHashTagDto)
    }

    @SuppressLint("SetTextI18n")
    private fun setHashTagListView(data: ArrayList<ResponseDetailContentHashTagDto.Root>) {
        TagResultListStorage.detailTagList = data

        for (i in 0 until data.size) {
            val customDynamicTagView = CustomDynamicTagView(this).apply {
                text = "# " + data[i].hashtag_nm
                tag = i
            }

            val layoutParams = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(5, 5, 5, 5)
            }

            customDynamicTagView.layoutParams = layoutParams
            binding.flexTagView.addView(customDynamicTagView)
        }
    }

    private fun getDetailContent() {
        val requestStorageDetailDto = RequestStorageDetailDto()
        requestStorageDetailDto.cntnts_no = cntntsNo
        requestStorageDetailDto.user_id = AppDataPref.userId

        libDetailViewModel.libContentDetail(requestStorageDetailDto)
    }

    private fun setupUI(data: ResponseStorageDetailDto.Root?) {
        binding.actionbar.actionMore.setOnClickListener {
            showPopupWindow(binding.actionbar.actionMore, data?.share_se)
        }

        //공개글 여부
        if (data?.share_se.equals("0")) {
            binding.desc1.text = getString(R.string.storage_detail_close_view)
        } else {
            binding.desc1.text = getString(R.string.storage_detail_open_view)
        }

        //용지이름
        binding.paperTypeView.text = data?.ctge_nm

        //작성 카테고리
        cateName = data?.menu_code.toString()
        binding.cateTypeView.text = cateName

        //작성자
        if (data?.nick_nm == null) {
            binding.nameView.text = AppDataPref.userId
        } else {
            binding.nameView.text = data.nick_nm
        }

        //작성일
        binding.dateView.text = data?.save_ds

        //작성 이미지
        Glide.with(this)
            .load(data?.img_url)
            .into(binding.thumbnailView)

        //좋아요 카운드
        binding.likeView.text = data?.favorite_cnt.toString()

        if (data?.share_se.equals("0")) {
            binding.inputComment.isEnabled = false
            binding.inputComment.hint = getString(R.string.storage_detail_input_hint_disable)
        } else {
            binding.inputComment.isEnabled = true
            binding.inputComment.hint = getString(R.string.storage_detail_input_hint)
        }

        showEditView()
    }

    override fun onDestroy() {
        super.onDestroy()
        TagResultListStorage.tagArrayList = null
    }

    private fun showPopupWindow(anchor: View, shareSe: String?) {
        val popBinding = PopupMenuDetailBinding.inflate(LayoutInflater.from(this))

        val popupWindow = PopupWindow(
            popBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true
        )

        popBinding.switchMenu1.isChecked = !shareSe.equals("0")
        popBinding.switchMenu1.setOnCheckedChangeListener { _, isChecked ->
            val requestPublicContentSettingDto = RequestPublicContentSettingDto()
            requestPublicContentSettingDto.cntnts_no = cntntsNo
            requestPublicContentSettingDto.menu_code = cateName
            requestPublicContentSettingDto.user_id = AppDataPref.userId

            if (isChecked) {
                requestPublicContentSettingDto.share_se = "1"
            } else {
                requestPublicContentSettingDto.share_se = "0"
            }

            libDetailViewModel.setPublicContentSetting(requestPublicContentSettingDto)
        }

        popBinding.menuItem2.setOnClickListener {
            popupWindow.dismiss()
            goInputTag()
        }

        popBinding.menuItem3.setOnClickListener {
            popupWindow.dismiss()
        }

        popBinding.menuItem4.setOnClickListener {
            popupWindow.dismiss()
            val dialogCommon = DialogCommon()
            dialogCommon.showCommentDelete(supportFragmentManager, {
                //닫기
            }, {
                //삭제
                setDeleteStorageContent()
            })
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(anchor, 0, 0)
    }

    private fun setDeleteStorageContent() {
        val requestDeleteStorageDto = RequestDeleteStorageDto()
        requestDeleteStorageDto.cntnts_no = cntntsNo
        requestDeleteStorageDto.user_id = AppDataPref.userId

        libDetailViewModel.deleteLibContent(requestDeleteStorageDto)
    }

    private fun setCommentList(cntntsNo: String) {
        val requestCommentListDto = RequestCommentListDto()
        requestCommentListDto.cntnts_no = cntntsNo
        requestCommentListDto.user_id = AppDataPref.userId
        requestCommentListDto.currPage = currentPage
        requestCommentListDto.pageSize = pageSize

        libDetailViewModel.commentList(requestCommentListDto)
    }

    private fun setupRecyclerViewScrollListener() {
        binding.recyclerComment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!binding.recyclerComment.canScrollVertically(1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                ) {
                    Log.e("jung", "addOnScrollListener : $currentPage")
                    setCommentList(cntntsNo)
                }
            }
        })
    }

    private fun showCommentView() {
        binding.recyclerComment.layoutManager = LinearLayoutManager(this)
        detailCommentListAdapter = DetailCommentListAdapter(
            this,
            data,
            object : DetailCommentListAdapter.OnItemClickListener {
                override fun onClick(position: Int, view: View) {
                    if (data[position].write_se == "1") {
                        //본인댓글 삭제하기
                        showPopupDeleteMyComment(view, position, data[position].seq_no)
                    } else {
                        //남의댓글 신고하기
                        showPopupReportOthers(view, position, data[position])
                    }
                }
            })
        binding.recyclerComment.adapter = detailCommentListAdapter

        setupRecyclerViewScrollListener()
    }

    private fun setDeleteComment(seqNo: String) {
        val requestDelCommentDto = RequestDelCommentDto()
        requestDelCommentDto.cntnts_no = cntntsNo
        requestDelCommentDto.user_id = AppDataPref.userId
        requestDelCommentDto.seq_no = seqNo

        libDetailViewModel.deleteComment(requestDelCommentDto)
    }

    private fun showPopupDeleteMyComment(view: View, position: Int, seqNo: String?) {
        val popBinding = PopupLibDeleteBinding.inflate(LayoutInflater.from(this))

        val popupWindow = PopupWindow(
            popBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true
        )

        popBinding.menuItem1.setOnClickListener {
            val dialogCommon = DialogCommon()
            dialogCommon.showCommentDelete(supportFragmentManager, {
                //닫기
            }, {
                //삭제
                setDeleteComment(seqNo.toString())
            })
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(view, 0, 0)
    }

    private fun showPopupReportOthers(view: View, position: Int, root: ResponseCommentListDto.Root) {
        val popBinding = PopupLibReportBinding.inflate(LayoutInflater.from(this))

        val popupWindow = PopupWindow(
            popBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true
        )

        popBinding.menuItem1.setOnClickListener {
            val dialogCommon = DialogCommon()
            dialogCommon.showCommentReport(supportFragmentManager, {
                //닫기
            }, {
                //신고
                setCommentReport(root)
            })
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(view, 0, 0)
    }

    private fun setCommentReport(root: ResponseCommentListDto.Root) {
        val requestCommentReportDto =  RequestCommentReportDto()
        requestCommentReportDto.cntnts_no = cntntsNo
        requestCommentReportDto.comment_seq_no = root.seq_no.toString()
        requestCommentReportDto.user_id = AppDataPref.userId
        requestCommentReportDto.write_user_id = root.write_user_id.toString()

        libDetailViewModel.reportComment(requestCommentReportDto)
    }

    private fun showEditView() {
        binding.root.isKeyboardVisible { isVisible ->
            if (isVisible) {
                val newHeight = resources.getDimensionPixelSize(R.dimen.new_edit_text_height)
                binding.inputComment.layoutParams.height = newHeight
                binding.inputComment.requestLayout()

                // 전송 버튼 보이기
                binding.sendBtn.visibility = View.VISIBLE
            } else {
                val originalHeight =
                    resources.getDimensionPixelSize(R.dimen.original_edit_text_height)
                binding.inputComment.layoutParams.height = originalHeight
                binding.inputComment.requestLayout()

                // 전송 버튼 숨기기
                binding.sendBtn.visibility = View.GONE
            }
        }

        binding.sendBtn.setOnClickListener {
            // 댓글 쓰기
            if (binding.inputComment.text?.isNotEmpty() == true) {
                setAddComment()
            }
        }
    }

    private fun setAddComment() {
        val requestAddCommentDto = RequestAddCommentDto()
        requestAddCommentDto.cntnts_no = cntntsNo
        requestAddCommentDto.user_id = AppDataPref.userId
        requestAddCommentDto.comment = binding.inputComment.text.toString()

        libDetailViewModel.addComment(requestAddCommentDto)
    }

    private fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let { view ->
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun View.isKeyboardVisible(listener: (Boolean) -> Unit) {
        val rootView = this.rootView
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            private var isKeyboardVisible = false

            override fun onGlobalLayout() {
                val rect = Rect()
                rootView.getWindowVisibleDisplayFrame(rect)

                val screenHeight = rootView.height
                val keypadHeight = screenHeight - rect.bottom

                val isVisible = keypadHeight > screenHeight * 0.15

                if (isVisible != isKeyboardVisible) {
                    isKeyboardVisible = isVisible
                    listener(isKeyboardVisible)
                }
            }
        })
    }

    private fun setPrinterAndPaper() {
        val binding = BottomSheetCartridgeBinding.inflate(layoutInflater)
        val bottomSheetDialog =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        bottomSheetDialog.window?.setDimAmount(0.7f)
        bottomSheetDialog.setContentView(binding.root)

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.isDraggable = false
        behavior.isHideable = false

        binding.concentration1.isSelected = true
        binding.printType1.isSelected = true
        binding.onePaper.isSelected = true

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

        binding.printType1.setOnClickListener {
            binding.printType1.isSelected = true
            binding.printType2.isSelected = false
        }

        binding.printType2.setOnClickListener {
            binding.printType1.isSelected = false
            binding.printType2.isSelected = true
        }

        binding.onePaper.setOnClickListener {
            binding.onePaper.isSelected = true
            binding.twoPaper.isSelected = false
        }

        binding.twoPaper.setOnClickListener {
            binding.onePaper.isSelected = false
            binding.twoPaper.isSelected = true
        }

        binding.closeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        binding.submitBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
            showToasting()
        }

        bottomSheetDialog.show()
    }

    private fun setPrinterAndAnotherPaper() {
        val customToast = CustomToast()
        customToast.showToastMessage(
            supportFragmentManager,
            getString(R.string.lib_detail_popup_paper_not),
            CustomToastType.WHITE
        ) {
            //close
        }
//        val binding = BottomSheetPaperDisconnectBinding.inflate(layoutInflater)
//        val bottomSheetDialog =
//            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
//        bottomSheetDialog.window?.setDimAmount(0.7f)
//        bottomSheetDialog.setContentView(binding.root)
//
//        val bottomSheet =
//            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//        val behavior = BottomSheetBehavior.from(bottomSheet!!)
//        behavior.isDraggable = false
//        behavior.isHideable = false
//
//        binding.closeBtn.setOnClickListener {
//            bottomSheetDialog.dismiss()
//        }
//
//        binding.submitBtn.text = getString(R.string.tag_popup_left_btn)
//        binding.submitBtn.setOnClickListener {
//            bottomSheetDialog.dismiss()
//        }
//
//        bottomSheetDialog.show()
    }

    private fun setDisconnectPrinter() {
        val binding = BottomSheetPrinterDisconnectBinding.inflate(layoutInflater)
        val bottomSheetDialog =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        bottomSheetDialog.window?.setDimAmount(0.7f)
        bottomSheetDialog.setContentView(binding.root)

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.isDraggable = false
        behavior.isHideable = false

        binding.closeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        binding.submitBtn.text = getString(R.string.tag_popup_left_btn)
        binding.submitBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun setPrinterAndNotPaper() {
        val binding = BottomSheetAnotherPaperBinding.inflate(layoutInflater)
        val bottomSheetDialog =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        bottomSheetDialog.window?.setDimAmount(0.7f)
        bottomSheetDialog.setContentView(binding.root)

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.isDraggable = false
        behavior.isHideable = false

        Glide.with(this)
            .load(R.drawable.sample_img_1)
            .into(binding.contentResultImg)

        binding.closeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        binding.nextBtn.text = getString(R.string.tag_popup_left_btn_1)
        binding.nextBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        binding.submitBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
            showToasting()
        }

        bottomSheetDialog.show()
    }

    private fun showToasting() {
        val dialogToast = DialogToastingCommon()
        dialogToast.showLoading(supportFragmentManager)
    }

    private fun goInputTag() {
        val intent = Intent(this, InputTagActivity::class.java)
        tagEditLauncher.launch(intent)
    }

    private fun updateHashTagList(tagText: String) {
        val requestEditContentHashTagDto = RequestEditContentHashTagDto()
        requestEditContentHashTagDto.cntnts_no = cntntsNo
        requestEditContentHashTagDto.user_id = AppDataPref.userId
        requestEditContentHashTagDto.share_hash_tag = tagText

        libDetailViewModel.updateHashTag(requestEditContentHashTagDto)
    }

    private val tagEditLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val tagText = TagResultListStorage.tagArrayList
                    ?.joinToString("") { it.replace("\\s".toRegex(), "") } ?: ""

                binding.flexTagView.removeAllViews()
                updateHashTagList(tagText)
            }
        }
}