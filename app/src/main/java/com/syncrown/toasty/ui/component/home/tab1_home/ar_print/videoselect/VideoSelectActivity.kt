package com.syncrown.toasty.ui.component.home.tab1_home.ar_print.videoselect

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.syncrown.toasty.R
import com.syncrown.toasty.databinding.ActivityVideoSelectBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.commons.CommonFunc

class VideoSelectActivity : BaseActivity() {
    private lateinit var binding: ActivityVideoSelectBinding
    private lateinit var videoSelectViewModel: VideoSelectViewModel

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityVideoSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setVideoList()
    }

    private fun setVideoList() {
        val items = listOf(
            GridItem(R.drawable.icon_home, "00:00"),
            GridItem(R.drawable.icon_home, "00:00"),
            GridItem(R.drawable.icon_home, "00:00"),
            GridItem(R.drawable.icon_home, "00:00")
        )

        val spanCount = 4 // 3 열 그리드 레이아웃
        val spacing = 6 // dp로 설정된 아이템 간 간격
        val includeEdge = false // 아이템의 가장자리에 간격 포함 여부

        binding.videoListView.layoutManager = GridLayoutManager(this, spanCount)
        binding.videoListView.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                CommonFunc.dpToPx(spacing, this),
                includeEdge
            )
        )

        binding.videoListView.adapter = GridItemAdapter(items)
    }



}