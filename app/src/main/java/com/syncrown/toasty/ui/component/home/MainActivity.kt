package com.syncrown.toasty.ui.component.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.syncrown.toasty.AppDataPref
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.ActivityMainBinding
import com.syncrown.toasty.databinding.BottomSheetEventBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.commons.BackPressCloseHandler
import com.syncrown.toasty.ui.component.home.ar_camera.ArCameraActivity
import com.syncrown.toasty.ui.component.home.tab1_home.main.HomeFragment
import com.syncrown.toasty.ui.component.home.tab1_home.search.SearchActivity
import com.syncrown.toasty.ui.component.home.tab2_Lib.main.LibFragment
import com.syncrown.toasty.ui.component.home.tab3_share.ShareFragment
import com.syncrown.toasty.ui.component.home.tab4_store.StoreFragment
import com.syncrown.toasty.ui.component.home.tab5_more.MoreFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var backPressCloseHandler: BackPressCloseHandler

    private var bottomSheetDialog: BottomSheetDialog? = null

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
    }

    override fun initViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backPressCloseHandler = BackPressCloseHandler(this)
        onBackPressedDispatcher.addCallback(this, backPressCloseHandler)

        //TODO default Fragment
        changeFragment(HomeFragment())

        binding.bottomNavigation.itemIconTintList = null
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_1 -> {
                    changeFragment(HomeFragment())
                    binding.actionbar.actionTitle.text =
                        ContextCompat.getString(this, R.string.splash_title_bic)
                    true
                }

                R.id.nav_2 -> {
                    changeFragment(LibFragment())
                    binding.actionbar.actionTitle.text =
                        ContextCompat.getString(this, R.string.home_nav_title_2)
                    true
                }

                R.id.nav_3 -> {
                    changeFragment(ShareFragment())
                    binding.actionbar.actionTitle.text =
                        ContextCompat.getString(this, R.string.home_nav_title_3)
                    true
                }

                R.id.nav_4 -> {
                    changeFragment(StoreFragment())
                    binding.actionbar.actionTitle.text =
                        ContextCompat.getString(this, R.string.home_nav_title_4)
                    true
                }

                R.id.nav_5 -> {
                    changeFragment(MoreFragment())
                    binding.actionbar.actionTitle.text =
                        ContextCompat.getString(this, R.string.home_nav_title_5)
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

        binding.actionbar.actionAr.setOnClickListener {
            goARpage()
        }
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

        // update ui
        updateUi(data)
    }

    private fun updateUi(data: Unit) {
        Log.e("jung", "updateUi call .....")
        if (AppDataPref.isMainEvent) {
            showEventBottomSheet()
        }

        // 네비게이션 뱃지가 필요할때 사용
        showBottomNavigationViewBadge(this, binding.bottomNavigation, 2)
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

    private fun goARpage() {
        val intent = Intent(this, ArCameraActivity::class.java)
        startActivity(intent)
    }

    private fun goSearch() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    private fun goCS() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://pf.kakao.com/_umBxmxb/chat"))
        startActivity(intent)
    }
}