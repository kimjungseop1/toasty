package com.syncrown.toasty.ui.component.home.tab1_home.connect_device

import android.os.Bundle
import com.syncrown.toasty.databinding.ActivityConnectDeviceBinding
import com.syncrown.toasty.ui.base.BaseActivity

class ConnectDeviceActivity : BaseActivity() {
    private lateinit var binding: ActivityConnectDeviceBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityConnectDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }
    }
}