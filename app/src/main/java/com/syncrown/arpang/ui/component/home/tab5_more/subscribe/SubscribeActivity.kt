package com.syncrown.arpang.ui.component.home.tab5_more.subscribe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivitySubscribeBinding
import com.syncrown.arpang.databinding.PopupSubscribeBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestSubscribeByMeDto
import com.syncrown.arpang.network.model.RequestSubscribeByMyDto
import com.syncrown.arpang.network.model.RequestSubscribeTotalDto
import com.syncrown.arpang.network.model.RequestSubscribeUpdateDto
import com.syncrown.arpang.network.model.ResponseSubscribeListDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.component.home.tab5_more.subscribe.adapter.SubscribeListAdapter
import com.syncrown.arpang.ui.component.home.tab5_more.subscribe.detail.SubscribeDetailActivity
import kotlinx.coroutines.launch

class SubscribeActivity : BaseActivity() {
    private lateinit var binding: ActivitySubscribeBinding
    private val subscribeViewModel: SubscribeViewModel by viewModels()

    private var type = ""

    private lateinit var subscribeListAdapter: SubscribeListAdapter
    private var currentPage = 1
    private var curPageSize = 15

    override fun observeViewModel() {
        lifecycleScope.launch {
            subscribeViewModel.subscribeTotalCountResponseLiveData()
                .observe(this@SubscribeActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.root

                            if (data?.msgCode.equals("SUCCESS")) {
                                binding.subscribeI.text = data?.my_subscription_cnt.toString()
                                binding.subscribeMe.text = data?.me_subscription_cnt.toString()
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

        lifecycleScope.launch {
            subscribeViewModel.subscribeByMyResponseLiveData()
                .observe(this@SubscribeActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.root

                            data?.let {
                                setSubscribeList(it, SubscribeType.MY)
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

        lifecycleScope.launch {
            subscribeViewModel.subscribeByMeResponseLiveData()
                .observe(this@SubscribeActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.root

                            data?.let {
                                setSubscribeList(it, SubscribeType.ME)
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

        lifecycleScope.launch {
            subscribeViewModel.subscribeUpdateResponseLiveData()
                .observe(this@SubscribeActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data
                            if (data?.equals("SUCCESS") == true) {
                                updateUI()
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
        binding = ActivitySubscribeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.subscribe_title)

        getTotalCount()

        binding.clMyView.setOnClickListener {
            binding.clMyView.isSelected = true
            binding.clMeView.isSelected = false

            currentPage = 1
            getSubscribeMyList()

            setSelectedViewBackground()
        }

        binding.clMeView.setOnClickListener {
            binding.clMeView.isSelected = true
            binding.clMyView.isSelected = false

            currentPage = 1
            getSubscribeMeList()

            setSelectedViewBackground()
        }

        type = intent.getStringExtra("SUBSCRIBE_TYPE").toString()
        updateUI()
    }

    private fun updateUI() {
        if (type == SubscribeType.ME.name) {
            binding.clMeView.performClick()
            getSubscribeMeList()
        } else {
            binding.clMyView.performClick()
            getSubscribeMyList()
        }
    }

    private fun getTotalCount() {
        val requestSubscribeTotalDto = RequestSubscribeTotalDto()
        requestSubscribeTotalDto.user_id = AppDataPref.userId

        subscribeViewModel.subscribeTotalCount(requestSubscribeTotalDto)
    }

    private fun getSubscribeMyList() {
        val requestSubscribeByMyDto = RequestSubscribeByMyDto()
        requestSubscribeByMyDto.user_id = AppDataPref.userId
        requestSubscribeByMyDto.currPage = currentPage
        requestSubscribeByMyDto.pageSize = curPageSize

        subscribeViewModel.subscribeByMyList(requestSubscribeByMyDto)
    }

    private fun getSubscribeMeList() {
        val requestSubscribeByMeDto = RequestSubscribeByMeDto()
        requestSubscribeByMeDto.user_id = AppDataPref.userId
        requestSubscribeByMeDto.currPage = currentPage
        requestSubscribeByMeDto.pageSize = curPageSize

        subscribeViewModel.subscribeByMeList(requestSubscribeByMeDto)
    }

    private fun setSubscribeList(
        data: ArrayList<ResponseSubscribeListDto.Root>,
        subscribeType: SubscribeType
    ) {
        binding.recyclerSubscribe.layoutManager = LinearLayoutManager(this)
        subscribeListAdapter = SubscribeListAdapter(
            this,
            data,
            subscribeType,
            object : SubscribeListAdapter.OnItemDeleteListener {
                override fun onDelete(position: Int, view: View) {
                    showPopupWindow(view, position, data[position])
                }
            },
            object : SubscribeListAdapter.OnItemClickListener {
                override fun onClick(position: Int, posData: ResponseSubscribeListDto.Root) {
                    if (subscribeType == SubscribeType.MY) {
                        goSubscribeDetail(posData.sub_user_id.toString(), posData.sub_nick_nm.toString())
                    }
                }
            },
            object : SubscribeListAdapter.OnSubscribeListener {
                override fun onSubscribe(position: Int, posData: ResponseSubscribeListDto.Root) {
                    //TODO nothing
                }
            })
        binding.recyclerSubscribe.adapter = subscribeListAdapter
    }

    private fun showPopupWindow(anchor: View, position: Int, root: ResponseSubscribeListDto.Root) {
        val popBinding = PopupSubscribeBinding.inflate(LayoutInflater.from(this))

        val popupWindow = PopupWindow(
            popBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true
        )

        popBinding.menuItem1.setOnClickListener {
            val dialogCommon = DialogCommon()
            dialogCommon.showSubscribeDel(supportFragmentManager, {
                //닫기
            }, {
                //삭제
                setSubscribeUpdate(root, 0)
            })
            popupWindow.dismiss()
        }

        popupWindow.elevation = 10.0f
        popupWindow.showAsDropDown(anchor, 0, 0)
    }

    private fun setSubscribeUpdate(root: ResponseSubscribeListDto.Root, subscription_se: Int) {
        val requestSubscribeUpdateDto = RequestSubscribeUpdateDto()
        requestSubscribeUpdateDto.user_id = AppDataPref.userId
        requestSubscribeUpdateDto.sub_user_id = root.sub_user_id.toString()
        requestSubscribeUpdateDto.subscription_se = subscription_se

        subscribeViewModel.subscribeUpdate(requestSubscribeUpdateDto)
    }

    private fun setSelectedViewBackground() {
        if (binding.clMyView.isSelected) {
            binding.desc1.setTextColor(ContextCompat.getColor(this, R.color.color_8e5d4b))
            binding.subscribeI.setTextColor(ContextCompat.getColor(this, R.color.color_8e5d4b))
        } else {
            binding.desc1.setTextColor(ContextCompat.getColor(this, R.color.color_black))
            binding.subscribeI.setTextColor(ContextCompat.getColor(this, R.color.color_black))
        }

        if (binding.clMeView.isSelected) {
            binding.desc2.setTextColor(ContextCompat.getColor(this, R.color.color_8e5d4b))
            binding.subscribeMe.setTextColor(ContextCompat.getColor(this, R.color.color_8e5d4b))
        } else {
            binding.desc2.setTextColor(ContextCompat.getColor(this, R.color.color_black))
            binding.subscribeMe.setTextColor(ContextCompat.getColor(this, R.color.color_black))
        }
    }

    private fun goSubscribeDetail(subUserId: String, subUserName: String) {
        val intent = Intent(this, SubscribeDetailActivity::class.java)
        intent.putExtra("SUB_USER_ID", subUserId)
        intent.putExtra("SUB_USER_NAME", subUserName)
        startActivity(intent)
    }
}