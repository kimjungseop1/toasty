package com.syncrown.arpang.ui.component.home.tab5_more

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.syncrown.arpang.databinding.FragmentMoreBinding
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.component.home.MainViewModel
import com.syncrown.arpang.ui.component.home.tab5_more.account.AccountManageActivity
import com.syncrown.arpang.ui.component.home.tab5_more.alert.AlertActivity
import com.syncrown.arpang.ui.component.home.tab5_more.alert_setting.AlertSettingActivity
import com.syncrown.arpang.ui.component.home.tab5_more.cutoff.CutOffActivity
import com.syncrown.arpang.ui.component.home.tab5_more.notice.NoticeActivity
import com.syncrown.arpang.ui.component.home.tab5_more.scrap.ScrapActivity
import com.syncrown.arpang.ui.component.join.term_privacy.PolishWebActivity

class MoreFragment : Fragment() {
    private lateinit var binding: FragmentMoreBinding
    private val moreViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreBinding.inflate(layoutInflater)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO 사용자 이름

        //TODO 알림함
        binding.alertView.setOnClickListener {
            goAlert()
        }

        //TODO 스크랩
        binding.scrapBtn.setOnClickListener {
            goScrap()
        }

        //TODO 내가 구독
        binding.subscribeI.setOnClickListener {

        }

        //TODO 나를 구독
        binding.subscribeMe.setOnClickListener {

        }

        //TODO 로그아웃
        binding.logoutBtn.setOnClickListener {
            val dialogCommon = DialogCommon()
            dialogCommon.showLogout(childFragmentManager, {
                // cancel

            }, {
                // ok

            })
        }

        //TODO 이벤트 모두보기
        binding.showEventAll.setOnClickListener {

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

    private fun goAlert() {
        val intent = Intent(requireContext(), AlertActivity::class.java)
        startActivity(intent)
    }

    private fun goScrap() {
        val intent = Intent(requireContext(), ScrapActivity::class.java)
        startActivity(intent)
    }

    private fun goCutOff() {
        val intent = Intent(requireContext(), CutOffActivity::class.java)
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
}