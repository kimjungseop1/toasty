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
import com.syncrown.arpang.databinding.DialogLoading1Binding
import com.syncrown.arpang.databinding.DialogLoadingBinding


class DialogProgressCommon2 : DialogFragment() {
    private lateinit var dialog: Dialog
    private lateinit var dialogLoadingBinding: DialogLoading1Binding

    private fun createNormal(): DialogProgressCommon2 {
        val dialog = DialogProgressCommon2()
        return dialog
    }

    private fun createDialog(): DialogProgressCommon2 {
        val dialog: DialogProgressCommon2 = createNormal()

        return dialog
    }

    fun showLoading(
        manager: FragmentManager?
    ) {
        manager?.let { createDialog().show(it, "") }
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
            setDimAmount(0.7f)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        dialogLoadingBinding = DialogLoading1Binding.inflate(layoutInflater)
        dialog.setContentView(dialogLoadingBinding.root)
        dialog.setCancelable(false)
        dialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                // Back 키 입력 무시
                true
            } else {
                false
            }
        }


        dialog.show()

        return dialog
    }
}