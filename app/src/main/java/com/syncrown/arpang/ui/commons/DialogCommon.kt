package com.syncrown.arpang.ui.commons

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.DialogNormalBinding


class DialogCommon : DialogFragment(), OnClickListener {
    private lateinit var dialog: Dialog
    private lateinit var dialogNormalBinding: DialogNormalBinding

    private var leftClickListener: OnClickListener? = null
    private var rightClickListener: OnClickListener? = null

    private var isNetWorkError = false //TODO 네트워크 장애시 발생

    private var isPermission = false //TODO 권한 체크 팝업
    private var isUpdate = false //TODO 업데이트 안내
    private var isServerOff = false //TODO 점검공지
    private var serverTime: String = ""
    private var isJoin = false //TODO 소셜 기가입 계정 알림
    private var snsPlatform: String = ""
    private var isDeleteHashTag = false //TODO 해시태그 삭제
    private var isEditCancel = false //TODO 인쇄편집 취소
    private var isLogout = false //TODO 로그아웃
    private var isTagDelete = false //TODO 태그 삭제
    private var isSubscribeDel = false //TODO 나를 구독한사람 삭제
    private var isCommentDel = false //TODO 공유공간 상세 코멘트 삭제
    private var isCommentReport = false //TODO 공유공간 상세 코멘트 신고
    private var isSubscribeReport = false //TODO 구독리스트 상세 신고
    private var isSubscribeReject = false //TODO 구독리스트 상세 차단

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_left -> leftClickListener?.onClick(v)
            R.id.btn_right -> rightClickListener?.onClick(v)
        }

        dismiss()
    }

    private fun setLeftBtnListener(onClickListener: OnClickListener) {
        leftClickListener = onClickListener
    }

    private fun setRightBtnListener(onClickListener: OnClickListener) {
        rightClickListener = onClickListener
    }

    private fun createNormal(): DialogCommon {
        val dialog = DialogCommon()
        return dialog
    }

    private fun createDialogNetworkError(
        leftBtn: OnClickListener
    ): DialogCommon {
        val dialog: DialogCommon = createNormal()
        dialog.isNetWorkError = true
        dialog.setLeftBtnListener(leftBtn)

        return dialog
    }

    fun showNetworkError(
        manager: FragmentManager?,
        leftBtn: OnClickListener
    ) {
        manager?.let { createDialogNetworkError(leftBtn) }
    }

    private fun createDialogCommentReport(
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ): DialogCommon {
        val dialog: DialogCommon = createNormal()
        dialog.isCommentReport = true
        dialog.setLeftBtnListener(leftBtn)
        dialog.setRightBtnListener(rightBtn)

        return dialog
    }

    fun showCommentReport(
        manager: FragmentManager?,
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ) {
        manager?.let { createDialogCommentReport(leftBtn, rightBtn).show(it, "") }
    }

    private fun createDialogSubscribeDetailReport(
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ): DialogCommon {
        val dialog: DialogCommon = createNormal()
        dialog.isSubscribeReport = true
        dialog.setLeftBtnListener(leftBtn)
        dialog.setRightBtnListener(rightBtn)

        return dialog
    }

    fun showSubscribeDetailReport(
        manager: FragmentManager?,
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ) {
        manager?.let { createDialogSubscribeDetailReport(leftBtn, rightBtn).show(it, "") }
    }

    private fun createDialogSubscribeDetailReject(
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ): DialogCommon {
        val dialog: DialogCommon = createNormal()
        dialog.isSubscribeReject = true
        dialog.setLeftBtnListener(leftBtn)
        dialog.setRightBtnListener(rightBtn)

        return dialog
    }

    fun showSubscribeDetailReject(
        manager: FragmentManager?,
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ) {
        manager?.let { createDialogSubscribeDetailReject(leftBtn, rightBtn).show(it, "") }
    }

    private fun createDialogCommentDelete(
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ): DialogCommon {
        val dialog: DialogCommon = createNormal()
        dialog.isCommentDel = true
        dialog.setLeftBtnListener(leftBtn)
        dialog.setRightBtnListener(rightBtn)

        return dialog
    }

    fun showCommentDelete(
        manager: FragmentManager?,
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ) {
        manager?.let { createDialogCommentDelete(leftBtn, rightBtn).show(it, "") }
    }

    private fun createDialogSubscribeDelete(
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ): DialogCommon {
        val dialog: DialogCommon = createNormal()
        dialog.isSubscribeDel = true
        dialog.setLeftBtnListener(leftBtn)
        dialog.setRightBtnListener(rightBtn)

        return dialog
    }

    fun showSubscribeDel(
        manager: FragmentManager?,
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ) {
        manager?.let { createDialogSubscribeDelete(leftBtn, rightBtn).show(it, "") }
    }

    private fun createDialogTagDelete(
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ): DialogCommon {
        val dialog: DialogCommon = createNormal()
        dialog.isTagDelete = true
        dialog.setLeftBtnListener(leftBtn)
        dialog.setRightBtnListener(rightBtn)

        return dialog
    }

    fun showTagDelete(
        manager: FragmentManager?,
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ) {
        manager?.let { createDialogTagDelete(leftBtn, rightBtn).show(it, "") }
    }

    private fun createDialogEditCancel(
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ): DialogCommon {
        val dialog: DialogCommon = createNormal()
        dialog.isEditCancel = true
        dialog.setLeftBtnListener(leftBtn)
        dialog.setRightBtnListener(rightBtn)

        return dialog
    }

    fun showEditCancel(
        manager: FragmentManager?,
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ) {
        manager?.let { createDialogEditCancel(leftBtn, rightBtn).show(it, "") }
    }

    private fun createDialogPermission(
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ): DialogCommon {
        val dialog: DialogCommon = createNormal()
        dialog.isPermission = true
        dialog.setLeftBtnListener(leftBtn)
        dialog.setRightBtnListener(rightBtn)

        return dialog
    }

    fun showPermissionPopup(
        manager: FragmentManager?,
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ) {
        manager?.let { createDialogPermission(leftBtn, rightBtn).show(it, "") }
    }

    private fun createDialogUpdate(
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ): DialogCommon {
        val dialog: DialogCommon = createNormal()
        dialog.isUpdate = true
        dialog.setLeftBtnListener(leftBtn)
        dialog.setRightBtnListener(rightBtn)

        return dialog
    }

    fun showUpdatePopup(
        manager: FragmentManager?,
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ) {
        manager?.let { createDialogUpdate(leftBtn, rightBtn).show(it, "") }
    }

    private fun createDialogServer(
        leftBtn: OnClickListener,
        date: String
    ): DialogCommon {
        val dialog: DialogCommon = createNormal()
        dialog.isServerOff = true
        dialog.setLeftBtnListener(leftBtn)
        dialog.serverTime = date

        return dialog
    }

    fun showServerCheckPopup(
        manager: FragmentManager?,
        leftBtn: OnClickListener,
        date: String
    ) {
        manager?.let { createDialogServer(leftBtn, date).show(it, "") }
    }

    private fun createDialogAlreadyJoined(
        leftBtn: OnClickListener,
        platform: String
    ): DialogCommon {
        val dialog: DialogCommon = createNormal()
        dialog.isJoin = true
        dialog.setLeftBtnListener(leftBtn)
        dialog.snsPlatform = platform

        return dialog
    }

    fun showAlreadyJoined(
        manager: FragmentManager?,
        leftBtn: OnClickListener,
        platform: String
    ) {
        manager?.let { createDialogAlreadyJoined(leftBtn, platform) }
    }

    private fun createDialogLogout(
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ): DialogCommon {
        val dialog: DialogCommon = createNormal()
        dialog.isLogout = true
        dialog.setLeftBtnListener(leftBtn)
        dialog.setRightBtnListener(rightBtn)

        return dialog
    }

    fun showLogout(
        manager: FragmentManager?,
        leftBtn: OnClickListener,
        rightBtn: OnClickListener
    ) {
        manager?.let { createDialogLogout(leftBtn, rightBtn).show(it, "") }
    }

    override fun dismiss() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = Dialog(requireActivity())
        dialog.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            setDimAmount(0.5f)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        dialogNormalBinding = DialogNormalBinding.inflate(layoutInflater)
        dialog.setContentView(dialogNormalBinding.root)
        dialog.setCanceledOnTouchOutside(false)

        when {
            isNetWorkError -> {
                dialogNormalBinding.btnLeft.visibility = VISIBLE
                dialogNormalBinding.btnRight.visibility = GONE

                dialogNormalBinding.titleView.text = context?.let {
                    getString(R.string.network_connect_error_title)
                }
                dialogNormalBinding.bodyView.text = context?.let {
                    getString(R.string.network_connect_error_content)
                }

                dialogNormalBinding.btnLeft.setOnClickListener(this)
            }

            isPermission -> {
                dialogNormalBinding.btnLeft.visibility = VISIBLE
                dialogNormalBinding.btnRight.visibility = VISIBLE

                dialogNormalBinding.titleView.text =
                    context?.let { getString(R.string.splash_alert_permission_title) }
                dialogNormalBinding.bodyView.text = context?.let {
                    getString(R.string.splash_alert_permission_message)
                }
                dialogNormalBinding.btnRight.text = context?.let {
                    getString(R.string.splash_alert_permission_btn)
                }
                dialogNormalBinding.btnLeft.text = context?.let {
                    getString(R.string.splash_alert_cancel)
                }

                dialogNormalBinding.btnLeft.setOnClickListener(this)
                dialogNormalBinding.btnRight.setOnClickListener(this)
            }

            isUpdate -> {
                dialogNormalBinding.btnLeft.visibility = VISIBLE
                dialogNormalBinding.btnRight.visibility = VISIBLE

                dialogNormalBinding.titleView.text =
                    context?.let { getString(R.string.splash_alert_update_title) }
                dialogNormalBinding.bodyView.text = context?.let {
                    getString(R.string.splash_alert_update_message)
                }

                dialogNormalBinding.btnLeft.setOnClickListener(this)
                dialogNormalBinding.btnRight.setOnClickListener(this)
            }

            isServerOff -> {
                dialogNormalBinding.btnLeft.visibility = VISIBLE
                dialogNormalBinding.btnRight.visibility = GONE

                dialogNormalBinding.titleView.text = context?.let {
                    getString(R.string.splash_alert_inspection_title)
                }
                dialogNormalBinding.bodyView.text = context?.let {
                    getString(R.string.splash_alert_inspection_message, serverTime)
                }

                dialogNormalBinding.btnLeft.setOnClickListener(this)
            }

            isJoin -> {
                dialogNormalBinding.btnLeft.visibility = VISIBLE
                dialogNormalBinding.btnRight.visibility = GONE

                dialogNormalBinding.titleView.text = context.let {
                    getString(R.string.login_alert_already_sns_title)
                }

                dialogNormalBinding.bodyView.text = context.let {
                    getString(R.string.login_alert_already_sns, snsPlatform)
                }

                dialogNormalBinding.btnLeft.setOnClickListener(this)
            }

            isDeleteHashTag -> {
                dialogNormalBinding.btnLeft.visibility = VISIBLE
                dialogNormalBinding.btnRight.visibility = VISIBLE

                dialogNormalBinding.btnLeft.setOnClickListener(this)
                dialogNormalBinding.btnRight.setOnClickListener(this)
            }

            isEditCancel -> {
                dialogNormalBinding.btnLeft.visibility = VISIBLE
                dialogNormalBinding.btnRight.visibility = VISIBLE

                dialogNormalBinding.titleView.text = context?.let {
                    getString(R.string.edit_video_print_alert_title)
                }
                dialogNormalBinding.bodyView.text = context?.let {
                    getString(R.string.edit_video_print_alert_msg)
                }
                dialogNormalBinding.btnLeft.text = context?.let {
                    getString(R.string.edit_video_print_alert_left_btn)
                }
                dialogNormalBinding.btnRight.text = context?.let {
                    getString(R.string.edit_video_print_alert_right_btn)
                }

                dialogNormalBinding.btnLeft.setOnClickListener(this)
                dialogNormalBinding.btnRight.setOnClickListener(this)
            }

            isLogout -> {
                dialogNormalBinding.btnLeft.visibility = VISIBLE
                dialogNormalBinding.btnRight.visibility = VISIBLE

                dialogNormalBinding.titleView.text = context?.let {
                    getString(R.string.edit_video_print_alert_title)
                }

                dialogNormalBinding.bodyView.text = context?.let {
                    getString(R.string.more_logout_popup_message)
                }

                dialogNormalBinding.btnLeft.text = context?.let {
                    getString(R.string.more_logout_popup_cancel)
                }

                dialogNormalBinding.btnRight.text = context?.let {
                    getString(R.string.more_logout_popup_ok)
                }

                dialogNormalBinding.btnLeft.setOnClickListener(this)
                dialogNormalBinding.btnRight.setOnClickListener(this)
            }

            isTagDelete -> {
                dialogNormalBinding.btnLeft.visibility = VISIBLE
                dialogNormalBinding.btnRight.visibility = VISIBLE

                dialogNormalBinding.titleView.text = context?.let {
                    getString(R.string.edit_video_print_alert_title)
                }

                dialogNormalBinding.bodyView.text = context?.let {
                    getString(R.string.tag_popup_content_message)
                }

                dialogNormalBinding.btnLeft.text = context?.let {
                    getString(R.string.tag_popup_left_btn)
                }

                dialogNormalBinding.btnRight.text = context?.let {
                    getString(R.string.tag_popup_right_btn)
                }

                dialogNormalBinding.btnLeft.setOnClickListener(this)
                dialogNormalBinding.btnRight.setOnClickListener(this)
            }

            isSubscribeDel -> {
                dialogNormalBinding.btnLeft.visibility = VISIBLE
                dialogNormalBinding.btnRight.visibility = VISIBLE

                dialogNormalBinding.titleView.text = context?.let {
                    getString(R.string.edit_video_print_alert_title)
                }

                dialogNormalBinding.bodyView.text = context?.let {
                    getString(R.string.subscribe_popup_delete_message)
                }

                dialogNormalBinding.btnLeft.text = context?.let {
                    getString(R.string.tag_popup_left_btn)
                }

                dialogNormalBinding.btnRight.text = context?.let {
                    getString(R.string.tag_popup_right_btn)
                }

                dialogNormalBinding.btnLeft.setOnClickListener(this)
                dialogNormalBinding.btnRight.setOnClickListener(this)
            }

            isCommentDel -> {
                dialogNormalBinding.btnLeft.visibility = VISIBLE
                dialogNormalBinding.btnRight.visibility = VISIBLE

                dialogNormalBinding.titleView.text = context?.let {
                    getString(R.string.edit_video_print_alert_title)
                }

                dialogNormalBinding.bodyView.text = context?.let {
                    getString(R.string.edit_video_print_alert_delete_content)
                }

                dialogNormalBinding.btnLeft.text = context?.let {
                    getString(R.string.tag_popup_left_btn)
                }

                dialogNormalBinding.btnRight.text = context?.let {
                    getString(R.string.tag_popup_right_btn)
                }

                dialogNormalBinding.btnLeft.setOnClickListener(this)
                dialogNormalBinding.btnRight.setOnClickListener(this)
            }

            isCommentReport -> {
                dialogNormalBinding.btnLeft.visibility = VISIBLE
                dialogNormalBinding.btnRight.visibility = VISIBLE

                dialogNormalBinding.titleView.text = context?.let {
                    getString(R.string.edit_video_print_alert_title)
                }

                dialogNormalBinding.bodyView.text = context?.let {
                    getString(R.string.share_detail_alert_comment_warning)
                }

                dialogNormalBinding.btnLeft.text = context?.let {
                    getString(R.string.tag_popup_left_btn)
                }

                dialogNormalBinding.btnRight.text = context?.let {
                    getString(R.string.share_detail_alert_comment_warning_btn)
                }

                dialogNormalBinding.btnLeft.setOnClickListener(this)
                dialogNormalBinding.btnRight.setOnClickListener(this)
            }

            isSubscribeReport -> {
                dialogNormalBinding.btnLeft.visibility = VISIBLE
                dialogNormalBinding.btnRight.visibility = VISIBLE

                dialogNormalBinding.titleView.text = context?.let {
                    getString(R.string.edit_video_print_alert_title)
                }

                dialogNormalBinding.bodyView.text = context?.let {
                    getString(R.string.share_detail_alert_comment_warning_user)
                }

                dialogNormalBinding.btnLeft.text = context?.let {
                    getString(R.string.tag_popup_left_btn)
                }

                dialogNormalBinding.btnRight.text = context?.let {
                    getString(R.string.share_detail_alert_comment_warning)
                }

                dialogNormalBinding.btnLeft.setOnClickListener(this)
                dialogNormalBinding.btnRight.setOnClickListener(this)
            }

            isSubscribeReject -> {
                dialogNormalBinding.btnLeft.visibility = VISIBLE
                dialogNormalBinding.btnRight.visibility = VISIBLE

                dialogNormalBinding.titleView.text = context?.let {
                    getString(R.string.edit_video_print_alert_title)
                }

                dialogNormalBinding.bodyView.text = context?.let {
                    getString(R.string.share_detail_alert_user_reject)
                }

                dialogNormalBinding.btnLeft.text = context?.let {
                    getString(R.string.tag_popup_left_btn)
                }

                dialogNormalBinding.btnRight.text = context?.let {
                    getString(R.string.share_detail_alert_user_reject_btn)
                }

                dialogNormalBinding.btnLeft.setOnClickListener(this)
                dialogNormalBinding.btnRight.setOnClickListener(this)
            }

            else -> {
                dialogNormalBinding.btnLeft.setOnClickListener(this)
                dialogNormalBinding.btnRight.setOnClickListener(this)
            }
        }

        dialog.show()

        return dialog
    }
}