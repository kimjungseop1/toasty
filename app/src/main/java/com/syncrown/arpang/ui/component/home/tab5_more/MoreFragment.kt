package com.syncrown.arpang.ui.component.home.tab5_more

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.databinding.FragmentMoreBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestSubscribeTotalDto
import com.syncrown.arpang.network.model.RequestUserProfileDto
import com.syncrown.arpang.network.model.ResponseUserProfileDto
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.component.home.MainViewModel
import com.syncrown.arpang.ui.component.home.tab5_more.account.AccountManageActivity
import com.syncrown.arpang.ui.component.home.tab5_more.alert.AlertActivity
import com.syncrown.arpang.ui.component.home.tab5_more.alert_setting.AlertSettingActivity
import com.syncrown.arpang.ui.component.home.tab5_more.block.BlockUserActivity
import com.syncrown.arpang.ui.component.home.tab5_more.event.EventAllActivity
import com.syncrown.arpang.ui.component.home.tab5_more.notice.NoticeActivity
import com.syncrown.arpang.ui.component.home.tab5_more.scrap.ScrapActivity
import com.syncrown.arpang.ui.component.home.tab5_more.subscribe.SubscribeActivity
import com.syncrown.arpang.ui.component.home.tab5_more.subscribe.SubscribeType
import com.syncrown.arpang.ui.component.join.term_privacy.PolishWebActivity
import com.syncrown.arpang.ui.component.login.LoginActivity
import kotlinx.coroutines.launch

class MoreFragment : Fragment() {
    private lateinit var binding: FragmentMoreBinding
    private val moreViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreBinding.inflate(layoutInflater)

        return binding.root
    }

    private fun observeData() {
        lifecycleScope.launch {
            moreViewModel.getUserProfileResponseLiveData().observe(viewLifecycleOwner) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        result.data.let { data ->
                            when (data?.msgCode) {
                                "SUCCESS" -> {
                                    updateUi(data)
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
                        Log.e("jung", "오류 : ${result.message}")
                    }
                }
            }
        }

        lifecycleScope.launch {
            moreViewModel.subscribeTotalCountResponseLiveData()
                .observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.root
                            if (data?.msgCode.equals("SUCCESS")) {
                                binding.subscribeI.text = data?.my_subscription_cnt.toString()
                                binding.subscribeMe.text = data?.me_subscription_cnt.toString()
                            }
                        }

                        is NetworkResult.NetCode -> {
                            Log.e("jung", "실패 : ${result.message}")
                            if (result.message.equals("403")) {
                                goLogin()
                            }
                        }

                        is NetworkResult.Error -> {
                            Log.e("jung", "오류 : ${result.message}")
                        }
                    }
                }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSubscribeCount()

        getUserProfile()

        observeData()
    }

    private fun getSubscribeCount() {
        val requestSubscribeTotalDto = RequestSubscribeTotalDto()
        requestSubscribeTotalDto.user_id = AppDataPref.userId

        moreViewModel.subscribeTotalCount(requestSubscribeTotalDto)
    }

    private fun getUserProfile() {
        val requestUserProfileDto = RequestUserProfileDto()
        requestUserProfileDto.user_id = AppDataPref.userId

        moreViewModel.getUserProfile(requestUserProfileDto)
    }

    private fun updateUi(data: ResponseUserProfileDto) {
        //TODO 사용자 이름
        if (data.nick_nm == null || data.nick_nm.toString().isEmpty()) {
            binding.nameTxt.text = data.user_id
        } else {
            binding.nameTxt.text = data.nick_nm
        }

        //TODO 알림함
        binding.alertView.setOnClickListener {
            goAlert()
        }

        //TODO 알림 갯수
        binding.alertCnt.text = "0"

        //TODO 스크랩
        binding.scrapBtn.setOnClickListener {
            goScrap()
        }

        //TODO 내가 구독
        binding.subscribeI.setOnClickListener {
            goSubscribe(SubscribeType.MY)
        }

        //TODO 나를 구독
        binding.subscribeMe.setOnClickListener {
            goSubscribe(SubscribeType.ME)
        }

        //TODO 로그아웃
        binding.logoutBtn.setOnClickListener {
            val dialogCommon = DialogCommon()
            dialogCommon.showLogout(childFragmentManager, {
                // 취소
            }, {
                // 로그아웃
                AppDataPref.clear(requireActivity())
                goLogin()
            })
        }

        //TODO 이벤트 모두보기
        binding.showEventAll.setOnClickListener {
            goEventAllView()
        }

        //TODO 차단관리
        binding.cutOffView.setOnClickListener {
            goCutOff()
        }

        //TODO 알림설정
        binding.alertSetting.setOnClickListener {
            goAlertSetting()
        }

        //TODO 계정관리
        binding.accountManager.setOnClickListener {
            goAccount()
        }

        //TODO 공지사항
        binding.noticeView.setOnClickListener {
            goNotice()
        }

        //TODO 버전
        binding.currentVersionTxt.text = "v" + moreViewModel.appVersion(requireContext())

        //TODO 이용약관
        binding.polishView.setOnClickListener {
            goWebPolish("term")
        }

        //TODO 개인정보
        binding.privacyView.setOnClickListener {
            goWebPolish("privacy")
        }

        //TODO 고객센터
        binding.csView.setOnClickListener {

        }
    }

    private fun goSubscribe(subscribeType: SubscribeType) {
        val intent = Intent(requireContext(), SubscribeActivity::class.java)
        intent.putExtra("SUBSCRIBE_TYPE", subscribeType.name)
        startActivity(intent)
    }

    private fun goAlert() {
        val intent = Intent(requireContext(), AlertActivity::class.java)
        startActivity(intent)
    }

    private fun goScrap() {
        val intent = Intent(requireContext(), ScrapActivity::class.java)
        startActivity(intent)
    }

    private fun goEventAllView() {
        val intent = Intent(requireContext(), EventAllActivity::class.java)
        startActivity(intent)
    }

    private fun goCutOff() {
        val intent = Intent(requireContext(), BlockUserActivity::class.java)
        startActivity(intent)
    }

    private fun goAlertSetting() {
        val intent = Intent(requireContext(), AlertSettingActivity::class.java)
        startActivity(intent)
    }

    private fun goAccount() {
        val intent = Intent(requireContext(), AccountManageActivity::class.java)
        startActivity(intent)
    }

    private fun goNotice() {
        val intent = Intent(requireContext(), NoticeActivity::class.java)
        startActivity(intent)
    }

    private fun goWebPolish(extra: String) {
        val intent = Intent(requireContext(), PolishWebActivity::class.java)
        intent.putExtra("CONSENT_EXTRA", extra)
        startActivity(intent)
    }

    private fun goLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}