package com.syncrown.toasty.ui.component.home.tab1_home.main

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.syncrown.toasty.AppDataPref
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.FragmentHomeBinding
import com.syncrown.toasty.ui.commons.CommonFunc
import com.syncrown.toasty.ui.component.home.MainViewModel
import com.syncrown.toasty.ui.component.home.tab1_home.ar_guide.ArGuideActivity
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.videoselect.VideoSelectActivity
import com.syncrown.toasty.ui.component.home.tab1_home.connect_device.ConnectDeviceActivity
import com.syncrown.toasty.ui.component.home.tab1_home.empty_cartridge.EmptyCartridgeActivity
import com.syncrown.toasty.ui.component.home.tab1_home.event.EventGuideActivity
import com.syncrown.toasty.ui.component.home.tab1_home.main.adapter.CasePagerAdapter
import com.syncrown.toasty.ui.component.home.tab1_home.main.adapter.MainEventAdapter
import com.syncrown.toasty.ui.component.home.tab1_home.main.adapter.SlideBannerAdapter
import com.syncrown.toasty.ui.component.home.tab1_home.paper_guide.PaperGuideActivity

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: MainViewModel by activityViewModels()

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO 프린트 연결
        binding.connectPrintView.setOnClickListener {
            goConnectDevice()
        }

        //TODO 메인 베너
        showSlideBanner()

        //TODO 용지 장착 가이드
        binding.guidePaperBtn.setOnClickListener {
            goPaperGuide()
        }

        //TODO AR 영상 인쇄
        binding.arPrintBtn.setOnClickListener {
            goArPrint()
        }

        //TODO 인생 네컷
        binding.life4cutBtn.setOnClickListener {
            goLife4Cut()
        }

        //TODO 자유 인쇄
        binding.freePrintBtn.setOnClickListener {
            goFreePrint()
        }

        //TODO 라벨 스티커
        binding.labelStickerBtn.setOnClickListener {
            goLabelSticker()
        }

        //TODO 이렇게 활용해 볼까요
        binding.viewPagerView.adapter = CasePagerAdapter(requireActivity())
        binding.viewPagerView.isUserInputEnabled = false
        TabLayoutMediator(binding.customTabView, binding.viewPagerView) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "AR 영상 인쇄"
                }

                1 -> {
                    tab.text = "인생네컷"
                }

                2 -> {
                    tab.text = "라벨 스티커"
                }
            }
        }.attach()
        setTabMargins(binding.customTabView)

        //TODO 이벤트
        showBottomEventBanner()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(slideRunnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(slideRunnable, 3000)
    }

    private fun goConnectDevice() {
        val intent = Intent(requireContext(), ConnectDeviceActivity::class.java)
        startActivity(intent)
    }

    private val slideRunnable = Runnable { binding.bannerView.currentItem += 1 }

    @SuppressLint("SetTextI18n")
    private fun showSlideBanner() {
        val arrayList = ArrayList<String>()
        arrayList.add("1")
        arrayList.add("2")
        arrayList.add("3")
        arrayList.add("4")

        val pagerWidth = resources.getDimensionPixelOffset(R.dimen.pagerWidth)
        val screenWidth = resources.displayMetrics.widthPixels
        val pagerPadding = ((screenWidth - pagerWidth) * 0.5).toInt()
        val offsetPx = ((screenWidth - pagerWidth) * 0.25).toInt()

        binding.bannerView.currentItem = 0
        binding.pageView.text = binding.bannerView.currentItem.toString() + " /" + arrayList.size
        val bannerAdapter = SlideBannerAdapter(
            requireContext(),
            binding.bannerView,
            arrayList,
            object : SlideBannerAdapter.OnPageChangeListener {
                override fun onPageChanged(currentPage: Int, totalItems: Int) {
                    binding.pageView.text = "$currentPage / $totalItems"
                }
            })
        binding.bannerView.apply {
            adapter = bannerAdapter
            offscreenPageLimit = arrayList.size
            setPadding(pagerPadding, 0, pagerPadding, 0)
            setPageTransformer { page, position ->
                page.translationX = position * offsetPx
            }
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (arrayList.size > 1 && handler != null) {
                        handler.removeCallbacks(slideRunnable)
                        handler.postDelayed(slideRunnable, 3000)
                    }
                }
            })
        }

        binding.bannerLeft.setOnClickListener {
            binding.bannerView.currentItem -= 1
        }

        binding.bannerRight.setOnClickListener {
            binding.bannerView.currentItem += 1
        }

    }

    private fun setTabMargins(tabLayout: TabLayout) {
        val tabStrip = tabLayout.getChildAt(0) as ViewGroup
        for (i in 0 until tabStrip.childCount) {
            val tab = tabStrip.getChildAt(i)
            val layoutParams = tab.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.marginEnd = CommonFunc.dpToPx(5, requireContext()) // 탭 사이의 간격 (픽셀)
            tab.requestLayout()
        }
    }

    private fun showBottomEventBanner() {
        val arrayList = ArrayList<String>()
        arrayList.add("1")
        arrayList.add("1")

        val eventAdapter = MainEventAdapter(requireContext(), arrayList)
        eventAdapter.setOnItemClickListener(object : MainEventAdapter.OnItemClickListener {
            override fun onClick(position: Int) {
                goEventGuide()
            }
        })
        binding.mainEventView.apply {
            adapter = eventAdapter
        }
    }

    private fun goArPrint() {
        if (AppDataPref.isArGuide) {
            // 처음 진입시 가이드 화면으로 이동
            val intent = Intent(requireContext(), ArGuideActivity::class.java)
            startActivity(intent)
        } else {
            if (AppDataPref.isCatridge) {
                // 용지와 프린트 모두 연결되있을때 영상 선택화면
                val intent = Intent(requireContext(), VideoSelectActivity::class.java)
                intent.putExtra(
                    "FROM_HOME_CATEGORY",
                    getString(R.string.cartridge_empty_action_text_1)
                )
                startActivity(intent)
            } else {
                // 처음 진입은 아니지만 용지가 없거나 프린트 미연결상태 용지화면으로 이동
                val intent = Intent(requireContext(), EmptyCartridgeActivity::class.java)
                intent.putExtra(
                    "FROM_HOME_CATEGORY",
                    getString(R.string.cartridge_empty_action_text_1)
                )
                startActivity(intent)
            }
        }
    }

    private fun goLife4Cut() {
        val intent = Intent(requireContext(), EmptyCartridgeActivity::class.java)
        intent.putExtra("FROM_HOME_CATEGORY", getString(R.string.cartridge_empty_action_text_2))
        startActivity(intent)
    }

    private fun goLabelSticker() {
        val intent = Intent(requireContext(), EmptyCartridgeActivity::class.java)
        intent.putExtra("FROM_HOME_CATEGORY", getString(R.string.cartridge_empty_action_text_4))
        startActivity(intent)
    }

    private fun goFreePrint() {
        val intent = Intent(requireContext(), EmptyCartridgeActivity::class.java)
        intent.putExtra("FROM_HOME_CATEGORY", getString(R.string.cartridge_empty_action_text_3))
        startActivity(intent)
    }

    private fun goEventGuide() {
        val intent = Intent(requireContext(), EventGuideActivity::class.java)
        startActivity(intent)
    }

    private fun goPaperGuide() {
        val intent = Intent(requireContext(), PaperGuideActivity::class.java)
        paperGuideLauncher.launch(intent)
    }

    private val paperGuideLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // PaperGuideActivity에서 전달한 데이터 받기
                val message = result.data?.getStringExtra("result_message")
                homeViewModel.paperGuideActivityClosed(message ?: "")
            }
        }


}