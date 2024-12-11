package com.syncrown.arpang.ui.component.home.tab5_more.block

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityBlockUserBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestBlockUserListDto
import com.syncrown.arpang.network.model.RequestTargetUserBlockDto
import com.syncrown.arpang.network.model.ResponseBlockUserListDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CustomToast
import com.syncrown.arpang.ui.commons.CustomToastType
import com.syncrown.arpang.ui.component.home.tab5_more.block.adapter.BlockUserAdapter
import kotlinx.coroutines.launch

class BlockUserActivity : BaseActivity(), BlockUserAdapter.OnItemClickListener {
    private lateinit var binding: ActivityBlockUserBinding
    private val blockUserViewModel: BlockUserViewModel by viewModels()

    private lateinit var adapter: BlockUserAdapter
    private var data: ArrayList<ResponseBlockUserListDto.Root> = ArrayList()
    private var curPage = 1
    private var curPageSize = 18

    private var selectItemPos = 0

    override fun observeViewModel() {
        lifecycleScope.launch {
            blockUserViewModel.blockUserListResponseLiveData()
                .observe(this@BlockUserActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.root ?: ArrayList()

                            if (curPage > 1 && data.isEmpty()) {
                                curPage = 1
                            } else {
                                adapter.addMoreData(data)
                                curPage++
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
            blockUserViewModel.targetUserBlockResponseLiveData()
                .observe(this@BlockUserActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data?.msgCode
                            if (data.equals("SUCCESS")) {
                                adapter.updateItem(selectItemPos)
                            } else {
                                Log.e("jung", "차단 해제 실패")
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
        binding = ActivityBlockUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        binding.actionbar.actionTitle.text = getString(R.string.cutoff_title)

        setBlockList()

        fetchData()
    }

    private fun fetchData() {
        val requestBlockUserListDto = RequestBlockUserListDto()
        requestBlockUserListDto.user_id = AppDataPref.userId
        requestBlockUserListDto.currPage = curPage
        requestBlockUserListDto.pageSize = curPageSize

        blockUserViewModel.blockUserList(requestBlockUserListDto)
    }

    private fun setBlockList() {
        clearItemDecorations(binding.recyclerCut)

        binding.recyclerCut.layoutManager = LinearLayoutManager(this)

        adapter = BlockUserAdapter(data, this)
        binding.recyclerCut.adapter = adapter

        setupRecyclerViewScrollListener()
    }

    private fun clearItemDecorations(recyclerView: RecyclerView) {
        val decorationCount = recyclerView.itemDecorationCount
        for (i in decorationCount - 1 downTo 0) {
            recyclerView.removeItemDecorationAt(i)
        }
    }

    private fun setupRecyclerViewScrollListener() {
        binding.recyclerCut.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!binding.recyclerCut.canScrollVertically(1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                ) {
                    fetchData()
                }
            }
        })
    }

    override fun onBlockClick(position: Int, blockUserId: String) {
        //차단 해제
        selectItemPos = position
        setBlockRelease(blockUserId)
    }

    private fun setBlockRelease(blockUserId: String) {
        val requestTargetUserBlockDto = RequestTargetUserBlockDto().apply {
            user_id = AppDataPref.userId
            block_user_id = blockUserId
            block_se = 0
        }

        blockUserViewModel.userBlock(requestTargetUserBlockDto)
    }
}