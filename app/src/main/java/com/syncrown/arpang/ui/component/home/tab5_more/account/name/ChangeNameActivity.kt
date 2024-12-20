package com.syncrown.arpang.ui.component.home.tab5_more.account.name

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityChangeNameBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestCheckNickNameDto
import com.syncrown.arpang.network.model.RequestUpdateProfileDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CustomToast
import com.syncrown.arpang.ui.commons.CustomToastType
import kotlinx.coroutines.launch

class ChangeNameActivity : BaseActivity() {
    private lateinit var binding: ActivityChangeNameBinding
    private val changeNameViewModel: ChangeNameViewModel by viewModels()

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun observeViewModel() {
        lifecycleScope.launch {
            changeNameViewModel.checkNickNameResponseLiveData()
                .observe(this@ChangeNameActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            result.data.let { data ->
                                when (data?.msgCode) {
                                    "SUCCESS" -> {
                                        updateNickName()
                                        Log.e("jung", "닉네임 중복 안됨")
                                    }

                                    "DUPPLE" -> {
                                        Log.e("jung", "닉네임 중복")
                                        val msg = getString(R.string.account_change_dupple_name)
                                        showToastMessage(msg, CustomToastType.RED)
                                    }

                                    else -> {}
                                }
                            }
                        }

                        is NetworkResult.NetCode -> {
                            Log.e("jung", "실패 : ${result.message}")
                            if (result.message.equals("403")) {
                                goLogin()
                            }
                        }

                        is NetworkResult.Error -> {
                            Log.e(TAG, "오류 : ${result.message}")
                        }
                    }
                }

            changeNameViewModel.updateProfileResponseLiveData()
                .observe(this@ChangeNameActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            result.data.let { data ->
                                when (data?.msgCode) {
                                    "SUCCESS" -> {
                                        val msg = getString(R.string.account_change_name_desc_3)
                                        showToastMessage(msg, CustomToastType.BLUE)
                                    }
                                }
                            }
                        }

                        is NetworkResult.NetCode -> {
                            Log.e("jung", "실패 : ${result.message}")
                            if (result.message.equals("403")) {
                                goLogin()
                            }
                        }

                        is NetworkResult.Error -> {
                            Log.e(TAG, "오류 : ${result.message}")
                        }
                    }
                }
        }
    }

    override fun initViewBinding() {
        binding = ActivityChangeNameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(callback)

        binding.actionbar.actionBack.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.account_change_name)

        binding.inputName.requestFocus()

        binding.changeBtn.setOnClickListener {
            if (binding.inputName.text?.isNotEmpty() == true) {
                checkDuplicateNickName()
            } else {
                val msg = getString(R.string.account_change_input_name)
                showToastMessage(msg, CustomToastType.RED)
            }
        }
    }

    private fun checkDuplicateNickName() {
        val requestCheckNickNameDto = RequestCheckNickNameDto()
        requestCheckNickNameDto.user_id = AppDataPref.userId
        requestCheckNickNameDto.app_id = "APP_ARPANG"
        requestCheckNickNameDto.nick_nm = binding.inputName.text.toString()

        changeNameViewModel.checkNickName(requestCheckNickNameDto)
    }

    private fun updateNickName() {
        val requestUpdateProfileDto = RequestUpdateProfileDto()
        requestUpdateProfileDto.user_id = AppDataPref.userId
        requestUpdateProfileDto.nick_nm =
            binding.inputName.text.toString()

        changeNameViewModel.updateProfile(requestUpdateProfileDto)
    }

    private fun showToastMessage(msg: String, type: CustomToastType) {
        val customToast = CustomToast()
        customToast.showToastMessage(
            supportFragmentManager,
            msg,
            type
        ) {
            //close
        }
    }
}