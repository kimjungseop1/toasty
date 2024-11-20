package com.syncrown.arpang.ui.component.home.tab5_more.account.name

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.syncrown.arpang.ui.component.home.MainActivity
import com.syncrown.arpang.ui.component.login.LoginActivity
import kotlinx.coroutines.launch

class ChangeNameActivity : BaseActivity() {
    private lateinit var binding: ActivityChangeNameBinding
    private val changeNameViewModel: ChangeNameViewModel by viewModels()

    override fun observeViewModel() {
        lifecycleScope.launch {
            changeNameViewModel.checkNickNameResponseLiveData().observe(this@ChangeNameActivity) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        result.data.let { data ->
                            when (data?.msgCode) {
                                "SUCCESS" -> {
                                    val requestUpdateProfileDto = RequestUpdateProfileDto()
                                    requestUpdateProfileDto.user_id = AppDataPref.userId
                                    requestUpdateProfileDto.nick_nm = binding.inputName.text.toString()

                                    changeNameViewModel.updateProfile(requestUpdateProfileDto)
                                    Log.e("jung","닉네임 중복 안됨")
                                }

                                "DUPPLE" -> {
                                    Log.e("jung","닉네임 중복")
                                }

                                else -> {}
                            }
                        }
                    }

                    is NetworkResult.NetCode -> {
                        Log.e("jung","실패 : ${result.message}")
                        if (result.message.equals("403")) {
                            goLogin()
                        }
                    }

                    is NetworkResult.Error -> {
                        Log.e(TAG, "오류 : ${result.message}")
                    }
                }
            }

            changeNameViewModel.updateProfileResponseLiveData().observe(this@ChangeNameActivity) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        result.data.let { data ->
                            when (data?.msgCode) {
                                "SUCCESS" -> {
                                    val customToast = CustomToast()
                                    customToast.showToastMessage(
                                        supportFragmentManager,
                                        getString(R.string.account_change_name_desc_3),
                                        CustomToastType.BLUE
                                    ) {
                                        //close
                                    }
                                }

                                "FAIL" -> {

                                }
                            }
                        }
                    }

                    is NetworkResult.NetCode -> {
                        Log.e("jung","실패 : ${result.message}")
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

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.account_change_name)

        binding.inputName.requestFocus()

        binding.changeBtn.setOnClickListener {
            if (binding.inputName.text?.isNotEmpty() == true) {
                val requestCheckNickNameDto = RequestCheckNickNameDto()
                requestCheckNickNameDto.user_id = AppDataPref.userId
                requestCheckNickNameDto.nick_nm = binding.inputName.text.toString()

                changeNameViewModel.checkNickName(requestCheckNickNameDto)
            } else {
                val customToast = CustomToast()
                customToast.showToastMessage(
                    supportFragmentManager,
                    "변경할 이름을 입력해주세요.",
                    CustomToastType.RED
                ) {
                    //close
                }
            }
        }
    }
}