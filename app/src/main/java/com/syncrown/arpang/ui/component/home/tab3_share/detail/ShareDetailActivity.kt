package com.syncrown.arpang.ui.component.home.tab3_share.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.PopupWindow
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
import com.syncrown.arpang.databinding.ActivityShareDetailBinding
import com.syncrown.arpang.databinding.BottomSheetAnotherPaperBinding
import com.syncrown.arpang.databinding.BottomSheetCartridgeBinding
import com.syncrown.arpang.databinding.BottomSheetPaperDisconnectBinding
import com.syncrown.arpang.databinding.BottomSheetPrinterDisconnectBinding
import com.syncrown.arpang.databinding.PopupSubscribeBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestAddCommentDto
import com.syncrown.arpang.network.model.RequestCommentListDto
import com.syncrown.arpang.network.model.RequestCommentReportDto
import com.syncrown.arpang.network.model.RequestContentReportDto
import com.syncrown.arpang.network.model.RequestDelCommentDto
import com.syncrown.arpang.network.model.RequestDetailContentHashTagDto
import com.syncrown.arpang.network.model.RequestFavoriteDto
import com.syncrown.arpang.network.model.RequestScrapUpdateDto
import com.syncrown.arpang.network.model.RequestShareDetailDto
import com.syncrown.arpang.network.model.ResponseCommentListDto
import com.syncrown.arpang.network.model.ResponseDetailContentHashTagDto
import com.syncrown.arpang.network.model.ResponseShareDetailDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CustomDynamicTagView
import com.syncrown.arpang.ui.commons.CustomToast
import com.syncrown.arpang.ui.commons.CustomToastType
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.commons.DialogToastingCommon
import com.syncrown.arpang.ui.component.home.tab3_share.detail.adapter.DetailCommentListAdapter
import com.syncrown.arpang.ui.component.home.tab5_more.subscribe.detail.SubscribeDetailActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


class ShareDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityShareDetailBinding
    private val shareDetailViewModel: ShareDetailViewModel by viewModels()
    private lateinit var detailCommentListAdapter: DetailCommentListAdapter

    private lateinit var data: ArrayList<ResponseCommentListDto.Root>
    private var writeUserId = ""
    private var cntntsNo = ""
    private var currentPage = 1
    private var curPageSize = 10

    override fun observeViewModel() {
        lifecycleScope.launch {
            shareDetailViewModel.shareContentDetailResponseLiveData()
                .observe(this@ShareDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.root
                            data?.let {
                                setupUI(it)
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
            shareDetailViewModel.favoriteUpdateResponseLiveData()
                .observe(this@ShareDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data
                            if (data?.msgCode.equals("SUCCESS")) {
                                binding.likeCount.text = data?.favorite_cnt.toString()
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
            shareDetailViewModel.hashTagListResponseLiveData()
                .observe(this@ShareDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.root ?: ArrayList()

                            showFlexTagView(data)
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
            shareDetailViewModel.commentListResponseLiveData()
                .observe(this@ShareDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.root ?: ArrayList()

                            if (currentPage > 1 && data.isEmpty()) {
                                currentPage = 1
                            } else {
                                detailCommentListAdapter.updateData(data)
                                currentPage++
                            }

                            binding.commentCount.text = result.data?.sub?.comment_cnt.toString()
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
            shareDetailViewModel.addCommentResponseLiveData()
                .observe(this@ShareDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.msgCode
                            if (data.equals("SUCCESS")) {
                                binding.inputComment.text?.clear()
                                hideKeyBoard()

                                currentPage = 1
                                getCommentList()
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
            shareDetailViewModel.deleteCommentResponseLiveData()
                .observe(this@ShareDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.msgCode
                            if (data.equals("SUCCESS")) {
                                binding.inputComment.text?.clear()
                                hideKeyBoard()

                                currentPage = 1
                                getCommentList()
                                //getDetailContent()
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
            shareDetailViewModel.reportContentsResponseLiveData()
                .observe(this@ShareDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.msgCode
                            if (data.equals("SUCCESS")) {
                                val customToast = CustomToast()
                                customToast.showToastMessage(
                                    supportFragmentManager,
                                    getString(R.string.share_detail_content_report),
                                    CustomToastType.BLUE
                                ) {
                                    //close
                                }
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
            shareDetailViewModel.reportCommentResponseLiveData()
                .observe(this@ShareDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.msgCode
                            if (data.equals("SUCCESS")) {
                                val customToast = CustomToast()
                                customToast.showToastMessage(
                                    supportFragmentManager,
                                    getString(R.string.share_detail_comment_report),
                                    CustomToastType.BLUE
                                ) {
                                    //close
                                }
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
            shareDetailViewModel.scrapContentsResponseLiveData()
                .observe(this@ShareDetailActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.msgCode
                            val customToast = CustomToast()

                            if (data.equals("SUCCESS")) {
                                customToast.showToastMessage(
                                    supportFragmentManager,
                                    "스크랩 등록하였습니다",
                                    CustomToastType.BLUE
                                ) {}
                            } else {
                                customToast.showToastMessage(
                                    supportFragmentManager,
                                    "이미 등록되어있습니다.",
                                    CustomToastType.BLUE
                                ) {}
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
        binding = ActivityShareDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        data = ArrayList()

        cntntsNo = intent.getStringExtra("CONTENT_DETAIL_NO").toString()
        getDetailContent()

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.share_detail_title)

        binding.actionbar.actionEtc1.text = getString(R.string.edit_video_print_print)
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
            setPrinterAndAnotherPaper()

            //TODO 프린터 미연동
            setDisconnectPrinter()

            //TODO 프린터 연결 + 용지 미장착
            setPrinterAndNotPaper()
        }

        binding.actionbar.actionEtc2.text = getString(R.string.scrap_title)
        binding.actionbar.actionEtc2.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this,
                R.drawable.icon_action_scrap
            ), null, null, null
        )
        binding.actionbar.actionEtc2.setOnClickListener {
            //스크랩
            setScrapContents()
        }

        binding.actionbar.actionMore.setOnClickListener {
            //신고하기 드롭다운 팝업
            showActionPopupWindow(binding.actionbar.actionMore)
        }
    }

    private fun setScrapContents() {
        val requestScrapUpdateDto = RequestScrapUpdateDto()
        requestScrapUpdateDto.user_id = AppDataPref.userId
        requestScrapUpdateDto.cntnts_no = cntntsNo
        requestScrapUpdateDto.scrap_se = 1

        shareDetailViewModel.scrapContents(requestScrapUpdateDto)
    }

    private fun getDetailContent() {
        val requestShareDetailDto = RequestShareDetailDto()
        requestShareDetailDto.cntnts_no = cntntsNo
        requestShareDetailDto.user_id = AppDataPref.userId
        shareDetailViewModel.shareContentDetail(requestShareDetailDto)
    }

    private fun setupUI(data: ResponseShareDetailDto.ROOT) {
        showEventView()

        writeUserId = data.write_user_id.toString()
        if (AppDataPref.userId == writeUserId) {
            binding.actionbar.actionMore.visibility = View.GONE
        } else {
            binding.actionbar.actionMore.visibility = View.VISIBLE
        }

        showContentView(data)

        getHashTagList()

        getCommentList()
        showCommentView()

        showEditView()
    }

    private fun showEventView() {
        Glide.with(this)
            .load(R.drawable.sample_img_1)
            .circleCrop()
            .into(binding.eventProfile)


    }

    private fun getHashTagList() {
        val requestDetailContentHashTagDto = RequestDetailContentHashTagDto()
        requestDetailContentHashTagDto.cntnts_no = cntntsNo

        shareDetailViewModel.getHashTagList(requestDetailContentHashTagDto)
    }

    @SuppressLint("SetTextI18n")
    private fun showFlexTagView(data: ArrayList<ResponseDetailContentHashTagDto.Root>) {
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

    private fun showContentView(data: ResponseShareDetailDto.ROOT) {
        //게시글 신고

        //다른 게시글로 이동
        binding.anotherLinkView.setOnClickListener {
            goSubscribeUser(data)
        }

        //작성 카테고리
        binding.contentTypeView.text = data.menu_nm

        //작성자
        if (data.nick_nm == null) {
            binding.nameView.text = AppDataPref.userId
        } else {
            binding.nameView.text = data.nick_nm
        }

        //작성일
        binding.dateView.text = getTimeAgo(data.save_ds.toString())

        //작성자의 다른게시글
        val formattedText =
            getString(R.string.share_detail_another_user, binding.nameView.text.toString())
        val content = SpannableString(formattedText)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        binding.anotherLinkView.text = content

        //이미지
        Glide.with(this)
            .load(data.img_url)
            .into(binding.contentImage)

        //좋아요 구분
        if (data.favorite_se.equals("1")) {
            binding.likeView.isSelected = true
        } else {
            binding.likeView.isSelected = false
        }

        //진입시 좋아요 수
        binding.likeCount.text = data.favorite_cnt.toString()

        //좋아요 등록 삭제
        binding.likeView.setOnClickListener {
            binding.likeView.isSelected = !binding.likeView.isSelected
            updateFavorite()
        }

        //진입시 댓글갯수
        binding.commentCount.text = data.comment_cnt.toString()
    }

    private fun updateFavorite() {
        val likeCnt: Int = if (binding.likeView.isSelected) {
            1
        } else {
            0
        }
        val requestFavoriteDto = RequestFavoriteDto()
        requestFavoriteDto.user_id = AppDataPref.userId
        requestFavoriteDto.cntnts_no = cntntsNo
        requestFavoriteDto.favorite_se = likeCnt

        shareDetailViewModel.favoriteUpdate(requestFavoriteDto)
    }

    private fun getTimeAgo(dateString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val currentDate = Date()

        // 주어진 날짜 문자열을 Date로 변환
        val inputDate = dateFormat.parse(dateString) ?: return "잘못된 날짜 형식"

        // 시간 차 계산 (밀리초 단위)
        val diff = currentDate.time - inputDate.time

        return when {
            diff < TimeUnit.MINUTES.toMillis(1) -> "${TimeUnit.MILLISECONDS.toSeconds(diff)}초 전"
            diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)}분 전"
            diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)}시간 전"
            else -> "${TimeUnit.MILLISECONDS.toDays(diff)}일 전"
        }
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
            if (binding.inputComment.text?.isNotEmpty() == true) {
                addComment()
            }
        }
    }

    private fun addComment() {
        val requestAddCommentDto = RequestAddCommentDto()
        requestAddCommentDto.cntnts_no = cntntsNo
        requestAddCommentDto.user_id = AppDataPref.userId
        requestAddCommentDto.comment = binding.inputComment.text.toString()

        shareDetailViewModel.addComment(requestAddCommentDto)
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

    private fun getCommentList() {
        val requestCommentListDto = RequestCommentListDto().apply {
            cntnts_no = cntntsNo
            user_id = AppDataPref.userId
            currPage = currentPage
            pageSize = curPageSize
        }

        shareDetailViewModel.commentList(requestCommentListDto)
    }

    private fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let { view ->
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun setupRecyclerViewScrollListener() {
        binding.recyclerComment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!binding.recyclerComment.canScrollVertically(1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                ) {
                    Log.e("jung", "addOnScrollListener : $currentPage")
                    getCommentList()
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
                        showDeletePopupWindow(view, position, data[position])
                    } else {
                        //남의댓글 신고하기
                        showReportPopupWindow(view, position, data[position])
                    }
                }
            })
        binding.recyclerComment.adapter = detailCommentListAdapter

        setupRecyclerViewScrollListener()
    }

    //글 신고하기
    private fun showActionPopupWindow(anchor: View) {
        val popBinding = PopupSubscribeBinding.inflate(LayoutInflater.from(this))

        val popupWindow = PopupWindow(
            popBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true
        )

        popBinding.menuItem1.text = getString(R.string.lib_popup_report)
        popBinding.menuItem1.setOnClickListener {
            val dialogCommon = DialogCommon()
            dialogCommon.showCommentReport(supportFragmentManager, {
                //닫기
            }, {
                //TODO 신고 개발중
                setContentsReport()
            })
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(anchor, 0, 0)
    }

    private fun setContentsReport() {
        val requestContentReportDto = RequestContentReportDto()
        requestContentReportDto.cntnts_no = cntntsNo
        requestContentReportDto.user_id = AppDataPref.userId
        requestContentReportDto.write_user_id = writeUserId

        shareDetailViewModel.reportContents(requestContentReportDto)
    }

    //댓글 신고하기
    private fun showReportPopupWindow(
        anchor: View,
        position: Int,
        data: ResponseCommentListDto.Root
    ) {
        val popBinding = PopupSubscribeBinding.inflate(LayoutInflater.from(this))

        val popupWindow = PopupWindow(
            popBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true
        )

        popBinding.menuItem1.text = getString(R.string.lib_popup_report)
        popBinding.menuItem1.setOnClickListener {
            val dialogCommon = DialogCommon()
            dialogCommon.showCommentReport(supportFragmentManager, {
                //닫기
            }, {
                //신고
                setCommentReport(data)
            })
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(anchor, 0, 0)
    }

    private fun setCommentReport(data: ResponseCommentListDto.Root) {
        val requestCommentReportDto = RequestCommentReportDto()
        requestCommentReportDto.cntnts_no = cntntsNo
        requestCommentReportDto.comment_seq_no = data.seq_no.toString()
        requestCommentReportDto.user_id = AppDataPref.userId
        requestCommentReportDto.write_user_id = data.write_user_id.toString()

        shareDetailViewModel.reportComment(requestCommentReportDto)
    }

    //댓글 삭제하기
    private fun showDeletePopupWindow(
        anchor: View,
        position: Int,
        data: ResponseCommentListDto.Root
    ) {
        val popBinding = PopupSubscribeBinding.inflate(LayoutInflater.from(this))

        val popupWindow = PopupWindow(
            popBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true
        )

        popBinding.menuItem1.text = getString(R.string.lib_popup_delete)
        popBinding.menuItem1.setTextColor(ContextCompat.getColor(this, R.color.color_black))
        popBinding.menuItem1.setOnClickListener {
            val dialogCommon = DialogCommon()
            dialogCommon.showCommentDelete(supportFragmentManager, {
                //닫기
            }, {
                //삭제
                setDeleteComment(data)
            })
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(anchor, 0, 0)
    }

    private fun setDeleteComment(data: ResponseCommentListDto.Root) {
        val requestDelCommentDto = RequestDelCommentDto()
        requestDelCommentDto.cntnts_no = cntntsNo
        requestDelCommentDto.user_id = AppDataPref.userId
        requestDelCommentDto.seq_no = data.seq_no.toString()

        shareDetailViewModel.deleteComment(requestDelCommentDto)
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
        val binding = BottomSheetPaperDisconnectBinding.inflate(layoutInflater)
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

        binding.submitBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
            showToasting()
        }

        bottomSheetDialog.show()
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

        binding.submitBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
            showToasting()
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

        binding.nextBtn.visibility = View.GONE

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

    private fun goSubscribeUser(data: ResponseShareDetailDto.ROOT) {
        val intent = Intent(this, SubscribeDetailActivity::class.java)
        intent.putExtra("SUB_USER_ID", AppDataPref.userId)
        intent.putExtra("SUB_USER_NAME", data.nick_nm)
        startActivity(intent)
    }
}