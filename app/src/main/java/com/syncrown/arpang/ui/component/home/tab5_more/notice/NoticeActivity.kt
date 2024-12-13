package com.syncrown.arpang.ui.component.home.tab5_more.notice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityNoticeBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestNoticeListDto
import com.syncrown.arpang.network.model.ResponseNoticeListDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.component.home.tab5_more.notice.adapter.NoticeListAdapter
import com.syncrown.arpang.ui.component.home.tab5_more.notice.detail.NoticeDetailActivity
import kotlinx.coroutines.launch

class NoticeActivity : BaseActivity() {
    private lateinit var binding: ActivityNoticeBinding
    private val noticeViewModel: NoticeViewModel by viewModels()

    override fun observeViewModel() {
        lifecycleScope.launch {
            noticeViewModel.noticeListResponseLiveData().observe(this@NoticeActivity) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val data = result.data?.root ?: ArrayList()

                        updateUI(data)
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
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }
        binding.actionbar.actionTitle.text = getString(R.string.notice_title)


        setNoticeList()
    }

    private fun setNoticeList() {
        val requestNoticeListDto = RequestNoticeListDto().apply {
            app_id = "APP_ARPANG"
        }

        noticeViewModel.noticeList(requestNoticeListDto)
    }

    private fun updateUI(data: ArrayList<ResponseNoticeListDto.Root>) {
        val noticeListAdapter = NoticeListAdapter(this, data)
        binding.recyclerNotice.layoutManager = LinearLayoutManager(this)
        binding.recyclerNotice.adapter = noticeListAdapter
        noticeListAdapter.setOnItemClickListener(object : NoticeListAdapter.OnItemClickListener {
            override fun onClick(position: Int, bbsid: String) {
                goDetail(bbsid)
            }
        })

    }

    private fun goDetail(bbsid: String) {
        val intent = Intent(this, NoticeDetailActivity::class.java)
        intent.putExtra("NOTICE_DETAIL_BBSID", bbsid)
        startActivity(intent)
    }
}