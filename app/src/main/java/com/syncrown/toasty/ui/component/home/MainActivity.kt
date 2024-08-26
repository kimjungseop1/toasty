package com.syncrown.toasty.ui.component.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.ActivityMainBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.commons.BackPressCloseHandler
import com.syncrown.toasty.ui.component.home.tab1_home.HomeFragment
import com.syncrown.toasty.ui.component.home.tab2_Lib.LibFragment
import com.syncrown.toasty.ui.component.home.tab3_share.ShareFragment
import com.syncrown.toasty.ui.component.home.tab4_store.StoreFragment
import com.syncrown.toasty.ui.component.home.tab5_more.MoreFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var backPressCloseHandler: BackPressCloseHandler

    override fun observeViewModel() {
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

        changeFragment(HomeFragment())

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_1 -> {
                    changeFragment(HomeFragment())
                    true
                }

                R.id.nav_2 -> {
                    changeFragment(LibFragment())
                    true
                }

                R.id.nav_3 -> {
                    changeFragment(ShareFragment())
                    true
                }

                R.id.nav_4 -> {
                    changeFragment(StoreFragment())
                    true
                }

                R.id.nav_5 -> {
                    changeFragment(MoreFragment())
                    true
                }

                else -> false
            }
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
        Log.e("jung", "call .....")
    }
}