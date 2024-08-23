package com.syncrown.toasty.ui.commons

import android.app.Activity
import android.widget.Toast
import androidx.activity.OnBackPressedCallback

class BackPressCloseHandler(
    private val activity: Activity,
    private var backKeyPressedTime: Long = 0L,
    private var toast: Toast? = null
) : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            showGuide()
            return
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.setResult(Activity.RESULT_OK)
            activity.finish()
            toast?.cancel()
        }
    }

    private fun showGuide() {
        toast = Toast.makeText(activity, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT)
        toast.apply {
            this?.show()
        }
    }
}