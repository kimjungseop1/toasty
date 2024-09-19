package com.syncrown.toasty.ui.component.home.ar_camera

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.ActivityArMainBinding
import com.syncrown.toasty.ui.base.BaseActivity
import kotlinx.coroutines.launch

class ArCameraActivity : BaseActivity() {
    private lateinit var binding: ActivityArMainBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityArMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (openGlVersion.toDouble() >= MIN_OPEN_GL_VERSION) {
            supportFragmentManager.inTransaction {
                replace(
                    R.id.fragmentContainer,
                    ArVideoFragment()
                )
            }
        } else {
            Toast.makeText(this, "지원되지 않는 단말기입니다.", Toast.LENGTH_SHORT).show()
        }

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }
    }

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    private val openGlVersion by lazy {
        (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .deviceConfigurationInfo
            .glEsVersion
    }

    companion object {
        private const val MIN_OPEN_GL_VERSION = 3.0
    }
}