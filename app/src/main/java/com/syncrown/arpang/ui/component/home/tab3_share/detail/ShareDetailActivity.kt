package com.syncrown.arpang.ui.component.home.tab3_share.detail

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityShareDetailBinding
import com.syncrown.arpang.databinding.PopupSubscribeBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CustomToast
import com.syncrown.arpang.ui.commons.CustomToastType
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.component.home.tab3_share.detail.adapter.DetailCommentListAdapter


class ShareDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityShareDetailBinding
    private lateinit var detailCommentListAdapter: DetailCommentListAdapter

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityShareDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = "공유공간"

        binding.actionbar.actionEtc1.text = "인쇄"
        binding.actionbar.actionEtc1.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this,
                R.drawable.icon_action_print
            ), null, null, null
        )
        binding.actionbar.actionEtc1.setOnClickListener {

        }

        binding.actionbar.actionEtc2.text = "스크랩"
        binding.actionbar.actionEtc2.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this,
                R.drawable.icon_action_scrap
            ), null, null, null
        )
        binding.actionbar.actionEtc2.setOnClickListener {

        }

        binding.actionbar.actionMore.setOnClickListener {
            showActionPopupWindow(binding.actionbar.actionMore, 0)
        }

        showEventView()

        showContentView()

        showCommentView()

        showEditView()
    }

    private fun showEventView() {
        Glide.with(this)
            .load(R.drawable.sample_img_1)
            .circleCrop()
            .into(binding.eventProfile)


    }

    private fun showContentView() {

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

    private fun showCommentView() {
        val arrayList = ArrayList<String>()
        arrayList.add("0")
        arrayList.add("1")
        arrayList.add("1")
        arrayList.add("1")

        binding.recyclerComment.layoutManager = LinearLayoutManager(this)
        detailCommentListAdapter = DetailCommentListAdapter(
            this,
            arrayList,
            object : DetailCommentListAdapter.OnItemClickListener {
                override fun onClick(position: Int, view: View) {
                    if (arrayList[position] == "0") {
                        showDeletePopupWindow(view, position)
                    } else {
                        showReportPopupWindow(view, position)
                    }
                }
            })
        binding.recyclerComment.adapter = detailCommentListAdapter
    }

    //글 신고하기
    private fun showActionPopupWindow(anchor: View, position: Int) {
        val popBinding = PopupSubscribeBinding.inflate(LayoutInflater.from(this))

        val popupWindow = PopupWindow(
            popBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true
        )

        popBinding.menuItem1.text = "신고하기"
        popBinding.menuItem1.setOnClickListener {
            val dialogCommon = DialogCommon()
            dialogCommon.showCommentReport(supportFragmentManager, {
                //닫기
            }, {
                //신고
                val customToast = CustomToast(this)
                customToast.showToast("게시글을 신고하였습니다.", CustomToastType.BLUE)
            })
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(anchor, 0, 0)
    }

    //댓글 신고하기
    private fun showReportPopupWindow(anchor: View, position: Int) {
        val popBinding = PopupSubscribeBinding.inflate(LayoutInflater.from(this))

        val popupWindow = PopupWindow(
            popBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true
        )

        popBinding.menuItem1.text = "신고하기"
        popBinding.menuItem1.setOnClickListener {
            val dialogCommon = DialogCommon()
            dialogCommon.showCommentReport(supportFragmentManager, {
                //닫기
            }, {
                //신고
                val customToast = CustomToast(this)
                customToast.showToast("게시글을 신고하였습니다.", CustomToastType.BLUE)
            })
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(anchor, 0, 0)
    }

    //댓글 삭제하기
    private fun showDeletePopupWindow(anchor: View, position: Int) {
        val popBinding = PopupSubscribeBinding.inflate(LayoutInflater.from(this))

        val popupWindow = PopupWindow(
            popBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true
        )

        popBinding.menuItem1.text = "삭제하기"
        popBinding.menuItem1.setTextColor(ContextCompat.getColor(this, R.color.color_black))
        popBinding.menuItem1.setOnClickListener {
            val dialogCommon = DialogCommon()
            dialogCommon.showCommentDelete(supportFragmentManager, {
                //닫기
            }, {
                //삭제
                detailCommentListAdapter.removeItem(position)
            })
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(anchor, 0, 0)
    }
}