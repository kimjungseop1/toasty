package com.syncrown.toasty.ui.component.home.tab1_home.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.syncrown.toasty.AppDataPref
import com.syncrown.toasty.databinding.FragmentHomeBinding
import com.syncrown.toasty.ui.component.home.MainViewModel
import com.syncrown.toasty.ui.component.home.tab1_home.ar_guide.ArGuideActivity
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.videoselect.VideoSelectActivity
import com.syncrown.toasty.ui.component.home.tab1_home.connect_device.ConnectDeviceActivity
import com.syncrown.toasty.ui.component.home.tab1_home.event.EventGuideActivity

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        //TODO 프린트 연결
        binding.connectPrintView.setOnClickListener {
            goConnectDevice()
        }

        //TODO AR 영상 인쇄
        binding.arPrintBtn.setOnClickListener {
            goArPrintGuide()
        }

        //TODO 인생 네컷
        binding.life4cutBtn.setOnClickListener {
            goLife4Cut()
        }

        //TODO 라벨 스티커
        binding.labelStickerBtn.setOnClickListener {
            goLabelSticker()
        }

        //TODO 자유 인쇄
        binding.freePrintBtn.setOnClickListener {
            goFreePrint()
        }

        //TODO 이벤트
        binding.eventBtn.setOnClickListener {
            goEventGuide()
        }

        return binding.root
    }

    private fun goConnectDevice() {
        val intent = Intent(requireContext(), ConnectDeviceActivity::class.java)
        startActivity(intent)
    }

    private fun goArPrintGuide() {
        if (AppDataPref.isArGuide) {
            // 처음 진입시 가이드 화면으로 이동
            val intent = Intent(requireContext(), ArGuideActivity::class.java)
            startActivity(intent)
        } else {
            if (AppDataPref.isCatridge) {
                // 용지와 프린트 모두 연결되있을때 영상 선택화면
                val intent = Intent(requireContext(), VideoSelectActivity::class.java)
                startActivity(intent)
            } else {
                // 처음 진입은 아니지만 용지가 없거나 프린트 미연결상태 용지화면으로 이동
            }
        }
    }

    private fun goLife4Cut() {

    }

    private fun goLabelSticker() {

    }

    private fun goFreePrint() {

    }

    private fun goEventGuide() {
        val intent = Intent(requireContext(), EventGuideActivity::class.java)
        startActivity(intent)
    }
}