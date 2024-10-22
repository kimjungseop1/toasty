package com.syncrown.arpang.ui.commons

import android.animation.ObjectAnimator
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.syncrown.arpang.databinding.DialogToastingBinding


class DialogToastingCommon : DialogFragment() {
    private lateinit var dialog: Dialog
    private lateinit var dialogToastingBinding: DialogToastingBinding

    private fun createNormal(): DialogToastingCommon {
        val dialog = DialogToastingCommon()
        return dialog
    }

    private fun createDialogLogout(): DialogToastingCommon {
        val dialog: DialogToastingCommon = createNormal()

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
            setDimAmount(0.4f)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        dialogToastingBinding = DialogToastingBinding.inflate(layoutInflater)
        dialog.setContentView(dialogToastingBinding.root)
        dialog.setCanceledOnTouchOutside(false)

        dialogToastingBinding.loadingImage1.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val location = IntArray(2)
                    dialogToastingBinding.loadingImage1.getLocationOnScreen(location)
                    val x = location[0]
                    val y = location[1]

                    val startY = 0f
                    val endY = -y * 0.15f

                    val animator =
                        ObjectAnimator.ofFloat(dialogToastingBinding.loadingImage1, "translationY", startY, endY).apply {
                            duration = 500
                            interpolator = LinearInterpolator()
                            repeatCount = ObjectAnimator.INFINITE
                            repeatMode = ObjectAnimator.REVERSE
                        }
                    animator.start()

                    dialogToastingBinding.loadingImage1.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        )

        dialog.show()

        return dialog
    }
}