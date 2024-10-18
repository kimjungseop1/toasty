package com.syncrown.arpang.ui.component.home.tab1_home.empty_cartridge

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.syncrown.arpang.R
import com.syncrown.arpang.databinding.ActivityEmptyCartridgeBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import com.syncrown.arpang.ui.commons.HorizontalSpaceItemDecoration
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videoselect.VideoSelectActivity
import com.syncrown.arpang.ui.component.home.tab1_home.empty_cartridge.adapter.CartridgeContentGridItemAdapter
import com.syncrown.arpang.ui.component.home.tab1_home.empty_cartridge.adapter.CartridgeMenuListAdapter
import com.syncrown.arpang.ui.component.home.tab1_home.festival_sticker.EditFestivalActivity
import com.syncrown.arpang.ui.component.home.tab1_home.free_print.EditFreePrintActivity
import com.syncrown.arpang.ui.component.home.tab1_home.label_sticker.EditLabelStickerActivity
import com.syncrown.arpang.ui.component.home.tab1_home.life2cut.image_select.ImageSelectActivity
import com.syncrown.arpang.ui.component.home.tab1_home.empty_cartridge.preview.PreViewCartridgeActivity

class EmptyCartridgeActivity : BaseActivity() {
    private lateinit var binding: ActivityEmptyCartridgeBinding
    companion object {
        private var actionTitle: String = ""
    }

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityEmptyCartridgeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionTitle = intent.getStringExtra("FROM_HOME_CATEGORY").toString()
        binding.actionbar.actionTitle.text = actionTitle
        binding.actionbar.actionBack.setOnClickListener {
            finish()
        }

        setCartridgeMenuTab()

    }

    private fun setCartridgeMenuTab() {
        val arrayList = ArrayList<String>()
        arrayList.add("추천")
        arrayList.add("봄에 어울리는")
        arrayList.add("가족 앨범")
        arrayList.add("AR 용지 분류1")
        arrayList.add("AR 용지 분류2")

        binding.recyclerCartridge.itemDecorationCount.let { count ->
            for (i in 0 until count) {
                binding.recyclerCartridge.removeItemDecorationAt(0)
            }
        }

        val menuListAdapter = CartridgeMenuListAdapter(this, arrayList)
        binding.recyclerCartridge.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerCartridge.adapter = menuListAdapter
        menuListAdapter.setOnItemClickListener(object :
            CartridgeMenuListAdapter.OnItemClickListener {
            override fun onClick(position: Int) {
                Log.e("jung", "click $position")
                setCartridgeContent()
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

    private fun setCartridgeContent() {
        val arrayList = ArrayList<String>()
        arrayList.add("비규격")
        arrayList.add("스튜디오")
        arrayList.add("폴라로이드")
        arrayList.add("용지이름1")
        arrayList.add("용지이름2")

        val decorationCount = binding.recyclerGridView.itemDecorationCount
        for (i in decorationCount - 1 downTo 0) {
            binding.recyclerGridView.removeItemDecorationAt(i)
        }

        val contentListAdapter = CartridgeContentGridItemAdapter(
            arrayList,
            object : CartridgeContentGridItemAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    Log.e("jung", "content click : $position")

                    when(actionTitle) {
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