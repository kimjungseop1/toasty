package com.syncrown.arpang.ui.component.home.tab1_home.empty_cartridge

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityEmptyCartridgeBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestCartridgeListByTagDto
import com.syncrown.arpang.network.model.RequestRecommendTagListDto
import com.syncrown.arpang.network.model.ResponseCartridgeListByTagDto
import com.syncrown.arpang.network.model.ResponseRecommendTagListDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import com.syncrown.arpang.ui.commons.HorizontalSpaceItemDecoration
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videoselect.VideoSelectActivity
import com.syncrown.arpang.ui.component.home.tab1_home.empty_cartridge.adapter.CartridgeContentGridItemAdapter
import com.syncrown.arpang.ui.component.home.tab1_home.empty_cartridge.adapter.CartridgeMenuListAdapter
import com.syncrown.arpang.ui.component.home.tab1_home.empty_cartridge.preview.PreViewCartridgeActivity
import com.syncrown.arpang.ui.component.home.tab1_home.festival_sticker.EditFestivalActivity
import com.syncrown.arpang.ui.component.home.tab1_home.free_print.EditFreePrintActivity
import com.syncrown.arpang.ui.component.home.tab1_home.label_sticker.EditLabelStickerActivity
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.image_select.ImageSelectActivity
import kotlinx.coroutines.launch

class EmptyCartridgeActivity : BaseActivity() {
    private lateinit var binding: ActivityEmptyCartridgeBinding
    private val emptyCartridgeViewModel: EmptyCartridgeViewModel by viewModels()

    companion object {
        private var actionTitle: String = ""
        private var mainMenuCode: String = ""
    }

    override fun observeViewModel() {
        lifecycleScope.launch {
            emptyCartridgeViewModel.recommendTagListResponseLiveData()
                .observe(this@EmptyCartridgeActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data
                            data?.let {
                                setCartridgeMenuTab(it)
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
            emptyCartridgeViewModel.cartridgeByTagListResponseLiveData()
                .observe(this@EmptyCartridgeActivity) { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val data = result.data
                            data?.let {
                                setCartridgeContent(it)
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
        binding = ActivityEmptyCartridgeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionTitle = intent.getStringExtra("FROM_HOME_CATEGORY").toString()
        mainMenuCode = intent.getStringExtra("MAIN_MENU_CODE").toString()

        binding.actionbar.actionTitle.text = actionTitle
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        getTagList()
    }

    private fun getTagList() {
        val requestRecommendTagListDto = RequestRecommendTagListDto()
        requestRecommendTagListDto.app_id = "APP_ARPANG"
        emptyCartridgeViewModel.responseRecommendTagList(requestRecommendTagListDto)
    }

    private fun setCartridgeMenuTab(data: ResponseRecommendTagListDto) {
        binding.recyclerCartridge.itemDecorationCount.let { count ->
            for (i in 0 until count) {
                binding.recyclerCartridge.removeItemDecorationAt(0)
            }
        }

        val menuListAdapter = CartridgeMenuListAdapter(this, data)
        binding.recyclerCartridge.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerCartridge.adapter = menuListAdapter
        menuListAdapter.setOnItemClickListener(object :
            CartridgeMenuListAdapter.OnItemClickListener {
            override fun onClick(position: Int) {
                getCartridgeByTag(data.root[position].seq_no.toString())
            }
        })
        binding.recyclerCartridge.addItemDecoration(
            HorizontalSpaceItemDecoration(
                CommonFunc.dpToPx(
                    5,
                    this
                )
            )
        )

    }

    private fun getCartridgeByTag(seqNo: String) {
        val requestCartridgeListByTagDto = RequestCartridgeListByTagDto()
        requestCartridgeListByTagDto.tag_seq_no = seqNo
        requestCartridgeListByTagDto.app_id = "APP_ARPANG"
        requestCartridgeListByTagDto.menu_code = mainMenuCode
        emptyCartridgeViewModel.cartridgeByTag(requestCartridgeListByTagDto)
    }

    private fun setCartridgeContent(responseCartridgeListByTagDto: ResponseCartridgeListByTagDto) {
        val decorationCount = binding.recyclerGridView.itemDecorationCount
        for (i in decorationCount - 1 downTo 0) {
            binding.recyclerGridView.removeItemDecorationAt(i)
        }

        val contentListAdapter = CartridgeContentGridItemAdapter(
            responseCartridgeListByTagDto,
            object : CartridgeContentGridItemAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    when (actionTitle) {
                        getString(R.string.cartridge_empty_action_text_1) -> {
                            // ar 영상 인쇄
                            goSelectVideo()
                        }

                        getString(R.string.cartridge_empty_action_text_2) -> {
                            // 인생 두컷
                            goImageSelect()
                        }

                        getString(R.string.cartridge_empty_action_text_4) -> {
                            // 라벨 스티커
                            goLabelSticker()
                        }

                        getString(R.string.cartridge_empty_action_text_5) -> {
                            // 행사 스트커
                            goFestivalSticker()
                        }

                        getString(R.string.cartridge_empty_action_text_3) -> {
                            // 자유 인쇄
                            goFreePrint()
                        }

                    }
                }
            },
            object : CartridgeContentGridItemAdapter.OnScaleClickListener {
                override fun onScaleClick(position: Int) {
                    Log.e("jung", "content scale click : $position")
                    goPreviewCartridge()
                }
            })
        binding.recyclerGridView.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerGridView.adapter = contentListAdapter
        binding.recyclerGridView.addItemDecoration(
            GridSpacingItemDecoration(
                3,
                CommonFunc.dpToPx(8, this),
                false
            )
        )
    }

    private fun goSelectVideo() {
        val intent = Intent(this, VideoSelectActivity::class.java)
        intent.putExtra(
            "FROM_HOME_CATEGORY",
            getString(R.string.cartridge_empty_action_text_1)
        )
        startActivity(intent)
        finish()
    }

    private fun goImageSelect() {
        val intent = Intent(this, ImageSelectActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goPreviewCartridge() {
        val intent = Intent(this, PreViewCartridgeActivity::class.java)
        intent.putExtra("FROM_HOME_CATEGORY", actionTitle)
        startActivity(intent)
    }

    private fun goFestivalSticker() {
        val intent = Intent(this, EditFestivalActivity::class.java)
        startActivity(intent)
    }

    private fun goLabelSticker() {
        val intent = Intent(this, EditLabelStickerActivity::class.java)
        startActivity(intent)
    }

    private fun goFreePrint() {
        val intent = Intent(this, EditFreePrintActivity::class.java)
        startActivity(intent)
    }
}