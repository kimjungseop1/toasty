package com.syncrown.arpang.ui.component.home.tab1_home.connect_device

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.arpang.databinding.ActivityConnectDeviceBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.VerticalSpaceItemDecoration
import com.syncrown.arpang.ui.component.home.tab1_home.connect_device.adapter.DeviceListAdapter
import com.syncrown.arpang.ui.component.home.tab1_home.connect_device.guide.DeviceHowUseActivity

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

        //사용방법 보러가기
        binding.howUseBtn.setOnClickListener {
            goHowUse()
        }
    }

    private fun setDeviceList() {
        //임시 data
        val arrayList = ArrayList<String>()
        arrayList.add("TOASTY MK1 (N1501152)")
        arrayList.add("OVENY MK1 (N1501152)")

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
    }

    private fun goHowUse() {
        val intent = Intent(this, DeviceHowUseActivity::class.java)
        startActivity(intent)
    }
}