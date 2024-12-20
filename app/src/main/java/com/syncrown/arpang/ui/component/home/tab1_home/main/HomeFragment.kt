package com.syncrown.arpang.ui.component.home.tab1_home.main

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.FragmentHomeBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestAppMainDto
import com.syncrown.arpang.network.model.RequestMainBannerDto
import com.syncrown.arpang.network.model.ResponseAppMainDto
import com.syncrown.arpang.network.model.ResponseMainBannerDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.component.home.MainViewModel
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videoselect.VideoSelectActivity
import com.syncrown.arpang.ui.component.home.tab1_home.connect_cartridge.ConnectPaperActivity
import com.syncrown.arpang.ui.component.home.tab1_home.connect_device.ConnectDeviceActivity
import com.syncrown.arpang.ui.component.home.tab1_home.empty_cartridge.EmptyCartridgeActivity
import com.syncrown.arpang.ui.component.home.tab1_home.event.EventDetailActivity
import com.syncrown.arpang.ui.component.home.tab1_home.festival_sticker.EditFestivalActivity
import com.syncrown.arpang.ui.component.home.tab1_home.free_print.EditFreePrintActivity
import com.syncrown.arpang.ui.component.home.tab1_home.label_sticker.EditLabelStickerActivity
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.image_select.ImageSelectActivity
import com.syncrown.arpang.ui.component.home.tab1_home.main.adapter.CasePagerAdapter
import com.syncrown.arpang.ui.component.home.tab1_home.main.adapter.MainEventAdapter
import com.syncrown.arpang.ui.component.home.tab1_home.main.adapter.PossibleJobAdapter
import com.syncrown.arpang.ui.component.home.tab1_home.main.adapter.SlideBannerAdapter
import com.syncrown.arpang.ui.component.home.tab1_home.manual.UseManual1DepthActivity
import com.syncrown.arpang.ui.util.PrintUtil
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: MainViewModel by activityViewModels()

    private val mainBannerData = ArrayList<ResponseMainBannerDto.Root>()

    private val possibleList = ArrayList<ResponseAppMainDto.Root>()
    private val imPossibleList = ArrayList<ResponseAppMainDto.Root>()

    private val handler = Handler(Looper.getMainLooper())

    private val printUtil = PrintUtil.getInstance()
    private var currentPaperNM = ""

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
        setHomeBanner()

        //TODO 용지 장착 가이드 (프린트 연결되고 용지 없는 상태에서 visible)
        binding.guidePaperBtn.setOnClickListener {
            goPaperGuide()
        }

        //TODO 장착된 용지로 사용 가능한 기능
        setPossibleJobProcess()

        //TODO 장착된 용지로 사용하지 못하는 기능
        setImPossibleJobProcess()

        //TODO 서비스 이용 가이드
        binding.serviceGuideView.setOnClickListener {
            goServiceManual()
        }

        //TODO 이렇게 활용해 볼까요
        binding.viewPagerView.adapter = CasePagerAdapter(requireActivity())
        binding.viewPagerView.isUserInputEnabled = false

        //TODO 이벤트
        showBottomEventBanner()

        updateView()

        //TODO 데이터 옵져빙
        observeHome()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeHome() {
        lifecycleScope.launch {
            homeViewModel.appMainResponseLiveData().observe(viewLifecycleOwner) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val data = result.data

                        data?.root?.let {
                            possibleList.clear()
                            possibleList.addAll(it)
                            binding.possibleJobRecycler.adapter?.notifyDataSetChanged()
                        }
                    }

                    is NetworkResult.NetCode -> {
                        Log.e("jung", "실패 : ${result.message}")
                        if (result.message.equals("403")) {
                            (activity as? BaseActivity)?.goLogin()
                        }
                    }

                    is NetworkResult.Error -> {
                        Log.e("jung", "오류 : ${result.message}")
                    }
                }
            }
        }

        lifecycleScope.launch {
            homeViewModel.homeBannerResponseLiveData().observe(viewLifecycleOwner) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val data = result.data?.root
                        data?.let {
                            mainBannerData.clear()
                            mainBannerData.addAll(it)
                            showSlideBanner()
                        }
                    }

                    is NetworkResult.NetCode -> {
                        Log.e("jung", "실패 : ${result.message}")
                        if (result.message.equals("403")) {
                            (activity as? BaseActivity)?.goLogin()
                        }
                    }

                    is NetworkResult.Error -> {
                        Log.e("jung", "오류 : ${result.message}")
                    }
                }
            }
        }
    }

    // 연결전 앱메뉴 or 연결후 현재용지로 가능한 작업 메뉴
    private fun setPossibleJobProcess() {
        val requestAppMainDto = RequestAppMainDto()
        requestAppMainDto.app_id = "APP_ARPANG"
        homeViewModel.appMain(requestAppMainDto)

        binding.possibleJobRecycler.layoutManager = GridLayoutManager(requireContext(), 3)

        val adapter = PossibleJobAdapter(requireContext(), possibleList)
        binding.possibleJobRecycler.adapter = adapter
        adapter.setJobListener(object : PossibleJobAdapter.JobListener {
            override fun onJobClick(position: Int, code: String) {
                when (code) {
                    "AR_MENU01" -> {
                        goArPrint(code)
                    } //TODO AR 영상 인쇄
                    "AR_MENU02" -> {
                        goLife4Cut()
                    } //TODO 인생 두컷
                    "AR_MENU03" -> {
                        goLabelSticker()
                    } //TODO 라벨 스티커
                    "AR_MENU04" -> {
                        goFestivalSticker()
                    } //TODO 행사 스트커
                    "AR_MENU05" -> {
                        goFreePrint()
                    } //TODO 자유 인쇄
                }
            }
        })
    }

    // 현재 장착된 용지와 다른용지로 작업가능한 메뉴들 목록
    private fun setImPossibleJobProcess() {
        binding.impossibleJobRecycler.layoutManager = GridLayoutManager(requireContext(), 3)

        val adapter = PossibleJobAdapter(requireContext(), imPossibleList)
        binding.impossibleJobRecycler.adapter = adapter
        adapter.setJobListener(object : PossibleJobAdapter.JobListener {
            override fun onJobClick(position: Int, code: String) {
                when (code) {
                    "AR_MENU01" -> goArPrint(code) //TODO AR 영상 인쇄
                    "AR_MENU02" -> goLife4Cut() //TODO 인생 두컷
                    "AR_MENU03" -> goLabelSticker() //TODO 라벨 스티커
                    "AR_MENU04" -> goFestivalSticker() //TODO 행사 스트커
                    "AR_MENU05" -> goFreePrint() //TODO 자유 인쇄
                }
            }
        })

        if (imPossibleList.size == 0) {
            binding.desc2Sub.visibility = View.GONE
            binding.impossibleJobRecycler.visibility = View.GONE
        } else {
            binding.desc2Sub.visibility = View.VISIBLE
            binding.impossibleJobRecycler.visibility = View.VISIBLE
        }
    }

    private fun invalidateTab() {
        TabLayoutMediator(binding.customTabView, binding.viewPagerView) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.cartridge_empty_action_text_1)
                }

                1 -> {
                    tab.text = getString(R.string.cartridge_empty_action_text_2)
                }

                2 -> {
                    tab.text = getString(R.string.cartridge_empty_action_text_4)
                }
            }
        }.attach()
        setTabMargins(binding.customTabView)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(slideRunnable)
    }

    override fun onResume() {
        super.onResume()

        if (mainBannerData.size > 1) {
            handler.postDelayed(slideRunnable, 500)
        }

        invalidateTab()
    }

    private fun updateView() {
        homeViewModel.connectStateShare.observe(viewLifecycleOwner) { connectState ->
            if (connectState) {
                binding.connectPrintView.visibility = View.GONE
            } else {
                binding.connectPrintView.visibility = View.VISIBLE

                binding.desc1.text = getString(R.string.home_desc_1)
                binding.guidePaperBtn.visibility = View.GONE
            }
        }

        printUtil.paperInfo.observe(requireActivity()) { paperInfo ->
            if (paperInfo.trim().isNotEmpty()) {
                currentPaperNM = paperInfo

                binding.desc1.text = getString(R.string.home_desc_1_2, paperInfo)
                binding.guidePaperBtn.visibility = View.GONE
            } else {
                currentPaperNM = ""

                binding.desc1.text = getString(R.string.home_desc_1_1)
                binding.guidePaperBtn.visibility = View.VISIBLE
            }
        }
    }

    private fun goConnectDevice() {
        val intent = Intent(requireContext(), ConnectDeviceActivity::class.java)
        startActivity(intent)
    }

    private val slideRunnable = Runnable {
        binding.bannerView.currentItem += 1
    }

    private fun setHomeBanner() {
        val requestMainBannerDto = RequestMainBannerDto().apply {
            banner_se_code = "AR_MAIN_BAN"
        }

        homeViewModel.homeBanner(requestMainBannerDto)
    }

    @SuppressLint("SetTextI18n")
    private fun showSlideBanner() {
        if (mainBannerData.isEmpty()) return

        val pagerWidth = resources.displayMetrics.widthPixels * 0.9
        val screenWidth = resources.displayMetrics.widthPixels
        val pagerPadding = ((screenWidth - pagerWidth) * 0.5).toInt()
        val offsetPx = ((screenWidth - pagerWidth) * 0.05).toInt()

        val bannerAdapter = SlideBannerAdapter(
            requireContext(),
            binding.bannerView,
            mainBannerData,
            object : SlideBannerAdapter.OnPageChangeListener {
                override fun onPageChanged(currentPage: Int, totalItems: Int) {
                    binding.pageView.text = "$currentPage / $totalItems"
                }
            })

        binding.bannerView.apply {
            adapter = bannerAdapter
            offscreenPageLimit = mainBannerData.size

            binding.pageView.text = "1 / ${mainBannerData.size}"

            if (mainBannerData.size > 1) {
                setPadding(pagerPadding, 0, pagerPadding, 0)
                setPageTransformer { page, position ->
                    page.translationX = position * offsetPx
                }
                isUserInputEnabled = true
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        if (mainBannerData.size > 1 && handler != null) {
                            handler.removeCallbacks(slideRunnable)
                            handler.postDelayed(slideRunnable, 3000)
                        }
                    }
                })
            } else {
                setPadding(18, 0, 18, 0)
                isUserInputEnabled = false
            }
        }

        bannerAdapter.setOnItemClickListener(object : SlideBannerAdapter.OnItemClickListener {
            override fun onClick(position: Int, clickLink: String) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(clickLink))
                startActivity(intent)
            }
        })

        if (mainBannerData.size > 1) {
            binding.bannerLeft.setOnClickListener {
                binding.bannerView.currentItem -= 1
            }

            binding.bannerRight.setOnClickListener {
                binding.bannerView.currentItem += 1
            }
        } else {
            binding.bannerLeft.visibility = View.GONE
            binding.bannerRight.visibility = View.GONE
        }
    }

    private fun setTabMargins(tabLayout: TabLayout) {
        val tabStrip = tabLayout.getChildAt(0) as ViewGroup
        for (i in 0 until tabStrip.childCount) {
            val tab = tabStrip.getChildAt(i)
            val layoutParams = tab.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.marginEnd = CommonFunc.dpToPx(5, requireContext())
            tab.requestLayout()
        }
    }

    private fun showBottomEventBanner() {
        val arrayList = ArrayList<String>()
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

    private fun goArPrint(code: String) {
        if (currentPaperNM.isEmpty()) {
            val intent = Intent(requireContext(), EmptyCartridgeActivity::class.java)
            intent.putExtra("MAIN_MENU_CODE", code)
            intent.putExtra("FROM_HOME_CATEGORY", getString(R.string.cartridge_empty_action_text_1))
            startActivity(intent)
        } else {
            val intent = Intent(requireContext(), VideoSelectActivity::class.java)
            intent.putExtra("FROM_HOME_CATEGORY", getString(R.string.cartridge_empty_action_text_1))
            intent.putExtra("CARTRIDGE_NAME", currentPaperNM)
            startActivity(intent)
        }
    }

    private fun goLife4Cut() {
        if (currentPaperNM.isEmpty()) {
            val intent = Intent(requireContext(), EmptyCartridgeActivity::class.java)
            intent.putExtra(
                "FROM_HOME_CATEGORY",
                getString(R.string.cartridge_empty_action_text_2)
            )
            startActivity(intent)
        } else {
            val intent = Intent(requireContext(), ImageSelectActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goLabelSticker() {
        if (currentPaperNM.isEmpty()) {
            val intent = Intent(requireContext(), EmptyCartridgeActivity::class.java)
            intent.putExtra("FROM_HOME_CATEGORY", getString(R.string.cartridge_empty_action_text_4))
            startActivity(intent)
        } else {
            val intent = Intent(requireContext(), EditLabelStickerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goFreePrint() {
        if (currentPaperNM.isEmpty()) {
            val intent = Intent(requireContext(), EmptyCartridgeActivity::class.java)
            intent.putExtra("FROM_HOME_CATEGORY", getString(R.string.cartridge_empty_action_text_3))
            startActivity(intent)
        } else {
            val intent = Intent(requireContext(), EditFreePrintActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goFestivalSticker() {
        if (currentPaperNM.isEmpty()) {
            val intent = Intent(requireContext(), EmptyCartridgeActivity::class.java)
            intent.putExtra(
                "FROM_HOME_CATEGORY",
                getString(R.string.cartridge_empty_action_text_5)
            )
            startActivity(intent)
        } else {
            val intent = Intent(requireContext(), EditFestivalActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goServiceManual() {
        val intent = Intent(requireContext(), UseManual1DepthActivity::class.java)
        startActivity(intent)
    }

    private fun goEventGuide() {
        val intent = Intent(requireContext(), EventDetailActivity::class.java)
        startActivity(intent)
    }

    private fun goPaperGuide() {
        val intent = Intent(requireContext(), ConnectPaperActivity::class.java)
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