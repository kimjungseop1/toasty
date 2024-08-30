package com.syncrown.toasty.ui.commons

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.CustomToastBinding

class CustomToast(private val context: Context) {
    private var toast: Toast? = null

    fun showToast(message: String, type: CustomToastType) {
        val binding = CustomToastBinding.inflate(LayoutInflater.from(context))

        // 레이아웃 내의 이미지와 텍스트를 설정합니다.
        binding.toastMsg.text = message
        binding.toastClose.setOnClickListener { toast?.cancel() }
        when (type) {
            CustomToastType.FAIL -> {
                binding.toastBg.setBackgroundResource(R.drawable.bg_custom_toast_1)
                true
            }

            CustomToastType.SUCCESS -> {
                binding.toastBg.setBackgroundResource(R.drawable.bg_custom_toast_2)
                true
            }

            CustomToastType.WARNING -> {
                binding.toastBg.setBackgroundResource(R.drawable.bg_custom_toast_3)
                binding.toastClose.visibility = View.GONE
                true
            }
        }

        toast = Toast(context).apply {
            duration = Toast.LENGTH_LONG
            view = binding.root
            show()
        }

    }
}