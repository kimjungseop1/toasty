package com.syncrown.toasty.ui.commons

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.CustomToastBinding

class CustomToast(private val context: Context) {
    private var toast: Toast? = null

    fun showToast(message: String, type: CustomToastType) {
        val binding = CustomToastBinding.inflate(LayoutInflater.from(context))

        // 레이아웃 내의 이미지와 텍스트를 설정합니다.
        binding.toastMsg.text = message
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
                true
            }
        }

        // 토스트 생성 및 설정
        toast = Toast(context).apply {
            duration = Toast.LENGTH_SHORT
            view = binding.root

            // 그라비티와 레이아웃 파라미터 설정
            setGravity(Gravity.BOTTOM, 0, 100)  // 위치를 필요에 따라 조정
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            binding.root.layoutParams = layoutParams

            show()
        }

    }
}