package com.syncrown.arpang.ui.component.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.withStarted
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityMainBinding
import com.syncrown.arpang.databinding.BottomSheetEventBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestMainBannerDto
import com.syncrown.arpang.network.model.ResponseMainBannerDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.BackPressCloseHandler
import com.syncrown.arpang.ui.component.home.adapter.MainEventPagerAdapter
import com.syncrown.arpang.ui.component.home.ar_camera.ArCameraActivity
import com.syncrown.arpang.ui.component.home.search.SearchActivity
import com.syncrown.arpang.ui.component.home.tab1_home.main.HomeFragment
import com.syncrown.arpang.ui.component.home.tab2_Lib.main.LibFragment
import com.syncrown.arpang.ui.component.home.tab3_share.main.ShareFragment
import com.syncrown.arpang.ui.component.home.tab4_store.StoreFragment
import com.syncrown.arpang.ui.component.home.tab5_more.MoreFragment
import com.syncrown.arpang.ui.component.home.tab5_more.subscribe.SubscribeActivity
import com.syncrown.arpang.ui.util.PrintUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var backPressCloseHandler: BackPressCloseHandler

    private var bottomSheetDialog: BottomSheetDialog? = null

    private val printUtil = PrintUtil.getInstance()
    private var batteryCheckJob: Job? = null
    private var printerStatusJob: Job? = null
    private var paperStatusJob: Job? = null

    override fun observeViewModel() {
        lifecycleScope.launch {
            mainViewModel.onPaperGuideActivityClosed.observe(this@MainActivity) { value ->
                if (value.equals("GO_STORE")) {
                    onPaperGuideClosed()
                    mainViewModel.paperGuideActivityClosed("")
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                printUtil.connectionState.observe(this@MainActivity) { isConnect ->
                    Log.e("jung", "main isConnect : $isConnect")
                    if (isConnect) {
                        mainViewModel.updateConnectedState(true)
                        startBatteryCheckJob()
                        startPrinterStatusJob()
                        startPaperStatusJob()
                    } else {
                        mainViewModel.updateConnectedState(false)
                        binding.actionbar.actionBattery.visibility = View.GONE
                        batteryCheckJob?.cancel()
                        printerStatusJob?.cancel()
                        paperStatusJob?.cancel()

                        printUtil.disConnect()
                    }
                }
            }
        }

        lifecycleScope.launch {
            printUtil.batteryVol.observe(this@MainActivity) { batteryLevel ->
                if (printUtil.printPP.isConnected) {
                    binding.actionbar.actionBattery.visibility = View.VISIBLE
                } else {
                    binding.actionbar.actionBattery.visibility = View.GONE
                }

                binding.actionbar.actionBattery.setImageResource(getBatteryIconResource(batteryLevel))
            }
        }

        lifecycleScope.launch {
            printUtil.paperInfo.observe(this@MainActivity) { result ->
                if (result.isNotEmpty()) {
                    paperStatusJob?.cancel()
                } else {
                    startPaperStatusJob()
                }
            }
        }

        lifecycleScope.launch {
            mainViewModel.popupBannerResponseLiveData().observe(this@MainActivity) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val data = result.data?.root ?: ArrayList()
                        data.let {
                            Log.e("jung","isMainEvent : ${AppDataPref.isMainEvent}")
                            if (AppDataPref.isMainEvent) {
                                showEventBottomSheet(it)
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
    }

    override fun initViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backPressCloseHandler = BackPressCloseHandler(this, binding.bottomNavigation)
        onBackPressedDispatcher.addCallback(this, backPressCloseHandler)

        printUtil.init(this)

        //TODO default Fragment
        changeFragment(HomeFragment())

        binding.bottomNavigation.itemIconTintList = null
        binding.actionbar.actionCs.visibility = View.VISIBLE
        binding.actionbar.actionSearch.visibility = View.VISIBLE
        binding.actionbar.actionSubscribe.visibility = View.GONE
        binding.actionbar.actionAr.visibility = View.VISIBLE

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_1 -> {
                    changeFragment(HomeFragment())
                    binding.actionbar.actionTitle.text =
                        ContextCompat.getString(this, R.string.splash_title_bic)

                    binding.actionbar.root.visibility = View.VISIBLE
                    binding.actionbar.actionCs.visibility = View.VISIBLE
                    binding.actionbar.actionSearch.visibility = View.VISIBLE
                    binding.actionbar.actionSubscribe.visibility = View.GONE
                    binding.actionbar.actionAr.visibility = View.VISIBLE
                    true
                }

                R.id.nav_2 -> {
                    changeFragment(LibFragment())
                    binding.actionbar.actionTitle.text =
                        ContextCompat.getString(this, R.string.home_nav_title_2)

                    binding.actionbar.root.visibility = View.VISIBLE
                    binding.actionbar.actionCs.visibility = View.GONE
                    binding.actionbar.actionSearch.visibility = View.VISIBLE
                    binding.actionbar.actionSubscribe.visibility = View.GONE
                    binding.actionbar.actionAr.visibility = View.VISIBLE
                    true
                }

                R.id.nav_3 -> {
                    changeFragment(ShareFragment())
                    binding.actionbar.actionTitle.text =
                        ContextCompat.getString(this, R.string.home_nav_title_3)

                    binding.actionbar.root.visibility = View.VISIBLE
                    binding.actionbar.actionCs.visibility = View.GONE
                    binding.actionbar.actionSearch.visibility = View.VISIBLE
                    binding.actionbar.actionSubscribe.visibility = View.VISIBLE
                    binding.actionbar.actionAr.visibility = View.VISIBLE
                    true
                }

                R.id.nav_4 -> {
                    changeFragment(StoreFragment())

                    binding.actionbar.root.visibility = View.GONE
                    true
                }

                R.id.nav_5 -> {
                    changeFragment(MoreFragment())
                    binding.actionbar.actionTitle.text =
                        ContextCompat.getString(this, R.string.home_nav_title_5)

                    binding.actionbar.root.visibility = View.VISIBLE
                    binding.actionbar.actionCs.visibility = View.GONE
                    binding.actionbar.actionSearch.visibility = View.VISIBLE
                    binding.actionbar.actionSubscribe.visibility = View.GONE
                    binding.actionbar.actionAr.visibility = View.VISIBLE
                    true
                }

                else -> false
            }
        }

        binding.actionbar.actionCs.setOnClickListener {
            goCS()
        }

        binding.actionbar.actionSearch.setOnClickListener {
            goSearch()
        }

        binding.actionbar.actionSubscribe.setOnClickListener {
            goSubscribe()
        }

        binding.actionbar.actionAr.setOnClickListener {
            goArPage()
        }


//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                setEventPopupBannerData()
//            }
//        }
        setEventPopupBannerData()
    }

    private fun startBatteryCheckJob() {
        batteryCheckJob?.cancel()
        batteryCheckJob = lifecycleScope.launch {
            while (isActive) {
                printUtil.sendBatteryVol()
                delay(1000)
            }
        }
    }

    private fun startPrinterStatusJob() {
        printerStatusJob?.cancel()
        printerStatusJob = lifecycleScope.launch {
            while (isActive) {
                printUtil.sendPrinterStatus()
                delay(1000)
            }
        }
    }

    private fun startPaperStatusJob() {
        paperStatusJob?.cancel()
        paperStatusJob = lifecycleScope.launch {
            while (isActive) {
                printUtil.sendPaperInfo()
                delay(1000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        batteryCheckJob?.cancel()
        printerStatusJob?.cancel()
        paperStatusJob?.cancel()

        printUtil.disConnect()
    }

    private fun changeFragment(fragment: Fragment): Fragment {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()

        return fragment
    }

    @SuppressLint("RestrictedApi")
    fun showBottomNavigationViewBadge(
        context: Context,
        navigationView: BottomNavigationView,
        index: Int
    ) {
        val bottomNavigationMenuView = navigationView.getChildAt(0) as BottomNavigationMenuView
        val v = bottomNavigationMenuView.getChildAt(index)
        val itemView = v as BottomNavigationItemView

        val badge = LayoutInflater.from(context)
            .inflate(R.layout.notification_badge, bottomNavigationMenuView, false)

        itemView.addView(badge)
    }

    @SuppressLint("RestrictedApi")
    fun hideBottomNavigationViewBadge(navigationView: BottomNavigationView, index: Int) {
        val bottomNavigationMenuView = navigationView.getChildAt(0) as BottomNavigationMenuView
        val v = bottomNavigationMenuView.getChildAt(index)
        val itemView = v as BottomNavigationItemView

        itemView.removeViewAt(itemView.childCount - 1)
    }

    private fun setEventPopupBannerData() {
        val requestMainBannerDto = RequestMainBannerDto().apply {
            banner_se_code = "AR_POP_BAN"
        }

        mainViewModel.popupBanner(requestMainBannerDto)
    }

    private fun showEventBottomSheet(data: ArrayList<ResponseMainBannerDto.Root>) {
        if (bottomSheetDialog?.isShowing == true) {
            return
        }

        val binding = BottomSheetEventBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        bottomSheetDialog?.window?.setDimAmount(0.7f)
        bottomSheetDialog?.setContentView(binding.root)

        val adapter = MainEventPagerAdapter(data) { _, clickLink ->
            Log.e("jung", "clickLink: $clickLink")
        }
        binding.eventViewPager.adapter = adapter
        binding.eventViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.eventViewPager.post {
                    adapter.updateCurrentPage(position)

                    val pageInfo = adapter.getPageInfo()
                    binding.pageView.text = pageInfo
                }
            }
        })

        binding.pageView.text = adapter.getPageInfo()

        binding.leftBtn.setOnClickListener {
            val currentPosition = binding.eventViewPager.currentItem
            if (currentPosition > 0) {
                binding.eventViewPager.currentItem = currentPosition - 1
            }
        }

        binding.rightBtn.setOnClickListener {
            val currentPosition = binding.eventViewPager.currentItem
            if (currentPosition < adapter.itemCount - 1) {
                binding.eventViewPager.currentItem = currentPosition + 1
            }
        }

        binding.continueCheckView.setOnCheckedChangeListener { _, isChecked ->
            AppDataPref.isMainEvent = !isChecked
            AppDataPref.save(this)
        }

        binding.closeView.setOnClickListener { bottomSheetDialog?.dismiss() }

        bottomSheetDialog?.show()
    }

    private fun getBatteryIconResource(batteryLevel: Int): Int {
        return when (batteryLevel) {
            100 -> R.drawable.icon_battery_4
            in 75..99 -> R.drawable.icon_battery_3
            in 50..74 -> R.drawable.icon_battery_2
            in 25..49 -> R.drawable.icon_battery_1
            else -> R.drawable.icon_battery_charge
        }
    }

    private fun onPaperGuideClosed() {
        binding.bottomNavigation.selectedItemId = R.id.nav_4
    }

    private fun goSubscribe() {
        val intent = Intent(this, SubscribeActivity::class.java)
        startActivity(intent)
    }

    private fun goArPage() {
        val intent = Intent(this, ArCameraActivity::class.java)
        startActivity(intent)
    }

    private fun goSearch() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    private fun goCS() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_umBxmxb/chat"))
        startActivity(intent)
    }
}