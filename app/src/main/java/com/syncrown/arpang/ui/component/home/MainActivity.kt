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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityMainBinding
import com.syncrown.arpang.databinding.BottomSheetEventBinding
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var backPressCloseHandler: BackPressCloseHandler

    private var bottomSheetDialog: BottomSheetDialog? = null

    private val printUtil = PrintUtil.getInstance()
    private var batteryCheckJob: Job? = null
    private var printerStatusJob: Job? = null

    override fun observeViewModel() {
        lifecycleScope.launch {
            mainViewModel.onPaperGuideActivityClosed.observe(this@MainActivity, Observer { value ->
                if (value.equals("GO_STORE")) {
                    onPaperGuideClosed()
                    mainViewModel.paperGuideActivityClosed("")
                }
            })
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                fetchDataFromNetwork()
            }
        }

        lifecycleScope.launch {
            printUtil.connectionState.observe(this@MainActivity) { isConnect ->
                Log.e("jung", "main isConnect : $isConnect")
                mainViewModel.updateConnectedState(isConnect)

                if (isConnect) {
                    startBatteryCheckJob()

                    startPrinterStatusJob()

                    binding.actionbar.actionBattery.visibility = View.VISIBLE
                } else {
                    batteryCheckJob?.cancel()
                    printerStatusJob?.cancel()

                    binding.actionbar.actionBattery.visibility = View.GONE
                }
            }

            printUtil.batteryVol.observe(this@MainActivity) { batteryLevel ->
                Log.e("jung", "main batteryLevel : $batteryLevel")
                when {
                    batteryLevel == 100 -> binding.actionbar.actionBattery.setImageResource(R.drawable.icon_battery_4)
                    batteryLevel in 75..99 -> binding.actionbar.actionBattery.setImageResource(R.drawable.icon_battery_3)
                    batteryLevel in 50..74 -> binding.actionbar.actionBattery.setImageResource(R.drawable.icon_battery_2)
                    batteryLevel in 25..50 -> binding.actionbar.actionBattery.setImageResource(R.drawable.icon_battery_1)
                    batteryLevel < 25 -> binding.actionbar.actionBattery.setImageResource(R.drawable.icon_battery_charge)
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

        if (AppDataPref.isMainEvent) {
            showEventBottomSheet()
        }
    }

    private fun startBatteryCheckJob() {
        batteryCheckJob?.cancel()

        batteryCheckJob = lifecycleScope.launch {
            while (true) {
                printUtil.sendBatteryVol()
                delay(1500)
            }
        }
    }

    private fun startPrinterStatusJob() {
        printerStatusJob?.cancel()

        printerStatusJob = lifecycleScope.launch {
            while (true) {
                printUtil.sendPrinterStatus()
                delay(1000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        batteryCheckJob?.cancel()
        printerStatusJob?.cancel()

        printUtil.disConnect()
    }

    private fun changeFragment(fragment: Fragment): Fragment {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()

        return fragment
    }

    private suspend fun fetchDataFromNetwork() {
        val data = withContext(Dispatchers.IO) {
            // 홈 데이터 호출

        }

        updateUi(data)
    }

    private fun updateUi(data: Unit) {
        Log.e("jung", "updateUi call .....")


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

    private fun showEventBottomSheet() {
        if (bottomSheetDialog?.isShowing == true) {
            return
        }

        val binding = BottomSheetEventBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        bottomSheetDialog?.window?.setDimAmount(0.7f)
        bottomSheetDialog?.setContentView(binding.root)

        val pageData = listOf("1", "2", "3")
        val adapter = MainEventPagerAdapter(
            this, pageData
        ) { position ->
            Log.e("jung", "pos : $position")
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
            if (isChecked) {
                AppDataPref.isMainEvent = false
                AppDataPref.save(this)
            }
        }

        binding.closeView.setOnClickListener { bottomSheetDialog?.dismiss() }

        bottomSheetDialog?.show()
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
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"))
        startActivity(intent)
    }
}