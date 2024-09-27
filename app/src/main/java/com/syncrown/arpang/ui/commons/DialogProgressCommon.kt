package com.syncrown.arpang.ui.commons

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.syncrown.arpang.databinding.DialogLoadingBinding


class DialogProgressCommon : DialogFragment() {
    private lateinit var dialog: Dialog
    private lateinit var dialogLoadingBinding: DialogLoadingBinding

    private fun createNormal(): DialogProgressCommon {
        val dialog = DialogProgressCommon()
        return dialog
    }

    private fun createDialogLogout(): DialogProgressCommon {
        val dialog: DialogProgressCommon = createNormal()

        return dialog
    }

    fun showLoading(
        manager: FragmentManager?
    ) {
        manager?.let { createDialogLogout().show(it, "") }
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

        dialogLoadingBinding = DialogLoadingBinding.inflate(layoutInflater)
        dialog.setContentView(dialogLoadingBinding.root)
        dialog.setCanceledOnTouchOutside(false)

        dialog.show()

        return dialog
    }
}