package com.syncrown.toasty.ui.component.login.find_pw

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.syncrown.toasty.databinding.ActivityFindPwBinding
import com.syncrown.toasty.ui.base.BaseActivity
import kotlinx.coroutines.launch

class FindPwActivity : BaseActivity() {
    private lateinit var binding: ActivityFindPwBinding
    private val findPwViewModel: FindPwViewModel by viewModels()

    override fun observeViewModel() {
        lifecycleScope.launch {

        }
    }

    override fun initViewBinding() {
        binding = ActivityFindPwBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.sendBtn.setOnClickListener {

        }
    }

}