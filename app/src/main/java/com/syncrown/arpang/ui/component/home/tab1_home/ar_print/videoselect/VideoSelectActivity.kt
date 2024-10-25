package com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videoselect

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.GridLayoutManager
import com.syncrown.arpang.databinding.ActivityVideoSelectBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.CommonFunc
import com.syncrown.arpang.ui.commons.GridSpacingItemDecoration
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videoselect.adapter.VideoInfo
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videoselect.adapter.VideoSelectGridItemAdapter
import com.syncrown.arpang.ui.component.home.tab1_home.ar_print.videotrimmer.TrimVideoActivity


class VideoSelectActivity : BaseActivity() {
    private lateinit var binding: ActivityVideoSelectBinding
    private val videoSelectViewModel: VideoSelectViewModel by viewModels()

    override fun observeViewModel() {
        videoSelectViewModel.folderNames.observe(this) { folderNames ->
            setSpinner(folderNames)
        }

        videoSelectViewModel.videoList.observe(this) { videoList ->
            setVideoList(videoList)
        }
    }

    override fun initViewBinding() {
        binding = ActivityVideoSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionTitle: String = intent.getStringExtra("FROM_HOME_CATEGORY").toString()
        binding.actionbar.actionTitle.text = actionTitle
        binding.actionbar.actionBack.setOnClickListener { finish() }

        videoSelectViewModel.loadFolderNames(this)
        videoSelectViewModel.loadVideoList(this)
    }

    private fun setSpinner(folderNames: List<String>) {
        val arrayList = ArrayList<String>()
        arrayList.add(
            "전체보기" + " (" + folderNames.sumOf {
                it.substringAfterLast(" (").substringBeforeLast(")").toInt()
            } + ")"
        )
        arrayList.addAll(folderNames)

        binding.folderSpinner.attachDataSource(arrayList)
        binding.folderSpinner.selectedIndex = 0
        binding.folderSpinner.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            val selectedFolder = parent.getItemAtPosition(position).toString().split(" (")[0]
            videoSelectViewModel.loadVideoList(this, selectedFolder)
        }
    }

    private fun setVideoList(videoList: List<VideoInfo>) {
        val spanCount = 4
        val spacing = 6
        val includeEdge = false

        binding.videoListView.itemDecorationCount.let { count ->
            for (i in 0 until count) {
                binding.videoListView.removeItemDecorationAt(0)
            }
        }

        binding.videoListView.layoutManager = GridLayoutManager(this, spanCount)
        binding.videoListView.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                CommonFunc.dpToPx(spacing, this),
                includeEdge
            )
        )

        val adapter = VideoSelectGridItemAdapter(
            this,
            videoList,
            object : VideoSelectGridItemAdapter.OnItemClickListener {
                override fun onItemClick(videoInfo: VideoInfo) {
                    goVideoTrim(videoInfo.filePath)
                }
            })

        binding.videoListView.adapter = adapter
    }

    @OptIn(UnstableApi::class)
    private fun goVideoTrim(filePath: String) {
        val intent = Intent(this, TrimVideoActivity::class.java)
        intent.putExtra("VIDEO_FILE_PATH", filePath)
        startActivity(intent)
        finish()
    }


}