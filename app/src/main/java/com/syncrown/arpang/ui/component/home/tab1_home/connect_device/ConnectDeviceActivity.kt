package com.syncrown.arpang.ui.component.home.tab1_home.connect_device

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.arpang.databinding.ActivityConnectDeviceBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.VerticalSpaceItemDecoration
import com.syncrown.arpang.ui.component.home.tab1_home.connect_device.adapter.DeviceListAdapter

class ConnectDeviceActivity : BaseActivity() {
    private lateinit var binding: ActivityConnectDeviceBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityConnectDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        //디바이스 목록
        setDeviceList()

        //재검색
        binding.searchBtn.setOnClickListener {

        }

        //사용방법
    }

    private fun setDeviceList() {
        //임시 data
        val arrayList = ArrayList<String>()
//        arrayList.add("TOASTY MK1 (N1501152)")
//        arrayList.add("TOASTY MK1 (N1501152)")
//        arrayList.add("OVENY MK1 (N1501152)")

        if (arrayList.size > 0) {
            binding.recyclerDevice.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE

            val adapter = DeviceListAdapter(this, arrayList)
            binding.recyclerDevice.layoutManager = LinearLayoutManager(this)
            adapter.setOnItemClickListener(object : DeviceListAdapter.OnItemClickListener {
                override fun onClick(position: Int) {
                    Log.e("jung", "click : $position")
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
        } else {
            binding.recyclerDevice.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        }

    }

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