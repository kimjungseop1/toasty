package com.syncrown.toasty.ui.commons

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
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.DialogNormalBinding


class DialogCommon : DialogFragment(), OnClickListener {
    private lateinit var dialog: Dialog
    private lateinit var dialogNormalBinding: DialogNormalBinding

    private var leftClickListener: OnClickListener? = null
    private var rightClickListener: OnClickListener? = null

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

                dialogNormalBinding.titleView.text = "알림"
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

            else -> {
                dialogNormalBinding.btnLeft.setOnClickListener(this)
                dialogNormalBinding.btnRight.setOnClickListener(this)
            }
        }

        dialog.show()

        return dialog
    }
}