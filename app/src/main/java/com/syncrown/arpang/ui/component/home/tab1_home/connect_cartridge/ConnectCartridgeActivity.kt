package com.syncrown.arpang.ui.component.home.tab1_home.connect_cartridge

import android.os.Bundle
import com.syncrown.arpang.databinding.ActivityConnectCatridgeBinding
import com.syncrown.arpang.ui.base.BaseActivity

class ConnectCartridgeActivity : BaseActivity() {
    private lateinit var binding: ActivityConnectCatridgeBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityConnectCatridgeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }
    }
}