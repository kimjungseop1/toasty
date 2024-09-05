package com.syncrown.toasty.ui.component.home.tab1_home.ar_print.videoselect

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.syncrown.toasty.databinding.ActivityVideoSelectBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.commons.CommonFunc
import com.syncrown.toasty.ui.commons.GridSpacingItemDecoration
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.videoselect.adapter.GridItemAdapter
import com.syncrown.toasty.ui.component.home.tab1_home.ar_print.videoselect.adapter.VideoInfo
import com.syncrown.toasty.ui.component.home.tab1_home.main.adapter.MainEventAdapter


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

        binding.actionbar.actionBack.setOnClickListener { finish() }


        setVideoList()
    }

    private fun getAllMp4FilesWithDuration(context: Context): List<VideoInfo> {
        val videoList = mutableListOf<VideoInfo>()
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATA, // Deprecated in API 29, use ContentUris instead
            MediaStore.Video.Media.DURATION // Duration in milliseconds
        )

        val selection = "${MediaStore.Video.Media.MIME_TYPE} = ?"
        val selectionArgs = arrayOf("video/mp4")

        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

        val queryUri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val cursor = context.contentResolver.query(
            queryUri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val dataColumn =
                it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA) // Deprecated in API 29
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val filePath =
                    it.getString(dataColumn) // Deprecated in API 29, consider using contentUri to access file
                val duration = it.getLong(durationColumn) // Duration in milliseconds

                videoList.add(VideoInfo(filePath, duration))
            }
        }

        return videoList
    }

    private fun setVideoList() {
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

        val adapter = GridItemAdapter(
            this,
            getAllMp4FilesWithDuration(this),
            object : GridItemAdapter.OnItemClickListener {
                override fun onItemClick(videoInfo: VideoInfo) {
                    Log.e("jung", "path : " + videoInfo.filePath)
                }
            })

        binding.videoListView.adapter = adapter
    }


}