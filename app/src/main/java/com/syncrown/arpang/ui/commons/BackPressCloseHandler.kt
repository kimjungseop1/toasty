package com.syncrown.arpang.ui.commons

import android.app.Activity
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.syncrown.arpang.R

class BackPressCloseHandler(
    private val activity: Activity,
    private val selectedView: BottomNavigationView,
    private var backKeyPressedTime: Long = 0L,
    private var toast: Toast? = null
) : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
        if (selectedView.selectedItemId != R.id.nav_1) {
            selectedView.selectedItemId = R.id.nav_1
        } else {
            timerFinish()
        }
    }

    private fun timerFinish() {
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
        toast = Toast.makeText(activity, "\'뒤로\'버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT)
        toast.apply {
            this?.show()
        }
    }
}