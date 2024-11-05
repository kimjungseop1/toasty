package com.syncrown.arpang.ui.commons

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.CustomToastBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CustomToast : DialogFragment(), OnClickListener {
    private lateinit var dialog: Dialog
    private lateinit var customToast: CustomToastBinding

    private var closeListener: OnClickListener? = null

    private var messageTxt: String? = null
    private var toastType: CustomToastType? = null

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.closeBtn -> closeListener?.onClick(v)
        }

        dismiss()
    }

    private fun setCloseBtnListener(onClickListener: OnClickListener) {
        closeListener = onClickListener
    }

    private fun createNormal(): CustomToast {
        val dialog = CustomToast()
        return dialog
    }

    private fun createDialogToast(
        message: String,
        type: CustomToastType,
        closeBtn: OnClickListener,
    ): CustomToast {
        val dialog: CustomToast = createNormal()
        dialog.toastType = type
        dialog.messageTxt = message
        dialog.setCloseBtnListener(closeBtn)

        return dialog
    }

    fun showToastMessage(
        manager: FragmentManager?,
        message: String,
        type: CustomToastType,
        closeBtn: OnClickListener
    ) {
        manager?.let { createDialogToast(message, type, closeBtn).show(it, "") }
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
            setDimAmount(0f)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        customToast = CustomToastBinding.inflate(layoutInflater)
        dialog.setContentView(customToast.root)
        dialog.setCanceledOnTouchOutside(false)

        customToast.toastMsg.text = messageTxt

        when (toastType) {
            CustomToastType.BLACK -> {
                customToast.toastBg.setBackgroundResource(R.drawable.bg_custom_toast_1)
            }

            CustomToastType.BLUE -> {
                customToast.toastBg.setBackgroundResource(R.drawable.bg_custom_toast_2)
            }

            CustomToastType.RED -> {
                customToast.toastBg.setBackgroundResource(R.drawable.bg_custom_toast_3)
            }

            CustomToastType.WHITE -> {
                customToast.toastBg.setBackgroundResource(R.drawable.bg_custom_toast_4)
                customToast.toastMsg.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_black
                    )
                )

                Glide.with(requireContext())
                    .load(R.drawable.icon_popup_close_black)
                    .into(customToast.closeBtn)
            }

            else -> {}
        }
        customToast.closeBtn.setOnClickListener(this)

        lifecycleScope.launch {
            delay(3000)
            dismiss()
        }

        dialog.show()

        return dialog
    }
}