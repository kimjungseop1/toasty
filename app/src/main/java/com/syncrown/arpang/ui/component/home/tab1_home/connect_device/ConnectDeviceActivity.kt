package com.syncrown.arpang.ui.component.home.tab1_home.connect_device

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.arpang.databinding.ActivityConnectDeviceBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.DialogProgressCommon2
import com.syncrown.arpang.ui.commons.VerticalSpaceItemDecoration
import com.syncrown.arpang.ui.component.home.tab1_home.connect_device.adapter.DeviceListAdapter
import com.syncrown.arpang.ui.util.PrintUtil

class ConnectDeviceActivity : BaseActivity() {
    private lateinit var binding: ActivityConnectDeviceBinding
    private var animator: ObjectAnimator? = null

    private lateinit var mBtAdapter: BluetoothAdapter
    private lateinit var adapter: DeviceListAdapter

    val printUtil = PrintUtil.getInstance()

    // 종료시 중복 호출 이슈 처리
    private var isDiscoveryFinishedLogged = false

    private lateinit var dialog: DialogProgressCommon2

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityConnectDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(callback)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        dialog = DialogProgressCommon2()

        //블루투스 연동
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBtAdapter = bluetoothManager.adapter

        var filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(mReceiver, filter)

        filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(mReceiver, filter)

        //디바이스 목록
        setDeviceList()

        //재검색
        binding.searchBtn.setOnClickListener {
            rotateImageDeviceSearch()

            doDiscovery()
        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            binding.searchBtn.performClick()
        }, 500)
    }

    @SuppressLint("MissingPermission")
    private fun doDiscovery() {
        isDiscoveryFinishedLogged = false
        //재검색시 기존 목록 모두 제거
        adapter.clearItems()

        if (mBtAdapter.isDiscovering) {
            mBtAdapter.cancelDiscovery()
        }

        mBtAdapter.startDiscovery()
    }

    private fun setDeviceList() {
        adapter = DeviceListAdapter(this, emptyList<BluetoothDevice>().toMutableList())
        binding.recyclerDevice.layoutManager = LinearLayoutManager(this)
        adapter.setOnItemClickListener(object : DeviceListAdapter.OnItemClickListener {
            override fun onClick(position: Int, name: String, address: String) {
                Log.e("jung", "click : $position, name : $name, address : $address")

                dialog.showLoading(supportFragmentManager)

                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    printUtil.connect(name, address)

                    setResult(RESULT_OK)
                    finish()
                }, 500)

            }
        })
        binding.recyclerDevice.adapter = adapter
        binding.recyclerDevice.addItemDecoration(
            VerticalSpaceItemDecoration(
                CommonFunc.dpToPx(
                    16,
                    this
                )
            )
        )
    }

    private fun rotateImageDeviceSearch() {
        animator = ObjectAnimator.ofFloat(binding.searchImg, "rotation", 0f, 360f).apply {
            duration = 500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
        }
        animator?.start()
    }

    private fun stopRotation() {
        animator?.cancel()
    }

    @SuppressLint("MissingPermission")
    override fun onDestroy() {
        super.onDestroy()
        mBtAdapter.cancelDiscovery()

        if (this::dialog.isInitialized && dialog.isVisible) {
            dialog.dismiss()
        }
    }

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission", "NewApi")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action

            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device = intent.getParcelableExtra(
                        BluetoothDevice.EXTRA_DEVICE,
                        BluetoothDevice::class.java
                    )

                    device?.let {
                        val deviceName = it.name ?: "Unknown Device"
                        val deviceAddress = it.address
                        Log.e(
                            "jung",
                            "Discovered Device: Name = $deviceName, Address = $deviceAddress"
                        )

                        if (deviceName.startsWith("ZP202")) {
                            adapter.addItem(it)
                        }
                    }
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    if (!isDiscoveryFinishedLogged) {
                        Log.e("jung", "Discovery finished")
                        stopRotation()
                    }
                }
            }

            if (adapter.itemCount > 0) {
                binding.recyclerDevice.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
            } else {
                binding.recyclerDevice.visibility = View.GONE
                binding.emptyView.visibility = View.VISIBLE
            }
        }

    }

    /**
     * 토스티가 먼저 들어가는 문제로 사용설명도 일단 하나만 보이도록
     */
//    private fun setUsePager() {
//        binding.viewPagerView.adapter = UsePagerAdapter(this)
//        binding.viewPagerView.isUserInputEnabled = false
//        TabLayoutMediator(binding.customTabView, binding.viewPagerView) { tab, position ->
//            when (position) {
//                0 -> {
//                    tab.text = "토스티"
//                }
//
//                1 -> {
//                    tab.text = "오브니"
//                }
//            }
//        }.attach()
//    }
}