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
    private var isDeleteHashTag = false //TODO 해시태그 삭제

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

            isDeleteHashTag -> {
                dialogNormalBinding.btnLeft.visibility = VISIBLE
                dialogNormalBinding.btnRight.visibility = VISIBLE

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